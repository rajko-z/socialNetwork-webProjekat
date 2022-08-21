package controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.NewPostDTO;
import exceptions.BadRequestException;
import exceptions.InternalAppException;
import model.User;
import repository.RepoFactory;
import services.PostService;
import spark.Request;
import spark.Response;
import util.JWTUtils;
import util.gson.AttributeExclusionStrategy;
import util.gson.GsonUtil;

import static spark.Spark.halt;

public class PostController  {

    private static final PostService postService = new PostService(RepoFactory.postRepo);

    // POST /api/posts
    public static Object addPost(Request req, Response res) {
        User user = JWTUtils.getUserIfLoggedIn(req);
        if (user == null) {
            halt(401, "Unauthorized");
        }
        Gson gson = GsonUtil.createGsonWithDateSupportAndExclusionStrategy("comments");
        String payload = req.body();
        NewPostDTO newPostDTO;
        try {
            newPostDTO = gson.fromJson(payload, NewPostDTO.class);
            if (newPostDTO == null) {
                res.status(400);
                return "Bad post request";
            }
        } catch (Exception e) {
            res.status(400);
            return "Bad post request";
        }

        try {
            return gson.toJson(postService.addPost(newPostDTO, user));
        } catch (BadRequestException e) {
            res.status(400);
            return e.getMessage();
        } catch (InternalAppException e) {
            res.status(500);
            return e.getMessage();
        } catch (Exception e) {
            res.status(500);
            return "Error happened on server";
        }

    }
}
