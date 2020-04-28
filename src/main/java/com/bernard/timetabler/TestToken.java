package com.bernard.timetabler;

import com.bernard.timetabler.dbinit.Constants;
import com.miiguar.tokengeneration.JwtTokenUtil;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author bernard
 */
public class TestToken {

    public static void main(String[] args) {
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();

        Date date = new Date();
        long millis = date.getTime();
        millis = millis + TimeUnit.HOURS.toMillis(6);
        System.out.println(new Date(millis));
        String token = jwtTokenUtil.generateToken(Constants.ISSUER, "login", new Date(millis));

        System.out.println("Token: " + token);
    }
}
