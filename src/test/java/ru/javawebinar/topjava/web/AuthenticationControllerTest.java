package ru.javawebinar.topjava.web;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.util.TokenUtil;

import static org.junit.Assert.assertEquals;
import static ru.javawebinar.topjava.UserTestData.ADMIN;
import static ru.javawebinar.topjava.UserTestData.USER;

public class AuthenticationControllerTest extends AbstractRestControllerTest {

    @Autowired
    private TokenUtil tokenUtil;

    @Test
    public void adminLoginTest() throws Exception {
        AuthenticationResponse authResponse = login(new AuthenticationRequest(ADMIN.getEmail(), ADMIN.getPassword()));
        assertEquals(ADMIN.getEmail(), tokenUtil.getUsernameFromToken(authResponse.getToken()));
    }

    @Test
    public void userLoginTest() throws Exception {
        AuthenticationResponse authResponse = login(new AuthenticationRequest(USER.getEmail(), USER.getPassword()));
        assertEquals(USER.getEmail(), tokenUtil.getUsernameFromToken(authResponse.getToken()));
    }
}
