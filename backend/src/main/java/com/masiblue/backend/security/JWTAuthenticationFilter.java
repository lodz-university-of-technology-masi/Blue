package com.masiblue.backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.masiblue.backend.model.UserAccount;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.stream.Collectors;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import com.auth0.jwt.JWT;
import static com.masiblue.backend.security.SecurityConstants.*;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            UserAccount credentials = new ObjectMapper().readValue(request.getInputStream(), UserAccount.class);
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword()));
        } catch (IOException ex) {
            //TODO: Handle this error in more gentle way
            throw new RuntimeException();
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String authorities = authResult.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(""));

        String token = JWT.create()
                .withSubject(((User) authResult.getPrincipal()).getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .withClaim(AUTHORITIES_KEY, authorities)
                .sign(HMAC512(SECRET.getBytes()));
        String authority = authorities.substring(authorities.indexOf("_") + 1);

        response.getWriter().print(" {\n\"" +
                 HEADER_STRING + "\": \"" + TOKEN_PREFIX + token + "\",\n\"" +
                 AUTHORITIES_KEY + "\": \"" + authority + "\"\n" +
                "}");

        response.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
        response.addHeader(AUTHORITIES_KEY, authority);
    }
}
