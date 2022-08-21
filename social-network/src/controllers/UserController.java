package controllers;

import com.google.gson.Gson;
import dto.NewUserDTO;
import exceptions.BadRequestException;
import repository.RepoFactory;
import services.UserService;
import spark.Request;
import spark.Response;
import util.gson.GsonUtil;


public class UserController {

    private static final UserService userService = new UserService(RepoFactory.userRepo);

    // POST api/users
    public static Object registerNewUser(Request req, Response res) {
        String payload = req.body();
        NewUserDTO u;
        Gson gson = GsonUtil.createGsonWithDateSupport();
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
