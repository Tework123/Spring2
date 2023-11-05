package springApp2.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import springApp2.models.Photo;
import springApp2.repositories.PhotoRepository;

import java.io.ByteArrayInputStream;

@RestController
@RequiredArgsConstructor
public class PhotoController {
    private final PhotoRepository photoRepository;

    @GetMapping("/photosUrl/{id}")
    private ResponseEntity<?> getPhotoById(@PathVariable Integer id) {
        Photo photo = photoRepository.findById(id).orElse(null);
        return ResponseEntity.ok().header("fileName", photo.getOriginalFileName())
                .contentType(MediaType.valueOf(photo.getContentType()))
                .contentLength(photo.getSize())
                .body(new InputStreamResource(new ByteArrayInputStream(photo.getBytes())));

    }
}
