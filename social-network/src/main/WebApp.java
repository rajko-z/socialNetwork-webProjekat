package main;


import controllers.*;
import repository.RepoFactory;

import java.io.File;
import java.io.IOException;

import static spark.Spark.*;

public class WebApp {

    public static void main(String[] args) throws IOException {

        RepoFactory.loadData();

        port(8081);
        staticFiles.externalLocation(new File("./static").getCanonicalPath());
        notFound((req, res) -> "{ \"error\": \"404 Not Found\"}" );
        internalServerError((req, res) -> "{ \"error\": \"500 Internal Server Error\"}" );

        path("/api", () -> {

            options("/*", (req, res) -> {
                String headers = req.headers("Access-Control-Request-Headers");
                if (headers != null)
                    res.header("Access-Control-Allow-Headers", headers);

                String methods = req.headers("Access-Control-Request-Method");
                if (methods != null)
                    res.header("Access-Control-Allow-Methods", methods);

                res.header("Access-Control-Allow-Origin", "*");
                return "OK";
            });

            before("/*", (req, res) -> {
                res.type("application/json");
                res.header("Access-Control-Allow-Origin", "*");
            });

            after("/*", (req, res) -> {
            });


            post("/auth", AuthController::login);
            get("/images", ImageController::getAll);
            get("/images/:id", ImageController::getById);
            post("/users", UserController::registerNewUser);
            put("/users", UserController::updateUser);
            put("/users/changePassword", UserController::changePassword);

            // users/getPostsWithComments/:username?postType={all|regular|image}
            get("/users/getPostsWithComments/:username", UserController::getPostsWithCommentsForUsername);

            get("/users/getFeed", UserController::getFeed);
            get("/users/:username", UserController::getUserByUsername);
            post("/posts", PostController::addPost);
            get("/friendStatus/getFriends", FriendStatusController::getFriends);
            get("/friendStatus/getCommonFriends/:username", FriendStatusController::getCommonFriends);
            get("/friendStatus/getAllFriendRequests", FriendStatusController::getAllFriendRequest);
            get("/friendStatus/getAllPendingFriendRequestsThatISent", FriendStatusController::getAllFriendRequestThatUserSent);
            get("/friendStatus/:username", FriendStatusController::getFriendStatusForUsername);
            post("/friendStatus/sendRequestTo/:username", FriendStatusController::sendFriendRequest);
            delete("/friendStatus/removeFriend/:username", FriendStatusController::removeFriend);
            put("/friendStatus/acceptRequest/:username", FriendStatusController::acceptRequest);
            post("/sendMessageTo/:username/:text", DirectMessageController::sendDirectMessage);
            post("/sendMessageTo", DirectMessageController::sendDirectMessage);
            post("/comments", CommentController::createComment);
            put("/comments", CommentController::updateComment);
            delete("/comments/:id",CommentController::deleteComment);
            delete("posts/:id",PostController::deletePost);
            delete("/posts/:id",PostController::deletePost);

        });

    }
}
