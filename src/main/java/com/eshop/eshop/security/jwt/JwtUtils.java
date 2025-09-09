package com.eshop.eshop.security.jwt;

import com.eshop.eshop.security.user.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.io.Decoders;
import org.springframework.stereotype.Component;

import java.security.Key;

import java.util.Date;
import java.util.List;

@Component
public class JwtUtils {

    @Value("${auth.token.expirationInMils}")
    private int expirationTime;
    @Value("${auth.token.jwtSecret}")
    private String jwtSecret;

    public String generateToken(Authentication authentication){
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userPrincipal.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority).toList();
        return Jwts.builder()
                .setSubject(userPrincipal.getEmail())
                .claim("id", userPrincipal.getId())
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime()+expirationTime))
                        .signWith(key(), SignatureAlgorithm.HS256).compact();
    }

    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUsernameFromToken(String token){
        return Jwts.parser().
                setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    public boolean validateToken(String token){
        try {
            Jwts.parser()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException
                | io.jsonwebtoken.security.SecurityException | IllegalArgumentException e) {
            throw new JwtException(e.getMessage());
        }
    }



}
