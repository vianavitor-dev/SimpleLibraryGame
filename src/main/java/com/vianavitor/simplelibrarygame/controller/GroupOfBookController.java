package com.vianavitor.simplelibrarygame.controller;

import com.vianavitor.simplelibrarygame.dto.ApiResponse;
import com.vianavitor.simplelibrarygame.service.GroupOfBookService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/group-books")
public class GroupOfBookController {

    @Autowired
    private GroupOfBookService groupOfBookService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Void>> addBookToGroup(@RequestParam Long groupId,
                                                            @RequestParam Long bookId,
                                                            HttpServletRequest request) {
        groupOfBookService.addBookToGroup(groupId, bookId);
        ApiResponse<Void> response = ApiResponse.success(null, "Book added to group", request.getRequestURI());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<ApiResponse<Void>> removeBookFromGroup(@RequestParam Long groupId,
                                                                 @RequestParam Long bookId,
                                                                 HttpServletRequest request) {
        groupOfBookService.removeBookFromGroup(groupId, bookId);
        ApiResponse<Void> response = ApiResponse.success(null, "Book removed from group", request.getRequestURI());
        return ResponseEntity.ok(response);
    }
}