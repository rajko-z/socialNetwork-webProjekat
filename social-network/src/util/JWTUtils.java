package util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import model.Role;
import model.User;
import repository.RepoFactory;
import services.UserService;
import spark.Request;
import spark.Response;

import java.security.Key;
import java.util.Date;

public class JWTUtils {

    static Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final static UserService userService = new UserService(RepoFactory.userRepo);

    public static String createJWT(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(new Date().getTime() + 1000*60L*60 * 5)) // valid for 5 hours
                .setIssuedAt(new Date())
                .signWith(key)
                .compact();
    }

    public static boolean userLoggedIn(Request req) {
        String auth = req.headers("Authorization");
        if (auth == null || !auth.contains("Bearer "))
            return false;

        String jwt = auth.substring(auth.indexOf("Bearer ") + 7);
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt);
            String username = claims.getBody().getSubject();
            return userService.userAlreadyExist(username);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean userLoggedInAsAdmin(Request req) {
        String auth = req.headers("Authorization");
        if (auth == null || !auth.contains("Bearer "))
            return false;

        String jwt = auth.substring(auth.indexOf("Bearer ") + 7);
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt);
            String username = claims.getBody().getSubject();
            User user = userService.getUserByUsername(username);
            return user != null && user.getRole().equals(Role.ADMIN);
        } catch (Exception e) {
            return false;
        }
    }

}
