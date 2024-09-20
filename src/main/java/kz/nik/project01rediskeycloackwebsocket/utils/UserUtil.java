package kz.nik.project01rediskeycloackwebsocket.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@Slf4j
public final class UserUtil {
    public static Jwt getCurrentUserJwt() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken) {
            return ((JwtAuthenticationToken) authentication).getToken();
        }
        log.warn("Incorrect token");
        return null;
    }

    public static String getCurrentUserName() {
        Jwt jwt = getCurrentUserJwt();
        if (jwt != null) {
            return jwt.getClaimAsString("preferred_username");
        }
        log.warn("Incorrect user");
        return null;
    }
}
