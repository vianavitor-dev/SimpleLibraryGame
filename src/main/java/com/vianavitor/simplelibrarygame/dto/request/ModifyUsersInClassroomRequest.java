package com.vianavitor.simplelibrarygame.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.Set;

public record ModifyUsersInClassroomRequest(
        @NotNull Set<Long> userIds
) {}