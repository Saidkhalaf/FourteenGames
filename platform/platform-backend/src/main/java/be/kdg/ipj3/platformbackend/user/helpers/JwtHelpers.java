package be.kdg.ipj3.platformbackend.user.helpers;

import org.springframework.security.oauth2.jwt.Jwt;

public class JwtHelpers {
    public static String userNameFromToken(Jwt token) {
        return token.getClaimAsString("preferred_username");
    }
}
