package dean.project.Dride.services.cloud_business;

import org.springframework.web.multipart.MultipartFile;

public interface CloudService {
    String upload(MultipartFile image);
    String urlUpload(String filePath);
}
