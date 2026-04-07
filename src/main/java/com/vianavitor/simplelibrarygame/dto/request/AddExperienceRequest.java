package com.vianavitor.simplelibrarygame.dto.request;

import jakarta.validation.constraints.PositiveOrZero;

public record AddExperienceRequest(
        @PositiveOrZero(message = "Experience cannot be negative") Integer exp
) {}