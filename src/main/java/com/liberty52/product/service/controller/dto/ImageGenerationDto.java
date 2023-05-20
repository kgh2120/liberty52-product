package com.liberty52.product.service.controller.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public class ImageGenerationDto {
    public record Request(@NotEmpty @Size(max = 1000) String prompt, @Min(1) @Max(10) Integer n) {
    }
    public record Response(List<String> urls) {
    }
}
