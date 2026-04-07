package com.vianavitor.simplelibrarygame.dto.request;

import jakarta.validation.constraints.Positive;

public record AverageReadingTimeRequest(
        @Positive(message = "Reading time must be positive") Double readingTimeInMins
) {}