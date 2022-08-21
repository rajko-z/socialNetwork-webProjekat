package controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import dto.NewUserDTO;
import exceptions.BadRequestException;
import model.Image;
import repository.RepoFactory;
import services.ImageService;
import services.UserService;
import spark.Request;
import spark.Response;
import util.LocalDateDeserializer;

import java.time.LocalDate;

public class UserController {

    private static final UserService userService = new UserService(RepoFactory.userRepo);


    private static Gson buildGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
        return gsonBuilder.setPrettyPrinting().create();
    }

    // POST api/users
    public static Object registerNewUser(Request req, Response res) {
        String payload = req.body();
        NewUserDTO u;
        Gson gson = buildGson();
        try {
            u = gson.fromJson(payload, NewUserDTO.class);
            if (u == null) {
                res.status(400);
                return "Bad user format request";
            }
        } catch (BadRequestException badRequestException) {
            res.status(400);
            return badRequestException.getMessage();
        } catch (Exception e) {
            res.status(400);
            return "Bad user request format";
        }

        try {
            userService.registerNewUser(u);
        } catch (BadRequestException e) {
            res.status(400);
            return e.getMessage();
        }
        return "Successfully registered";
    }
}
