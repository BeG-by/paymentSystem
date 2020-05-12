package by.beg.payment_system.security.jwt;

import lombok.SneakyThrows;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @SneakyThrows
    @Override
    public void handle(HttpServletRequest req, HttpServletResponse res, AccessDeniedException e) {
        res.setContentType("application/json");
        res.setStatus(HttpServletResponse.SC_FORBIDDEN);

        String message = "User's role is not admin. Access denied";

        res.getWriter().write(new JSONObject()
                .put("status", HttpStatus.FORBIDDEN)
                .put("message", message)
                .toString());

    }

}
