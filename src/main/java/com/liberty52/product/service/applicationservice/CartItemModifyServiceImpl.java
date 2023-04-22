package com.liberty52.product.service.applicationservice;

import com.liberty52.product.global.adapter.S3Uploader;
import com.liberty52.product.global.exception.external.CustomProductNotFoundExcpetion;
import com.liberty52.product.global.exception.external.NotYourResourceException;
import com.liberty52.product.global.exception.external.OptionDetailNotFoundException;
import com.liberty52.product.global.exception.external.OrderItemCannotModifiedException;
import com.liberty52.product.service.controller.dto.CartModifyRequestDto;
import com.liberty52.product.service.entity.CustomProduct;
import com.liberty52.product.service.entity.CustomProductOption;
import com.liberty52.product.service.entity.OptionDetail;
import com.liberty52.product.service.repository.CustomProductOptionRepository;
import com.liberty52.product.service.repository.CustomProductRepository;
import com.liberty52.product.service.repository.OptionDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class CartItemModifyServiceImpl implements CartItemModifyService{

  private final S3Uploader s3Uploader;
  private final CustomProductRepository customProductRepository;
  private final OptionDetailRepository optionDetailRepository;

  private final CustomProductOptionRepository customProductOptionRepository;

  @Transactional
  @Override
  public void modifyUserCartItem(String authId, CartModifyRequestDto dto, MultipartFile imageFile, String customProductId) {
    modifyCartItem(authId,dto,imageFile,customProductId);
  }
  @Transactional
  @Override
  public void modifyGuestCartItem(String guestId, CartModifyRequestDto dto, MultipartFile imageFile, String customProductId) {
    modifyCartItem(guestId,dto,imageFile,customProductId);
  }

  private void modifyCartItem(String ownerId, CartModifyRequestDto dto, MultipartFile imageFile, String customProductId) {
    CustomProduct customProduct = customProductRepository.findById(customProductId).orElseThrow((CustomProductNotFoundExcpetion::new));
    validCartItem(ownerId, customProduct);
    modifyOptionsDetail(dto, customProduct,imageFile);
  }

  private void validCartItem(String authId, CustomProduct customProduct) {
    if(customProduct.isInOrder()){
      throw new OrderItemCannotModifiedException();
    }

    if(!customProduct.getCart().getAuthId().equals(authId)){
      throw new NotYourResourceException("customProduct", authId);
    }
  }

  private void modifyOptionsDetail(CartModifyRequestDto dto, CustomProduct customProduct,MultipartFile imageFile) {
    if (imageFile != null){
      String customPictureUrl = uploadImage(imageFile);
      customProduct.modifyCustomPictureUrl(customPictureUrl);
    }
    customProduct.modifyQuantity(dto.getQuantity());
    customProductOptionRepository.deleteAll(customProduct.getOptions());
    for (String optionDetailName : dto.getOptions()){
      CustomProductOption customProductOption = CustomProductOption.create();
      OptionDetail optionDetail = optionDetailRepository.findByName(optionDetailName)
          .orElseThrow(() -> new OptionDetailNotFoundException(optionDetailName));
      customProductOption.associate(optionDetail);
      customProductOption.associate(customProduct);
      customProductOptionRepository.save(customProductOption);
    }
  }

  private String uploadImage(MultipartFile multipartFile) {
    if(multipartFile == null) {
      return null;
    }
    return s3Uploader.upload(multipartFile);
  }

}