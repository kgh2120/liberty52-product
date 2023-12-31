package com.liberty52.product.service.applicationservice;

import com.liberty52.product.service.controller.dto.ReviewCreateRequestDto;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface ReviewCreateService {
  void createReview(String reviewerId,ReviewCreateRequestDto dto, List<MultipartFile> imageFile);
}
