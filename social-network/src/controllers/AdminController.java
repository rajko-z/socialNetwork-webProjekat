package controllers;

import com.google.gson.Gson;
import dto.user.UserDTO;
import model.Role;
import model.User;
import repository.RepoFactory;
import services.UserService;
import spark.Request;
import spark.Response;
import util.DTOConverter;
import util.JWTUtils;
import util.gson.GsonUtil;

import java.util.List;
import java.util.stream.Collectors;

import static spark.Spark.halt;

public class AdminController {
    private static final UserService userService = new UserService(RepoFactory.userRepo);


    //vraca listu svih regula
    public static Object getAllUsers(Request request, Response response) {
        User currentUser = JWTUtils.getUserIfLoggedIn(request);
        if (currentUser == null || !currentUser.getRole().equals(Role.ADMIN)) {
            halt(401, "Unauthorized");
        }
        Gson gson = GsonUtil.createGsonWithDateSupport();
        List<UserDTO> all = RepoFactory.userRepo.getAll().stream().filter(u -> u.getRole().equals(Role.REGULAR)).map(DTOConverter::convertUserToDto).collect(Collectors.toList());
        return gson.toJson(all);
    }





}
