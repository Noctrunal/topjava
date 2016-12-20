package ru.javawebinar.topjava.web;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import ru.javawebinar.topjava.TestUtil;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.web.json.JsonUtil;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public abstract class AbstractRestControllerTest extends AbstractControllerTest {
    private static final String REST_AUTH_URL = "/rest/authentication";
    private static final String TOKEN_TYPE = "Bearer";

    protected AuthenticationResponse login(AuthenticationRequest authRequest) throws Exception {
        ResultActions actions = mockMvc.perform(post(REST_AUTH_URL + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(authRequest)))
                .andDo(print())
                .andExpect(status().isOk());
        return JsonUtil.readValue(TestUtil.getContent(actions), AuthenticationResponse.class);
    }

    protected String token(User user) throws Exception {
        AuthenticationResponse authResponse = login(new AuthenticationRequest(user.getEmail(), user.getPassword()));
        return TOKEN_TYPE + " " + authResponse.getToken();
    }
}
