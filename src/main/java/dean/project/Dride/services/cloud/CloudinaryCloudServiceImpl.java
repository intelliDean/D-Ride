package dean.project.Dride.services.cloud;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import dean.project.Dride.exceptions.ImageUploadException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

import static dean.project.Dride.utilities.Constants.URL;

@Service
@AllArgsConstructor
public class CloudinaryCloudServiceImpl implements CloudService {

    private final Cloudinary cloudinary;

    @Override
    public String upload(MultipartFile image) {
        try {
            Map<?, ?> response =
                    cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
            return response.get(URL).toString();
        } catch (IOException ex) {
            throw new ImageUploadException(ex.getMessage());
        }
    }
}
