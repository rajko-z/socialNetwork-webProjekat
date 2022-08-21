package controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.LoginRequestDTO;
import dto.UserWithJWTResponse;
import model.User;
import repository.RepoFactory;
import services.UserService;
import spark.Request;
import spark.Response;
import util.JWTUtils;
import util.gson.GsonUtil;

public class AuthController {

    private static final UserService userService = new UserService(RepoFactory.userRepo);

    public static Object login(Request req, Response res) {
        Gson gson = GsonUtil.createGsonWithDateSupport();
        String payload = req.body();
        LoginRequestDTO loginRequestDTO;
        try {
            loginRequestDTO = gson.fromJson(payload, LoginRequestDTO.class);
            if (loginRequestDTO == null) {
                res.status(400);
                return "Bad login request format";
            }
        } catch (Exception e) {
            res.status(400);
            return "Bad login request format";
        }

        User user = userService.getUserByUsernameAndPassword(loginRequestDTO.getUsername(),
                loginRequestDTO.getPassword());
        if (user == null || user.isBlocked()) {
            res.status(401);
            return "Bad credentials, cant' find user";
        }

        return gson.toJson(
                UserWithJWTResponse.builder()
                    .username(user.getUsername())
                    .name(user.getName())
                    .surname(user.getUsername())
                    .dateOfBirth(user.getDateOfBirth())
                    .gender(user.getGender())
                    .isAccountPrivate(user.isAccountPrivate())
                    .role(user.getRole())
                    .jwt(JWTUtils.createJWT(user.getUsername()))
                    .build());
    }
}
