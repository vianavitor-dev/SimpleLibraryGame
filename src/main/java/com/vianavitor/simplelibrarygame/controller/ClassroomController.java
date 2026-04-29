package com.vianavitor.simplelibrarygame.controller;

import com.vianavitor.simplelibrarygame.dto.ApiResponse;
import com.vianavitor.simplelibrarygame.dto.request.ChangeNameRequest;
import com.vianavitor.simplelibrarygame.dto.request.ModifyUsersInClassroomRequest;
import com.vianavitor.simplelibrarygame.dto.response.ClassroomResponse;
import com.vianavitor.simplelibrarygame.dto.response.UserInClassroomResponse;
import com.vianavitor.simplelibrarygame.model.BookReadHistory;
import com.vianavitor.simplelibrarygame.model.Classroom;
import com.vianavitor.simplelibrarygame.model.Student;
import com.vianavitor.simplelibrarygame.model.utils.classes.UserClassroom;
import com.vianavitor.simplelibrarygame.service.ClassroomService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/classrooms")
public class ClassroomController {

    @Autowired
    private ClassroomService classroomService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createClassroom(@RequestParam String name, HttpServletRequest request) {
        classroomService.create(name);
        ApiResponse<Void> response = ApiResponse.success(null, "Classroom created", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<ClassroomResponse>>> getAll(
            HttpServletRequest req
    ) {
        List<ClassroomResponse> result = classroomService.getAllClasses()
                .stream()
                .map(this::classroomToDto)
                .toList();

        return ResponseEntity.ok(ApiResponse.success(result, "Result of searching all classrooms", req.getRequestURI()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClassroomResponse>> getById(
            @PathVariable Long id,
            HttpServletRequest req
    ) {
        ClassroomResponse result = this.classroomToDto(classroomService.getClassroom(id));
        return ResponseEntity.ok(ApiResponse.success(result, "Result of searching by ID", req.getRequestURI()));
    }

    @PutMapping("/{id}/users")
    public ResponseEntity<ApiResponse<Set<UserInClassroomResponse>>> modifyUsers(
            @PathVariable Long id,
            @Valid @RequestBody ModifyUsersInClassroomRequest request,
            HttpServletRequest req
    ) {
        Set<UserInClassroomResponse> updated = classroomService.modifyUsersInClassroom(id, request.userIds())
                .stream()
                .map(this::userClassroomToDtoResp)
                .collect(Collectors.toSet());

        return ResponseEntity.ok(ApiResponse.success(updated, "Users updated", req.getRequestURI()));
    }

    @PatchMapping("/{id}/name")
    public ResponseEntity<ApiResponse<ClassroomResponse>> changeName(
            @PathVariable Long id,
            @Valid @RequestBody ChangeNameRequest request,
            HttpServletRequest req
    ) {
        ClassroomResponse classroom = this.classroomToDto(classroomService.changeName(id, request.name()));
        return ResponseEntity.ok(ApiResponse.success(classroom, "Name changed", req.getRequestURI()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteClassroom(@PathVariable Long id, HttpServletRequest request) {
        classroomService.delete(id);
        ApiResponse<Void> response = ApiResponse.success(null, "Classroom deleted", request.getRequestURI());
        return ResponseEntity.ok(response);
    }

    private UserInClassroomResponse userClassroomToDtoResp(UserClassroom user) {
        if (user instanceof Student) {
            BookReadHistory history = ((Student) user).getStats().getCurrentBook();

            return new UserInClassroomResponse(
                    user.getId(), user.getName(), user.getUsername(), user.getLastLogin(),
                    new UserInClassroomResponse.CurrentBookInfo(
                            history.getId(),
                            history.getBook().getTitle()
                    ),
                    false,
                    user.isActive()
            );
        }

        return new UserInClassroomResponse(
                user.getId(), user.getName(), user.getUsername(), user.getLastLogin(),
                null, true, user.isActive()
        );
    }

    private ClassroomResponse classroomToDto(Classroom classroom) {
        return new ClassroomResponse(
                classroom.getId(),
                classroom.getName(),
                classroom.getPublicCode()
        );
    }
}