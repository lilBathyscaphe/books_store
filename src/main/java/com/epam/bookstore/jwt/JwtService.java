package com.epam.bookstore.jwt;

import com.epam.bookstore.configuration.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtService {

    private static final String AUTHORITY_KEY = "authority";
    private static final String AUTHORITIES_CLAIM_STATEMENT = "authorities";


    private final JwtConfig jwtConfig;

    @Autowired
    public JwtService(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }


    public String generateToken(Authentication authentication) {
        return Jwts.builder()
                .setSubject(authentication.getName())                                   //body of JWT Token
                .claim(AUTHORITIES_CLAIM_STATEMENT, authentication.getAuthorities())
                .setIssuedAt(new Date())                                            //creation date
                .setExpiration(
                        java.sql.Date.valueOf(LocalDate.now().plusDays(jwtConfig.getTokenExpirationAfterDays()))) //expiration date
                .signWith(Keys.hmacShaKeyFor(jwtConfig.getSecretKey().getBytes()))
                .compact();                                                         //make token to called "JWS". Later token can be access by "JWS"

    }

    public Claims getClaims(String token) {
        Jws<Claims> jwsClaims = Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(jwtConfig.getSecretKey().getBytes()))
                .parseClaimsJws(token);   //JWS because of use of compress() method
        return jwsClaims.getBody();
    }

    @NotNull
    public Authentication getAuthentication(String token) {
        Claims body = getClaims(token);
        String login = body.getSubject();

        List<Map<String, String>> authorities = (List<Map<String, String>>) body.get(AUTHORITIES_CLAIM_STATEMENT);
        Set<SimpleGrantedAuthority> authoritySet = authorities.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.get(AUTHORITY_KEY)))
                .collect(Collectors.toSet());

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                login,
                null,
                authoritySet
        );
        log.debug("User Authentication: {}", authentication);
        return authentication;
    }

}
