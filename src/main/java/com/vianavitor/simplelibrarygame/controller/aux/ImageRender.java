package com.vianavitor.simplelibrarygame.controller.aux;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;

@RestController
@RequestMapping("/api/image-render")
public class ImageRender {
    @GetMapping(value = "/path")
    @ResponseBody
//    TODO: make it works to external images
    public ResponseEntity<InputStreamResource> render(@RequestParam String path, HttpServletRequest req) {
        String extension = path.split("[.]")[1];
        MediaType type = extension.equals("png") ? MediaType.IMAGE_PNG : MediaType.IMAGE_JPEG;

        InputStream in = getClass().getResourceAsStream("/static/images/3.png");

        if (in == null) {
            throw new NullPointerException("InputStream (in) cannot be null");
        }

        return ResponseEntity.ok()
                .contentType(type)
                .body(new InputStreamResource(in));
    }
}
