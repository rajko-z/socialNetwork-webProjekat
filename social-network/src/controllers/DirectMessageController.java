package controllers;

import com.google.gson.Gson;
import dto.message.DirectMessageDTO;
import dto.message.NewMessageDTO;
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

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static spark.Spark.halt;

public class DirectMessageController {

    private static final UserService userService = new UserService(RepoFactory.userRepo);

    private static final DirectMessageService directMessageService= new DirectMessageService();



    public static Object sendDirectMessage(Request req, Response res) {
        User currentUser = JWTUtils.getUserIfLoggedIn(req);
        if (currentUser == null) {
            halt(401, "Unauthorized");
        }
        Gson gson = GsonUtil.createGsonWithDateSupport();
        String payload = req.body();
        NewMessageDTO newMessageDTO;

        try {
            newMessageDTO = gson.fromJson(payload, NewMessageDTO.class);
            if (newMessageDTO == null) {
                res.status(400);
                return "Bad message request";
            }
        } catch (Exception e) {
            res.status(400);
            return "Bad message request";
        }

        User sendTo = userService.getUserByUsername(newMessageDTO.getUsername());
        if (sendTo == null) {
            res.status(400);
            return "Bad Request. User with this username does not exist";
        }
        String text = newMessageDTO.getText();
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

            return gson.toJson(converted);
        }
        catch (BadRequestException e) {
            res.status(400);
            return e.getMessage();
        }
    }



    public static Object getAllMessages(Request req, Response res) {
        User currentUser = JWTUtils.getUserIfLoggedIn(req);

        String username = req.params("username");
        User user = userService.getUserByUsername(username);
        if (user == null) {
            res.status(404);
            return "User with username: " + username + " not found.";
        }


        String usernameTo = req.params("usernameTo");
        User userTo = userService.getUserByUsername(usernameTo);
        if (userTo == null) {
            res.status(404);
            return "User with username: " + usernameTo + " not found.";
        }


        List<DirectMessage> chat = directMessageService.getChat(user,userTo);

        List<DirectMessageDTO> retChat =
                chat.stream().map(p->
                        DirectMessageDTO.builder()
                                .from(DTOConverter.convertUserToDto(p.getFrom()))
                                .to(DTOConverter.convertUserToDto(p.getTo()))
                                .adminSent(p.isAdminSent())
                                .createdAt(p.getCreatedAt())
                                .text(p.getText()).build()).sorted(Comparator.comparing(DirectMessageDTO::getCreatedAt)).collect(Collectors.toList());



        Gson gson = GsonUtil.createGsonWithDateSupport();
        return gson.toJson(retChat);
    }





}
