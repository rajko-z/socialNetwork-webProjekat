package controllers;

import com.google.gson.Gson;
import dto.SearchUsersAdminDto;
import dto.user.UserDTO;
import exceptions.BadRequestException;
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



    //get  /searchUsers
    public static Object searchUsers(Request request, Response response) {
        String payload = request.body();
        SearchUsersAdminDto srcDTO;
        Gson gson = GsonUtil.createGsonWithDateSupport();

        try {
            srcDTO = gson.fromJson(payload, SearchUsersAdminDto.class);
            if (srcDTO == null) {
                response.status(400);
                return "Bad search format request";
            }
        } catch (BadRequestException badRequestException) {
            response.status(400);
            return badRequestException.getMessage();
        } catch (Exception e) {
            response.status(400);
            return "Bad search request format";
        }


        List<UserDTO> all = RepoFactory.userRepo.getAllUsersSearchAdmin(srcDTO).stream().map(DTOConverter::convertUserToDto).collect(Collectors.toList());
        return gson.toJson(all);
    }




}
