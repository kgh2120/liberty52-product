package com.liberty52.product.service.controller;


import com.liberty52.product.service.applicationservice.ReviewReplyModifyService;
import com.liberty52.product.service.controller.dto.ReplyModifyRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ReviewReplyModifyController {

    private final ReviewReplyModifyService reviewReplyModifyService;

    @PutMapping("/admin/reviews/{reviewId}/replies/{replyId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void modifyReviewReplyByAdmin(@RequestHeader(HttpHeaders.AUTHORIZATION) String adminId,
                            @RequestHeader("LB-Role") String role,
                            @Validated @RequestBody ReplyModifyRequestDto dto,
                            @PathVariable String reviewId,
                            @PathVariable String replyId) {
        reviewReplyModifyService.modifyReviewReplyByAdmin(adminId, role, dto, reviewId, replyId);
    }

}
