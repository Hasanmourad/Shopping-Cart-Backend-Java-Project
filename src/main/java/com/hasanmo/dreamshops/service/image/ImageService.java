package com.hasanmo.dreamshops.service.image;

import com.hasanmo.dreamshops.dto.ImageDto;
import com.hasanmo.dreamshops.exceptions.ResourceNotFoundExeption;
import com.hasanmo.dreamshops.model.Image;
import com.hasanmo.dreamshops.model.Product;
import com.hasanmo.dreamshops.repository.ImageRepository;
import com.hasanmo.dreamshops.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService{

    private final ImageRepository imageRepository;
    private final IProductService productService;

    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id).
                orElseThrow(()-> new ResourceNotFoundExeption("image not fuond"));
    }

    @Override
    public void deleteImageById(Long id) {
         imageRepository.findById(id)
                 .ifPresentOrElse(imageRepository::delete,
                         () -> {throw new ResourceNotFoundExeption("image not found");});
    }

    @Override
    public List<ImageDto> saveImages(List<MultipartFile> files, Long productId) {
        Product product = productService.getProductById(productId);
        List<ImageDto> savedImageDto = new ArrayList<>();
        for (MultipartFile file : files){
            try {
                Image image = new Image();
                image.setImageName(file.getOriginalFilename());
                image.setImageType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);
                String buildDownloadUrl = "/api/v1/images/image/download/";
                String downloadUrl = buildDownloadUrl+image.getId();
                image.setDownloadUrl(downloadUrl);
                Image savedImage = imageRepository.save(image);

                savedImage.setDownloadUrl(buildDownloadUrl+savedImage.getId());
                imageRepository.save(savedImage);

                ImageDto imageDto = new ImageDto();
                imageDto.setId(savedImage.getId());
                imageDto.setImageName(savedImage.getImageName());
                imageDto.setDownloadUrl(savedImage.getDownloadUrl());
                savedImageDto.add(imageDto);

            } catch (IOException | SQLException e) {
                throw new RuntimeException(e.getMessage());
            }

        }
        return savedImageDto;

    }

    @Override
    public void updateImage(MultipartFile file, Long imageId) {
        try {
            Image image = getImageById(imageId);
            image.setImageName(file.getOriginalFilename());
            image.setImageType(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(image);
        } catch (SQLException  | IOException e) {
            throw new RuntimeException(e.getMessage());
        }

    }
}
