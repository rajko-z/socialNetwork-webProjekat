package controllers;

import com.google.gson.Gson;
import dto.CommentDTO;
import dto.NewCommentDTO;
import dto.NewPostDTO;
import dto.UpdatedCommentDTO;
import exceptions.BadRequestException;
import exceptions.ForbiddenAccessException;
import exceptions.InternalAppException;
import model.Comment;
import model.Post;
import model.User;
import repository.RepoFactory;
import services.CommentService;
import services.PostService;
import spark.Request;
import spark.Response;
import util.DTOConverter;
import util.JWTUtils;
import util.gson.GsonUtil;

import static spark.Spark.halt;

public class CommentController {

    private static final CommentService commentService = new CommentService();

    // POST /comments
    public static Object createComment(Request req, Response res) {
        User user = JWTUtils.getUserIfLoggedIn(req);
        if (user == null) {
            halt(401, "Unauthorized");
        }
        Gson gson = GsonUtil.createGsonWithDateSupport();
        String payload = req.body();
        NewCommentDTO newCommentDTO;
        try {
            newCommentDTO = gson.fromJson(payload, NewCommentDTO.class);
            if (newCommentDTO == null) {
                res.status(400);
                return "Bad new comment request";
            }
        } catch (Exception e) {
            res.status(400);
            return "Bad new comment request";
        }

        try {
            Comment created = commentService.create(newCommentDTO, user);
            return gson.toJson(CommentDTO.builder()
                    .id(created.getId())
                    .createdAt(created.getCreatedAt())
                    .text(created.getText())
                    .isDeleted(false)
                    .user(DTOConverter.convertUserToDto(created.getUser()))
                    .build());
        } catch (BadRequestException e) {
            res.status(400);
            return e.getMessage();
        } catch (ForbiddenAccessException e) {
            res.status(403);
            return e.getMessage();
        } catch (Exception e) {
            res.status(500);
            return "Error happened on server";
        }
    }

    // PUT /comments
    public static Object updateComment(Request req, Response res) {
        User user = JWTUtils.getUserIfLoggedIn(req);
        if (user == null) {
            halt(401, "Unauthorized");
        }
        Gson gson = GsonUtil.createGsonWithDateSupport();
        String payload = req.body();
        UpdatedCommentDTO updatedCommentDTO;
        try {
            updatedCommentDTO = gson.fromJson(payload, UpdatedCommentDTO.class);
            if (updatedCommentDTO == null) {
                res.status(400);
                return "Bad updated comment request";
            }
        } catch (Exception e) {
            res.status(400);
            return "Bad updated comment request";
        }

        try {
            Comment updated = commentService.update(updatedCommentDTO, user);
            return gson.toJson(CommentDTO.builder()
                    .id(updated.getId())
                    .createdAt(updated.getCreatedAt())
                    .text(updated.getText())
                    .isDeleted(false)
                    .modifiedAt(updated.getModifiedAt())
                    .user(DTOConverter.convertUserToDto(updated.getUser()))
                    .build());
        } catch (BadRequestException e) {
            res.status(400);
            return e.getMessage();
        } catch (ForbiddenAccessException e) {
            res.status(403);
            return e.getMessage();
        }
    }

    public static Object deleteComment(Request req, Response res) {
        User user = JWTUtils.getUserIfLoggedIn(req);
        if (user == null) {
            halt(401, "Unauthorized");
        }
        long commentId;
        try {
            commentId = Long.parseLong(req.params("id"));
        } catch (NumberFormatException e) {
            res.status(400);
            return "Bad request. Comment id should be number";
        }

        try {
            commentService.deleteCommentById(commentId, user);
            return "Successfully deleted comment";
        } catch (BadRequestException e) {
            res.status(400);
            return e.getMessage();
        } catch (ForbiddenAccessException e) {
            res.status(403);
            return e.getMessage();
        }
    }
}
