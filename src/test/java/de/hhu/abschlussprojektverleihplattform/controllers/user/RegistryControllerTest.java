package de.hhu.abschlussprojektverleihplattform.controllers.user;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc

public class RegistryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    WebApplicationContext context;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(springSecurity())
            .build();
    }

    @Test
    public void testcontrolleristhere() throws Exception {
        mockMvc.perform(get("/register"))
            .andExpect(status().isOk())
            .andExpect(content()
                .string(containsString("Registrierung")))
            .andExpect(content()
                .string(containsString("username")))
            .andExpect(content()
                .string(containsString("password")));
    }

    @Test
    public void test404onwrongparameters() throws Exception {
        mockMvc
            .perform(post("/register"))
            .andExpect(status()
                .is4xxClientError());
    }

    @Test
    public void testPostValidUser() throws Exception {
        mockMvc
            .perform(post("/register")
                .param("firstname", "Vorname")
                .param("lastname", "Name")
                .param("email", "mail@test.com")
                .param("username", "testValid")
                .param("password", "password")
                .with(csrf()))
            .andExpect(redirectedUrl("/profile"))
            .andExpect(status().isFound());
    }

    @Test
    public void testPostWrongEmail() throws Exception {
        mockMvc
            .perform(post("/register")
                .param("firstname", "Vorname")
                .param("lastname", "Name")
                .param("email", "mail")
                .param("username", "username")
                .param("password", "password")
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("E-Mail ist nicht korrekt.")));
    }

    @Test
    public void redirectToProfileWhenAlreadyLoggedIn() throws Exception {
        mockMvc
            .perform(get("/register").with(user("user")))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/profile"));
    }

}
