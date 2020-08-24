package com.markovic.todoApplication.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.markovic.todoApplication.constant.SecurityConstant;
import com.markovic.todoApplication.domain.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.markovic.todoApplication.constant.SecurityConstant.*;
import static java.util.Arrays.stream;

// Could use another one for different specification porpuses depending on the needs of our app
// Could be done the same way in the same page in another Class
@Component
public class JWTTokenProvider {

    // TODO: 7/26/2020 Should be Secret, as a property file in the server and get it from there for more security
    @Value("${jwt.secret}")
    private String secret;

    // We are using the secret which we get from the application.properties.
    // - also use the SecurityConstant class where we have defined a lot of needed settings for JWT spring security.
    // - are getting claims -> authorities that the user has
    // - are using the JWT dependency from OAuth to create a new token.
    // - are setting as the Issuer (the company that wants to gen a token) to be us.
    // - are setting the administration.
    // - are generating a date to set it as time created.
    // - are setting for whom (unique) the token will be used.
    // - are the user's authorities (that we claimed)
    // - are setting the expiration date of the token (now plus 5 days)
    // - are signing the token with HMAC512 Encryption algorithm using the secret.
    public String generateJwtToken(User user){
        String[] claims = getClaimsFromUser(user);
        return JWT.create()
                .withIssuer(SecurityConstant.TODO_LLC)
                .withAudience(SecurityConstant.TODO_ADMINISTRATION)
                .withIssuedAt(new Date())
                .withSubject(user.getUsername())
                .withArrayClaim(AUTHORITIES, claims)
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstant.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(secret.getBytes()));
    }

    // Getting Authorities from the token and return them as GrandedAuthorities
    public List<GrantedAuthority> getAuthorities(String token){
        String[] claims = getClaimsFromToken(token);
        return stream(claims).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    // Authenticating ***
    public Authentication getAuthentication(String username, List<GrantedAuthority> authorities, HttpServletRequest request){
        UsernamePasswordAuthenticationToken usernamePasswordAuthToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
        usernamePasswordAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return usernamePasswordAuthToken;
    }

    // Checking if the token is valid, not expired and username isn't null or empty or with spaces
    public boolean isTokenValid(String username, String token){
        JWTVerifier verifier = getJWTVerifier();
        return StringUtils.isNotEmpty(username) && !isTokenExpired(verifier, token);
    }

    // Getting the Subject (i think user) from the token
    public String getSubject(String token){
        JWTVerifier verifier = getJWTVerifier();
        return verifier.verify(token).getSubject();
    }

    // Checking if the token isn't expired
    private boolean isTokenExpired(JWTVerifier verifier, String token) {
        Date expiration = verifier.verify(token).getExpiresAt();
        return expiration.before(new Date());
    }

    // Verifying the token and getting claims (authorities)
    private String[] getClaimsFromToken(String token) {
        JWTVerifier verifier = getJWTVerifier();
        return verifier.verify(token).getClaim(AUTHORITIES).asArray(String.class);
    }

    // Verifying the token is legit having correct credentials
    private JWTVerifier getJWTVerifier() {
        JWTVerifier verifier;
        try {
            Algorithm algorithm = Algorithm.HMAC512(secret);
            verifier = JWT.require(algorithm).withIssuer(TODO_LLC).build();
        }catch (JWTVerificationException e){
            throw new JWTVerificationException(TOKEN_CANNOT_BE_VERIFIED);
        }
        return verifier;
    }

    // Getting authorities from the user
    private String[] getClaimsFromUser(User user) {
        List<String> authorities = new ArrayList<>();
        for (GrantedAuthority grantedAuthority:
             user.getAuthorities()) {
            authorities.add(grantedAuthority.getAuthority());
        }
        return authorities.toArray(new String[0]);
    }

}
