package springApp2.services;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import springApp2.models.Photo;
import springApp2.models.User;
import springApp2.repositories.PhotoRepository;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PhotoService {
    private final PhotoRepository photoRepository;

    public Photo toImageEntity(MultipartFile file, User currentUser) throws IOException {
        Photo photo = new Photo();
        String fileName1 = StringUtils.cleanPath(file.getOriginalFilename());
        photo.setName(fileName1);
        photo.setUser(currentUser);
        photoRepository.save(photo);
        return photo;

    }
}
