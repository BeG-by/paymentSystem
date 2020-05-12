package by.beg.payment_system.security.jwt;

import lombok.SneakyThrows;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {


    @SneakyThrows
    @Override
    public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException e) {
        res.setContentType("application/json");
        res.setStatus(res.SC_UNAUTHORIZED);

        String message = "User is not authenticated";

        res.getWriter().write(new JSONObject()
                .put("status", HttpStatus.UNAUTHORIZED)
                .put("message", message)
                .toString());
    }

}
