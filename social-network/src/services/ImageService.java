package services;


import model.Image;
import repository.ImageRepository;
import java.util.List;

public class ImageService {

    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public Image getById(Long id) {
        return imageRepository.getById(id);
    }

    public List<Image> getAll() {
        return imageRepository.getAll();
    }

	
}
