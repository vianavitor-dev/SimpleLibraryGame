package com.vianavitor.simplelibrarygame.dto.response;


import jakarta.annotation.Nullable;

import java.util.Set;

public record ClassroomWithUsersResponse(
        Long id,
        String name,
        String publicCode,
        @Nullable Set<UserInClassroomResponse> users
) {
}
