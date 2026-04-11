package com.vianavitor.simplelibrarygame.controller;

import com.vianavitor.simplelibrarygame.dto.ApiResponse;
import com.vianavitor.simplelibrarygame.dto.request.ChangeNameRequest;
import com.vianavitor.simplelibrarygame.dto.request.ModifyUsersInClassroomRequest;
import com.vianavitor.simplelibrarygame.model.Classroom;
import com.vianavitor.simplelibrarygame.model.utils.classes.UserClassroom;
import com.vianavitor.simplelibrarygame.service.ClassroomService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
    public ResponseEntity<ApiResponse<List<Classroom>>> getAll(
            HttpServletRequest req
    ) {
        List<Classroom> result = classroomService.getAllClasses();
        return ResponseEntity.ok(ApiResponse.success(result, "Result of searching all classrooms", req.getRequestURI()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Classroom>> getById(
            @PathVariable Long id,
            HttpServletRequest req
    ) {
        Classroom result = classroomService.getClassroom(id);
        return ResponseEntity.ok(ApiResponse.success(result, "Result of searching by ID", req.getRequestURI()));
    }

    @PutMapping("/{id}/users")
    public ResponseEntity<ApiResponse<Set<UserClassroom>>> modifyUsers(
            @PathVariable Long id,
            @Valid @RequestBody ModifyUsersInClassroomRequest request,
            HttpServletRequest req
    ) {
        Set<UserClassroom> updated = classroomService.modifyUsersInClassroom(id, request.users());
        return ResponseEntity.ok(ApiResponse.success(updated, "Users updated", req.getRequestURI()));
    }

    @PatchMapping("/{id}/name")
    public ResponseEntity<ApiResponse<Classroom>> changeName(
            @PathVariable Long id,
            @Valid @RequestBody ChangeNameRequest request,
            HttpServletRequest req
    ) {
        Classroom classroom = classroomService.changeName(id, request.name());
        return ResponseEntity.ok(ApiResponse.success(classroom, "Name changed", req.getRequestURI()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteClassroom(@PathVariable Long id, HttpServletRequest request) {
        classroomService.delete(id);
        ApiResponse<Void> response = ApiResponse.success(null, "Classroom deleted", request.getRequestURI());
        return ResponseEntity.ok(response);
    }
}