package com.vianavitor.simplelibrarygame.dto.response;

import jakarta.annotation.Nullable;

public record StudentStatsResponse (
        Long id,
        @Nullable UsersBookReadHistoryResponse currentBook,
        int level,
        int currentExperience,
        int maxLvlExperience,
        int ongoingStreak,
        int averageReadingTime,
        int readingCount
) {
}
