package com.vianavitor.simplelibrarygame.controller.aux;

import com.vianavitor.simplelibrarygame.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/image-render")
public class ImageRender {
    @GetMapping(value = "/path")
    public ResponseEntity<Resource> render(@RequestParam String path, HttpServletRequest req) throws MalformedURLException {
        String extension = path.split("[.]")[1];
        MediaType type = extension.equals("png") ? MediaType.IMAGE_PNG : MediaType.IMAGE_JPEG;

        Resource resource = new UrlResource(Path.of(path).toUri());

        if (!resource.exists() || !resource.isReadable()) {
            throw new ResourceNotFoundException("image is not readable or does not exists");
        }

        return ResponseEntity.ok()
                .contentType(type)
                .body(resource);
    }
}
