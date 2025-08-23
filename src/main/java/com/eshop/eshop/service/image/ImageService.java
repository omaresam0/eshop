package com.eshop.eshop.service.image;

import com.eshop.eshop.dto.ImageDto;
import com.eshop.eshop.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {
    Image getImageById(Long id);
    void deleteImageById(Long id);
    List<ImageDto> saveImages(List<MultipartFile> images, Long productId);
    void updateImage(MultipartFile image, Long id);
}
