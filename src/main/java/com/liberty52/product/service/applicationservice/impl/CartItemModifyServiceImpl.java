package com.liberty52.product.service.applicationservice.impl;

import com.liberty52.product.global.adapter.s3.S3UploaderApi;
import com.liberty52.product.global.exception.external.badrequest.CartItemRequiredButOrderItemFoundException;
import com.liberty52.product.global.exception.external.forbidden.NotYourCartItemException;
import com.liberty52.product.global.exception.external.notfound.CustomProductNotFoundByIdException;
import com.liberty52.product.global.exception.external.notfound.OptionDetailNotFoundByNameException;
import com.liberty52.product.service.applicationservice.CartItemModifyService;
import com.liberty52.product.service.controller.dto.CartModifyRequestDto;
import com.liberty52.product.service.entity.CustomProduct;
import com.liberty52.product.service.entity.CustomProductOption;
import com.liberty52.product.service.entity.OptionDetail;
import com.liberty52.product.service.event.internal.ImageRemovedEvent;
import com.liberty52.product.service.event.internal.dto.ImageRemovedEventDto;
import com.liberty52.product.service.repository.CustomProductOptionRepository;
import com.liberty52.product.service.repository.CustomProductRepository;
import com.liberty52.product.service.repository.OptionDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class CartItemModifyServiceImpl implements CartItemModifyService {

  private final S3UploaderApi s3Uploader;
  private final ApplicationEventPublisher eventPublisher;
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
    CustomProduct customProduct = customProductRepository.findById(customProductId).orElseThrow(() -> new CustomProductNotFoundByIdException(customProductId));
    validCartItem(ownerId, customProduct);
    modifyOptionsDetail(dto, customProduct,imageFile);
  }

  private void validCartItem(String authId, CustomProduct customProduct) {
    if(customProduct.isInOrder()){
      throw new CartItemRequiredButOrderItemFoundException(customProduct.getId());
    }

    if(!customProduct.getCart().getAuthId().equals(authId)){
      throw new NotYourCartItemException(authId);
    }
  }

  private void modifyOptionsDetail(CartModifyRequestDto dto, CustomProduct customProduct,MultipartFile imageFile) {
    customProduct.modifyQuantity(dto.getQuantity());
    if (!dto.getOptions().isEmpty()){
      customProductOptionRepository.deleteAll(customProduct.getOptions());
      for (String optionDetailName : dto.getOptions()){
        CustomProductOption customProductOption = CustomProductOption.create();
        OptionDetail optionDetail = optionDetailRepository.findByName(optionDetailName)
            .orElseThrow(() -> new OptionDetailNotFoundByNameException(optionDetailName));
        customProductOption.associate(optionDetail);
        customProductOption.associate(customProduct);
        customProductOptionRepository.save(customProductOption);
      }
    }
    if (imageFile != null){
      String url = customProduct.getUserCustomPictureUrl();
      String customPictureUrl = s3Uploader.upload(imageFile);
      customProduct.modifyCustomPictureUrl(customPictureUrl);
      eventPublisher.publishEvent(new ImageRemovedEvent(this, new ImageRemovedEventDto(url)));
    }
  }
}
