package com.vianavitor.simplelibrarygame.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateGroupRequest(
        @NotBlank String name,
        @NotNull Long studentId
) {}