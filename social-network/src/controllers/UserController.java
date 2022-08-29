package controllers;

import com.google.gson.Gson;
import dto.*;
import exceptions.BadRequestException;
import model.FriendStatus;
import model.Post;
import model.PostType;
import model.User;
import repository.RepoFactory;
import services.FriendStatusService;
import services.PostService;
import services.UserService;
import spark.Request;
import spark.Response;
import util.DTOConverter;
import util.JWTUtils;
import util.gson.GsonUtil;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static spark.Spark.halt;


public class UserController {

    private static final UserService userService = new UserService(RepoFactory.userRepo);
    private static final FriendStatusService friendStatusService = new FriendStatusService();
    private static final PostService postService = new PostService(RepoFactory.postRepo);

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

    // GET /users/:username
    public static Object getUserByUsername(Request req, Response res) {
        String username = req.params("username");
        User user = userService.getUserByUsername(username);
        if (user == null) {
            res.status(404);
            return "User with username: " + username + " not found.";
        }
        Gson gson = GsonUtil.createGsonWithDateSupport();
        return gson.toJson(DTOConverter.convertUserToDto(user));
    }


    // GET /users/getPostsWithComments/:username?postType=all|image|regular
    public static Object getPostsWithCommentsForUsername(Request req, Response res) {
        User currentUser = JWTUtils.getUserIfLoggedIn(req);

        String username = req.params("username");
        User user = userService.getUserByUsername(username);
        if (user == null) {
            res.status(404);
            return "User with username: " + username + " not found.";
        }

        if (user.isAccountPrivate()) {
            if (currentUser == null) {
                halt(401, "Unauthorized");
            } else {
                FriendStatus friendStatus = friendStatusService.getFriendStatusBetweenUsers(currentUser, user);
                if (!friendStatus.equals(FriendStatus.FRIENDS)) {
                    res.status(403);
                    return "Forbidden. You have to be friend with " + username + " to see users posts";
                }
            }
        }

        String typeParam = req.queryParams("postType");
        if (typeParam == null)
            typeParam = "all";
        List<Post> posts = filterPostsForUserByTypeParam(user, typeParam);

        List<PostDTOWithoutUser> retVal =
                posts.stream().map( p ->
                        PostDTOWithoutUser.builder()
                            .imageUrl(postService.getImageUrlForPost(p))
                            .createdAt(p.getCreatedAt())
                            .text(p.getText())
                            .type(p.getType())
                            .comments(DTOConverter.convertListOfCommentsToDTOs(p.getUndeletedComments())
                                    .stream()
                                    .sorted(Comparator.comparing(CommentDTO::getCreatedAt))
                                    .collect(Collectors.toList()))
                            .build()
                    )
                    .sorted(Comparator.comparing(PostDTOWithoutUser::getCreatedAt))
                    .collect(Collectors.toList());

        Gson gson = GsonUtil.createGsonWithDateSupport();
        return gson.toJson(retVal);
    }

    private static List<Post> filterPostsForUserByTypeParam(User user, String postType) {
        List<Post> retVal = user.getUndeletedPosts();
        if (postType.equals("image"))
            return userService.getPostsFromUserByPostType(user, PostType.IMAGE_POST);
        if (postType.equals("regular"))
            return userService.getPostsFromUserByPostType(user, PostType.REGULAR_POST);
        return retVal;
    }
}
