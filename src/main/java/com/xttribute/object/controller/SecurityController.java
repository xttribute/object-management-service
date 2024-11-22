package com.xttribute.object.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;

public class SecurityController {
	private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public static String generateToken(String string) {
        return Jwts.builder()
                .setSubject(string)
                .setIssuedAt(new Date())
                .signWith(SECRET_KEY)
                .compact();
    }
}