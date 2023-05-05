package com.liberty52.product.service.controller;

import com.liberty52.product.service.applicationservice.ReviewCreateService;
import com.liberty52.product.service.controller.dto.ReplyCreateRequestDto;
import com.liberty52.product.service.controller.dto.ReviewCreateRequestDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class ReviewCreateController {

  private final ReviewCreateService reviewCreateService;

  @PostMapping("/reviews")
  @ResponseStatus(HttpStatus.CREATED)
  public void reviewCreate( @RequestHeader(HttpHeaders.AUTHORIZATION) String reviewerId, @Validated @RequestPart ReviewCreateRequestDto dto,
      @RequestPart(value = "images",required = false) List<MultipartFile> images) {
    reviewCreateService.createReview(reviewerId,dto,images);
  }
}
