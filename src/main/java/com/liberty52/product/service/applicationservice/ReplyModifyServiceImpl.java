package com.liberty52.product.service.applicationservice;

import com.liberty52.product.global.exception.external.badrequest.BadRequestException;
import com.liberty52.product.global.exception.external.forbidden.InvalidRoleException;
import com.liberty52.product.global.exception.external.notfound.ResourceNotFoundException;
import com.liberty52.product.service.controller.dto.ReplyModifyRequestDto;
import com.liberty52.product.service.entity.Reply;
import com.liberty52.product.service.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.liberty52.product.global.contants.RoleConstants.ADMIN;

@Service
@Transactional
@RequiredArgsConstructor
public class ReplyModifyServiceImpl implements ReplyModifyService {
    private final ReplyRepository replyRepository;

    @Override
    public void modify(String adminId, String role, ReplyModifyRequestDto dto, String reviewId, String replyId) {
        if(!ADMIN.equals(role))
            throw new InvalidRoleException(role);
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new ResourceNotFoundException("Reply", "id", replyId));
        if(!reply.getReview().getId().equals(reviewId))
            throw new BadRequestException("Review와 Reply가 매칭되지 않습니다.");
        reply.modify(dto.getContent());
    }
}
