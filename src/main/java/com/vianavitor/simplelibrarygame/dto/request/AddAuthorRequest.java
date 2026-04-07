package com.vianavitor.simplelibrarygame.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AddAuthorRequest(
        @NotBlank String name
) {}