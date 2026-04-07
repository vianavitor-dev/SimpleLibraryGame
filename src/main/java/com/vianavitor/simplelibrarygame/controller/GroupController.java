package com.vianavitor.simplelibrarygame.controller;

import com.vianavitor.simplelibrarygame.dto.ApiResponse;
import com.vianavitor.simplelibrarygame.dto.request.ChangeNameRequest;
import com.vianavitor.simplelibrarygame.dto.request.CreateGroupRequest;
import com.vianavitor.simplelibrarygame.model.Group;
import com.vianavitor.simplelibrarygame.service.GroupService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @PostMapping
    public ResponseEntity<ApiResponse<Group>> createGroup(
            @Valid @RequestBody CreateGroupRequest request,
            HttpServletRequest req
    ) {
        Group group = groupService.create(request.name(), request.studentId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(group, "Group created", req.getRequestURI()));
    }

    @PatchMapping("/{id}/name")
    public ResponseEntity<ApiResponse<Group>> changeName(
            @PathVariable Long id,
            @Valid @RequestBody ChangeNameRequest request,
            HttpServletRequest req
    ) {
        Group group = groupService.changeName(id, request.name());
        return ResponseEntity.ok(ApiResponse.success(group, "Name updated", req.getRequestURI()));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<List<Group>>> getByStudent(@PathVariable Long studentId, HttpServletRequest request) {
        List<Group> groups = groupService.getByStudent(studentId);
        ApiResponse<List<Group>> response = ApiResponse.success(groups, request.getRequestURI());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteGroup(@PathVariable Long id, HttpServletRequest request) {
        groupService.delete(id);
        ApiResponse<Void> response = ApiResponse.success(null, "Group deleted", request.getRequestURI());
        return ResponseEntity.ok(response);
    }
}