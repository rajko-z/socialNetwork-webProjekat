package controllers;

import model.FriendStatus;
import model.User;
import repository.RepoFactory;
import services.FriendStatusService;
import services.UserService;
import spark.Request;
import spark.Response;
import util.JWTUtils;

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
}
