package com.vianavitor.simplelibrarygame.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record RateBookRequest(
        @NotNull @Min(0) @Max(5) Integer rate
) {}