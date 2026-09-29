package com.elearning.media;

import com.elearning.auth.dto.LoginRequest;
import com.elearning.users.entity.User;
import com.elearning.users.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MediaStreamingIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    private String studentToken;

    @BeforeEach
    void setUp() throws Exception {
        studentToken = loginOrCreate("student1@example.com", "student123", User.Role.STUDENT);
    }

    private String loginOrCreate(String email, String password, User.Role role) throws Exception {
        if (userRepository.findByEmail(email).isEmpty()) {
            User user = new User();
            user.setName(email.split("@")[0]);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            user.setRole(role);
            user.setEmailVerified(true);
            userRepository.save(user);
        }

        MvcResult result = mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new LoginRequest(email, password))))
            .andExpect(status().isOk())
            .andReturn();

        String response = result.getResponse().getContentAsString();
        return objectMapper.readTree(response).get("token").asText();
    }

    @Test
    void streamToken_anonymous_returns401() throws Exception {
        mockMvc.perform(get("/api/v1/lessons/1001/stream-token"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void streamToken_authenticated() throws Exception {
        mockMvc.perform(get("/api/v1/lessons/1001/stream-token")
                .header("Authorization", "Bearer " + studentToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void stream_anonymous_returns401() throws Exception {
        mockMvc.perform(get("/api/v1/media/stream")
                .param("token", "some-token")
                .param("lessonId", "1001"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void stream_invalidToken_returns403() throws Exception {
        mockMvc.perform(get("/api/v1/media/stream")
                .header("Authorization", "Bearer " + studentToken)
                .param("token", "invalid-token")
                .param("lessonId", "1001"))
            .andExpect(status().isForbidden());
    }

    @Test
    void stream_forgedToken_returns403() throws Exception {
        mockMvc.perform(get("/api/v1/media/stream")
                .header("Authorization", "Bearer " + studentToken)
                .param("token", "forged.token.here")
                .param("lessonId", "1001"))
            .andExpect(status().isForbidden());
    }

    @Test
    void stream_rangeRequest_returns206() throws Exception {
        // First get a valid token
        MvcResult tokenResult = mockMvc.perform(get("/api/v1/lessons/1001/stream-token")
                .header("Authorization", "Bearer " + studentToken))
            .andExpect(status().isOk())
            .andReturn();

        String token = objectMapper.readTree(tokenResult.getResponse().getContentAsString()).get("token").asText();

        // Test range request
        mockMvc.perform(get("/api/v1/media/stream")
                .header("Authorization", "Bearer " + studentToken)
                .param("token", token)
                .param("lessonId", "1001")
                .header("Range", "bytes=0-1023"))
            .andExpect(status().isPartialContent())
            .andExpect(header().string("Accept-Ranges", "bytes"))
            .andExpect(header().string("Content-Range", org.hamcrest.Matchers.startsWith("bytes 0-1023/")));
    }

    @Test
    void stream_fullRequest_returns200() throws Exception {
        MvcResult tokenResult = mockMvc.perform(get("/api/v1/lessons/1001/stream-token")
                .header("Authorization", "Bearer " + studentToken))
            .andExpect(status().isOk())
            .andReturn();

        String token = objectMapper.readTree(tokenResult.getResponse().getContentAsString()).get("token").asText();

        mockMvc.perform(get("/api/v1/media/stream")
                .header("Authorization", "Bearer " + studentToken)
                .param("token", token)
                .param("lessonId", "1001"))
            .andExpect(status().isOk())
            .andExpect(header().string("Accept-Ranges", "bytes"));
    }
}
