package com.vianavitor.simplelibrarygame.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ChangeNameRequest(
        @NotBlank String name
) {}