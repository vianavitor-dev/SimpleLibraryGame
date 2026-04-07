package com.vianavitor.simplelibrarygame.dto.request;

import com.vianavitor.simplelibrarygame.model.Genre;
import com.vianavitor.simplelibrarygame.model.Student;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

public record RegisterStudentRequest(
        @Valid @NotNull Student student,
        String classroomCode,          // optional
        Set<Genre> favoriteGenres      // optional
) {}