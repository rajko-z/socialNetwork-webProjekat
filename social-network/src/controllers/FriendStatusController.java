package controllers;

import com.google.gson.Gson;
import dto.FriendRequestDTO;
import dto.UserDTO;
import exceptions.BadRequestException;
import exceptions.InternalAppException;
import model.FriendRequest;
import model.FriendStatus;
import model.User;
import repository.RepoFactory;
import services.FriendStatusService;
import services.UserService;
import spark.Request;
import spark.Response;
import util.JWTUtils;
import util.gson.GsonUtil;

import static spark.Spark.halt;

public class FriendStatusController {

    private static final FriendStatusService friendStatusService = new FriendStatusService();
    private static final UserService userService = new UserService(RepoFactory.userRepo);

    // GET /friendStatus/:username
    public static Object getFriendStatusForUsername(Request req, Response res) {
        User currentUser = JWTUtils.getUserIfLoggedIn(req);
        if (currentUser == null) {
            halt(401, "Unauthorized");
        }
        User compareUser = userService.getUserByUsername(req.params("username"));
        if (compareUser == null) {
            res.status(400);
            return "Bad Request. User with this username does not exist";
        }
        FriendStatus friendStatus = friendStatusService.getFriendStatusBetweenUsers(currentUser, compareUser);
        return friendStatus.toString();
    }

    // POST /friendStatus/sendRequestTo/:username
    public static Object sendFriendRequest(Request req, Response res) {
        User currentUser = JWTUtils.getUserIfLoggedIn(req);
        if (currentUser == null) {
            halt(401, "Unauthorized");
        }
        User sendTo = userService.getUserByUsername(req.params("username"));
        if (sendTo == null) {
            res.status(400);
            return "Bad Request. User with this username does not exist";
        }
        try {
            FriendRequest created =  friendStatusService.sendFriendRequest(currentUser, sendTo);
            FriendRequestDTO converted = FriendRequestDTO.builder()
                    .from(convertUserToDto(currentUser))
                    .to(convertUserToDto(sendTo))
                    .createdAt(created.getCreatedAt())
                    .build();
            Gson gson = GsonUtil.createGsonWithDateSupport();
            return gson.toJson(converted);
        }
        catch (BadRequestException e) {
            res.status(400);
            return e.getMessage();
        }
    }

    private static UserDTO convertUserToDto(User user) {
        return UserDTO.builder()
                .username(user.getUsername())
                .name(user.getName())
                .surname(user.getUsername())
                .accountPrivate(user.isAccountPrivate())
                .dateOfBirth(user.getDateOfBirth())
                .gender(user.getGender())
                .isBlocked(user.isBlocked())
                .role(user.getRole())
                .build();
    }


    // DELETE  /friendStatus/removeFriend/:username
    public static Object removeFriend(Request req, Response res) {
        User currentUser = JWTUtils.getUserIfLoggedIn(req);
        if (currentUser == null) {
            halt(401, "Unauthorized");
        }
        User toRemove = userService.getUserByUsername(req.params("username"));
        if (toRemove == null) {
            res.status(400);
            return "Bad Request. User with this username does not exist";
        }

        try {
            friendStatusService.removeFriend(currentUser, toRemove);
            return "You successfully removed " + toRemove.getUsername() + " from friends.";
        } catch (BadRequestException e) {
            res.status(400);
            return e.getMessage();
        }

    }

    // PUT /friendStatus/acceptRequest/:username
    public static Object acceptRequest(Request req, Response res) {
        User currentUser = JWTUtils.getUserIfLoggedIn(req);
        if (currentUser == null) {
            halt(401, "Unauthorized");
        }
        User toAccept = userService.getUserByUsername(req.params("username"));
        if (toAccept == null) {
            res.status(400);
            return "Bad Request. User with this username does not exist";
        }

        try {
            friendStatusService.acceptFriendRequest(toAccept, currentUser);
            return "You are now friend with " + toAccept.getUsername();
        } catch (BadRequestException e) {
            res.status(400);
            return e.getMessage();
        } catch (InternalAppException e) {
            res.status(500);
            return e.getMessage();
        }
    }

}
