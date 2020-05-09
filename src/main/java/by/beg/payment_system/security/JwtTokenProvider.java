package by.beg.payment_system.security;

import by.beg.payment_system.model.user.UserRole;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;


@Component
public class JwtTokenProvider {

    @Value("${jwt.token.secret}")
    private String secretWord;

    @Value("${jwt.token.expired}")
    private long timeOfActionInMs;

    @Value("${jwt.token.prefix}")
    private String prefix;

    private UserDetailsService userDetailsService;

    @Autowired
    public JwtTokenProvider(@Qualifier("jwtUserDetailService") UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }


    @PostConstruct
    protected void init() {
        secretWord = Base64.getEncoder().encodeToString(secretWord.getBytes());
    }

    public String createToken(String userEmail, UserRole role) {

        Claims claims = Jwts.claims().setSubject(userEmail);

        if (role.equals(UserRole.ADMIN)) {
            claims.put("roles", List.of(UserRole.ADMIN, UserRole.USER));
        } else {
            claims.put("roles", List.of(UserRole.USER));
        }

        Date now = new Date();
        Date periodOfValidity = new Date(now.getTime() + timeOfActionInMs);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(periodOfValidity)
                .signWith(SignatureAlgorithm.HS256, secretWord)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        String userEmail = Jwts.parser().setSigningKey(secretWord).parseClaimsJws(token).getBody().getSubject();
        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }


    public String resolveToken(HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        if (token != null && token.startsWith(prefix)) {
            return token.substring(prefix.length());
        }
        return null;
    }

    public boolean validateToken(String token) {

        try {

            Jws<Claims> claims = Jwts.parser().setSigningKey(secretWord).parseClaimsJws(token);

            if (claims.getBody().getExpiration().before(new Date())) {
                return false;
            }
            return true;

        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

}
