package controllers;

import com.google.gson.Gson;
import dto.DirectMessageDTO;
import exceptions.BadRequestException;
import model.DirectMessage;
import model.User;
import repository.RepoFactory;
import services.DirectMessageService;
import services.UserService;
import spark.Request;
import spark.Response;
import util.DTOConverter;
import util.JWTUtils;
import util.gson.GsonUtil;

import static spark.Spark.halt;

public class DirectMessageController {

    private static final UserService userService = new UserService(RepoFactory.userRepo);

    private static final DirectMessageService directMessageService= new DirectMessageService();



    public static Object sendDirectMessage(Request req, Response res) {
        User currentUser = JWTUtils.getUserIfLoggedIn(req);
        if (currentUser == null) {
            halt(401, "Unauthorized");
        }
        User sendTo = userService.getUserByUsername(req.params("username"));
        if (sendTo == null) {
            res.status(400);
            return "Bad Request. User with this username does not exist";
        }
        String text = req.params("text");
        if (text.trim().equals("")) {     // ne moze poslati praznu poruku ili samo space-ove (ovo vrv nece biti ovako kasnije)
            res.status(400);
            return "Bad Request. Message is empty.";
        }
        try {

            DirectMessage created  = directMessageService.sendDirectMessage(currentUser,sendTo,text);

            DirectMessageDTO converted = DirectMessageDTO.builder()
                    .from(DTOConverter.convertUserToDto(currentUser))
                    .to(DTOConverter.convertUserToDto(sendTo))
                    .createdAt(created.getCreatedAt())
                    .text(created.getText())
                    .adminSent(created.isAdminSent())
                    .build();
            Gson gson = GsonUtil.createGsonWithDateSupport();
            return gson.toJson(converted);
        }
        catch (BadRequestException e) {
            res.status(400);
            return e.getMessage();
        }
    }




}
