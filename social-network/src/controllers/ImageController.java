package controllers;

import com.google.gson.Gson;
import model.Image;
import java.util.List;

import repository.RepoFactory;
import services.ImageService;
import spark.*;

public class ImageController {

    private static final ImageService imageService = new ImageService(RepoFactory.imageRepo);
    private static final Gson gson = new Gson();

    // proba primera controllera

    // GET api/images
    public static Object getAll(Request request, Response response) {
        List<Image> images = imageService.getAll();
        return gson.toJson(images);
    }

    // GET api/images/:id
    public static Object getById(Request request, Response response) {
        Long id = Long.parseLong(request.params("id"));
        Image image = imageService.getById(id);
        if (image == null) {
            response.redirect("/error", 404);
            return "";
        }
        return gson.toJson(image);
    }
}
