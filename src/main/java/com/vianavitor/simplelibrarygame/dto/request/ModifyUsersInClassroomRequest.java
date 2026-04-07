package com.vianavitor.simplelibrarygame.dto.request;

import com.vianavitor.simplelibrarygame.model.utils.classes.UserClassroom;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

public record ModifyUsersInClassroomRequest(
        @NotNull Set<UserClassroom> users
) {}