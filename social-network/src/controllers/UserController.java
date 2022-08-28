package controllers;

import com.google.gson.Gson;
import dto.ChangePasswordRequestDTO;
import dto.NewUserDTO;
import dto.UpdatedUserDTO;
import exceptions.BadRequestException;
import model.User;
import repository.RepoFactory;
import services.UserService;
import spark.Request;
import spark.Response;
import util.DTOConverter;
import util.JWTUtils;
import util.gson.GsonUtil;

import static spark.Spark.halt;


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

    // PUT /users
    public static Object updateUser(Request req, Response res) {
        User currentUser = JWTUtils.getUserIfLoggedIn(req);
        if (currentUser == null) {
            halt(401, "Unauthorized");
        }

        String payload = req.body();
        UpdatedUserDTO u;
        Gson gson = GsonUtil.createGsonWithDateSupport();
        try {
            u = gson.fromJson(payload, UpdatedUserDTO.class);
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

        if (!currentUser.getUsername().equals(u.getUsername())) {
            res.status(400);
            return "Can't change info from other user";
        }

        try {
            User updated = userService.updateUser(u);
            return gson.toJson(DTOConverter.convertUserToDto(updated));
        } catch (BadRequestException e) {
            res.status(400);
            return e.getMessage();
        }

    }

    // PUT /users/changePassword
    public static Object changePassword(Request req, Response res) {
        User currentUser = JWTUtils.getUserIfLoggedIn(req);
        if (currentUser == null) {
            halt(401, "Unauthorized");
        }

        String payload = req.body();
        ChangePasswordRequestDTO c;
        Gson gson = GsonUtil.createGsonWithDateSupport();
        try {
            c = gson.fromJson(payload, ChangePasswordRequestDTO.class);
            if (c == null) {
                res.status(400);
                return "Bad new password request";
            }
        } catch (Exception e) {
            res.status(400);
            return "Bad new password request format";
        }

        if (!currentUser.getUsername().equals(c.getUsername())) {
            res.status(400);
            return "Can't change password from other user";
        }

        try {
            userService.updatePassword(c);
            return "Successfully changed password";
        } catch (BadRequestException e) {
            res.status(400);
            return e.getMessage();
        }

    }
}
