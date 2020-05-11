//package by.beg.payment_system.security;
//
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.web.access.AccessDeniedHandler;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//public class CustomAccessDeniedHandler implements AccessDeniedHandler {
//
//    @Override
//    public void handle(HttpServletRequest req, HttpServletResponse res, AccessDeniedException e) throws IOException {
//        res.setContentType("application/json;charset=UTF-8");
//        res.setStatus(403);
//        res.getWriter().write("Token is invalid or expired");
//    }
//}
