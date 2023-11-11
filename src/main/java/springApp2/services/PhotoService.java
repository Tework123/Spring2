package springApp2.services;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import springApp2.models.Photo;
import springApp2.models.Post;
import springApp2.models.User;
import springApp2.repositories.PhotoRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;

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

    public void deletePhotos(User currentUser, Post post) {
        try {
            List<Photo> photos = post.getPhotos();
            for (Photo photo : photos) {
                File file = new File("src/main/resources/static/photos/"
                        + currentUser.getEmail() + "/" + post.getId() + "/" + photo.getName());
                File file2 = new File("target/classes/static/photos/"
                        + currentUser.getEmail() + "/" + post.getId() + "/" + photo.getName());
                file.delete();
                file2.delete();
            }
            File folder = new File("src/main/resources/static/photos/"
                    + currentUser.getEmail() + "/" + post.getId());
            File folder2 = new File("target/classes/static/photos/"
                    + currentUser.getEmail() + "/" + post.getId());
            folder.delete();
            folder2.delete();

        } catch (Exception e) {
            System.out.println("Failed to Delete image !!");
        }
    }
}



