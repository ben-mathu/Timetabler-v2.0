package com.bernard.timetabler.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author bernard
 */
public class JwtTokenUtilTest {

    private JwtTokenUtil jwtTokenUtil;
    private String token;

    @Before
    public void setUp() throws Exception {
        jwtTokenUtil = new JwtTokenUtil();
    }

    @Test
    public void generateToken_ThrowNullPointerException_NullArgument() {

    }

    @Test
    public void verifyToken() {
    }
}