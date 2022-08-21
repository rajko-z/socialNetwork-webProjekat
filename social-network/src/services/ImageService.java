package services;


import exceptions.BadRequestException;
import model.Image;
import repository.ImageRepository;
import util.Constants;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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


    public Image createImageFromBase64String(String base64Image, String imageName) {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
        String newImageName = timeStamp + imageName;

        if (tryConvertBase64ToImageAndSave(newImageName, base64Image)) {
            Image newImage = new Image(newImageName);
            this.imageRepository.add(newImage);
            return newImage;
        }
        return null;
    }


    private boolean tryConvertBase64ToImageAndSave(String imageName, String base64) {
        try{
            String path = "./static/images/" + imageName;
            if (!isValidBase64Format(base64))
                return false;

            byte[] image = Base64.getDecoder().decode(base64);
            OutputStream out = new FileOutputStream(path);
            out.write(image);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isValidBase64Format(String base64) {
        String pattern = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(base64);
        return m.find();
    }
}
