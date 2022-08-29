package controllers;

import com.google.gson.Gson;
import dto.FriendRequestDTO;
import dto.user.UserDTO;
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
import util.DTOConverter;
import util.JWTUtils;
import util.gson.GsonUtil;

import java.util.List;
import java.util.stream.Collectors;

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
                    .from(DTOConverter.convertUserToDto(currentUser))
                    .to(DTOConverter.convertUserToDto(sendTo))
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

    // GET /friendStatus/getAllFriendRequests
    public static Object getAllFriendRequest(Request req, Response res) {
        User currentUser = JWTUtils.getUserIfLoggedIn(req);
        if (currentUser == null) {
            halt(401, "Unauthorized");
        }

        List<FriendRequest> friendRequestList = friendStatusService.getFriendRequestsForUser(currentUser);
        List<FriendRequestDTO> retVal = friendRequestList.stream().map(r ->
                        FriendRequestDTO.builder()
                            .from(DTOConverter.convertUserToDto(r.getFrom()))
                            .createdAt(r.getCreatedAt())
                            .build())
                        .collect(Collectors.toList());
        Gson gson = GsonUtil.createGsonWithDateSupportAndExclusionStrategy("to");
        return gson.toJson(retVal);
    }

    // GET /friendStatus/getAllPendingFriendRequestsThatISent
    public static Object getAllFriendRequestThatUserSent(Request req, Response res) {
        User currentUser = JWTUtils.getUserIfLoggedIn(req);
        if (currentUser == null) {
            halt(401, "Unauthorized");
        }

        List<FriendRequest> friendRequestList = friendStatusService.getFriendRequestsThatUserSent(currentUser);
        List<FriendRequestDTO> retVal = friendRequestList.stream().map(r ->
                FriendRequestDTO.builder()
                        .to(DTOConverter.convertUserToDto(r.getTo()))
                        .createdAt(r.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
        Gson gson = GsonUtil.createGsonWithDateSupportAndExclusionStrategy("from");
        return gson.toJson(retVal);
    }

    public static Object getCommonFriends(Request req, Response res) {
        User currentUser = JWTUtils.getUserIfLoggedIn(req);
        if (currentUser == null) {
            halt(401, "Unauthorized");
        }

        User user = userService.getUserByUsername(req.params("username"));
        if (user == null) {
            res.status(400);
            return "Bad Request. User with username: " + req.params("username") + " does not exist";
        }

        List<User> commonFriends = friendStatusService.getCommonFriendsForUsers(currentUser, user);
        List<UserDTO> retVal = commonFriends.stream().
                map(f -> DTOConverter.convertUserToDto(f)).collect(Collectors.toList());

        Gson gson = GsonUtil.createGsonWithDateSupport();
        return gson.toJson(retVal);
    }

    public static Object getFriends(Request req, Response res) {
        User currentUser = JWTUtils.getUserIfLoggedIn(req);
        if (currentUser == null) {
            halt(401, "Unauthorized");
        }

        Gson gson = GsonUtil.createGsonWithDateSupport();
        List<UserDTO> retVal = currentUser.getFriends().stream()
                .map(DTOConverter::convertUserToDto)
                .collect(Collectors.toList());

        return gson.toJson(retVal);
    }
}
