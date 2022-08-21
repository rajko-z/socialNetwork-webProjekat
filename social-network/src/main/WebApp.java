package main;


import controllers.AuthController;
import controllers.ImageController;
import controllers.PostController;
import controllers.UserController;
import repository.RepoFactory;
import spark.route.RouteOverview;

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
            post("/posts", PostController::addPost);
        });

    }
}
