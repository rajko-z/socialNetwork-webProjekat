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
import java.time.ZonedDateTime;
import java.util.Date;

public class JWTUtils {

    static Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final static String SECRET_KEY = "3an9rucdFLJ9iQzRBstM2VKuTBe1d0FDSFDSFDSFDSFu6DNG3PGU9iGw=4FDSFDSfdfdsfSDFASDFDSFDSFDSFDSFDSFDSREGFGFDRT%$";
    private final static UserService userService = new UserService(RepoFactory.userRepo);

    public static String createJWT(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(Date.from(ZonedDateTime.now().plusHours(5).toInstant()))
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public static User getUserIfLoggedIn(Request req) {
        String auth = req.headers("Authorization");
        if (auth == null || !auth.contains("Bearer "))
            return null;

        String jwt = auth.substring(auth.indexOf("Bearer ") + 7);
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(jwt);
            String username = claims.getBody().getSubject();
            return userService.getUserByUsername(username);
        } catch (Exception e) {
            return null;
        }
    }

    public static User getUserIfLoggedInAsAdmin(Request req) {
        String auth = req.headers("Authorization");
        if (auth == null || !auth.contains("Bearer "))
            return null;

        String jwt = auth.substring(auth.indexOf("Bearer ") + 7);
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(jwt);
            String username = claims.getBody().getSubject();
            User user = userService.getUserByUsername(username);
            if (user != null && user.getRole().equals(Role.ADMIN))
                return userService.getUserByUsername(username);
            return null;
        } catch (Exception e) {
            return null;
        }
    }

}
