package controllers;

import com.google.gson.Gson;
import dto.SearchUsersAdminDto;
import dto.user.UserDTO;
import exceptions.BadRequestException;
import exceptions.ForbiddenAccessException;
import model.DirectMessage;
import model.Role;
import model.User;
import repository.RepoFactory;
import services.DirectMessageService;
import services.PostService;
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

    private static final PostService postService = new PostService(RepoFactory.postRepo);
    private static final DirectMessageService directMessageService = new DirectMessageService(RepoFactory.directMessageRepository);
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


    public static Object getFriendsOfUser(Request req, Response res) {
        User currentUser = JWTUtils.getUserIfLoggedIn(req);
        if (currentUser == null || !currentUser.getRole().equals(Role.ADMIN)) {
            halt(401, "Unauthorized");
        }
        String username = req.params("username");
        User u = userService.getUserByUsername(username);

        Gson gson = GsonUtil.createGsonWithDateSupport();
        List<UserDTO> retVal = u.getFriends().stream()
                .map(DTOConverter::convertUserToDto)
                .collect(Collectors.toList());

        return gson.toJson(retVal);
    }


    public static Object deletePostAdmin(Request req, Response res) {
        User user = JWTUtils.getUserIfLoggedIn(req);
        if (user == null) {
            halt(401, "Unauthorized");
        }
        long postId;
        String txt;
        try {
            postId = Long.parseLong(req.params("id"));
            txt = req.params("text");
        } catch (NumberFormatException e) {
            res.status(400);
            return "Bad request. Post id should be number.";
        }

        try {
            String text = "Vas post je obrisan od strane admina: "+txt;
            DirectMessage dm =directMessageService.sendDirectMessage(user,postService.getUserFromPost(postId),text);
            postService.deletePostById(postId, user);
            return "Successfully deleted post.";
        } catch (BadRequestException e) {
            res.status(400);
            return e.getMessage();
        } catch (ForbiddenAccessException e) {
            res.status(403);
            return e.getMessage();
        }
    }




}
