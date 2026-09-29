package com.elearning.common.security;

import com.elearning.auth.dto.LoginRequest;
import com.elearning.auth.dto.SignupRequest;
import com.elearning.auth.service.AuthService;
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
class RbacIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    private String studentToken;
    private String instructorToken;
    private String adminToken;

    @BeforeEach
    void setUp() throws Exception {
        studentToken = loginOrCreate("student1@example.com", "student123", User.Role.STUDENT);
        instructorToken = loginOrCreate("instructor1@example.com", "instructor123", User.Role.INSTRUCTOR);
        adminToken = loginOrCreate("admin@example.com", "admin123", User.Role.ADMIN);
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

    // ============ AUTH ENDPOINTS ============

    @Test
    void login_success() throws Exception {
        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new LoginRequest("student1@example.com", "student123"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").exists())
            .andExpect(jsonPath("$.role").value("STUDENT"));
    }

    @Test
    void login_invalidCredentials_returns401() throws Exception {
        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new LoginRequest("student1@example.com", "wrongpassword"))))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void signup_createsStudent() throws Exception {
        mockMvc.perform(post("/api/v1/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new SignupRequest("New Student", "newstudent@example.com", "password123"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.role").value("STUDENT"));
    }

    // ============ COURSE ENDPOINTS ============

    @Test
    void courses_list_public() throws Exception {
        mockMvc.perform(get("/api/v1/courses"))
            .andExpect(status().isOk());
    }

    @Test
    void courses_create_anonymous_returns401() throws Exception {
        mockMvc.perform(post("/api/v1/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void courses_create_student_returns403() throws Exception {
        mockMvc.perform(post("/api/v1/courses")
                .header("Authorization", "Bearer " + studentToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Test Course\",\"description\":\"Test\",\"category\":\"Programming\",\"price\":999,\"level\":\"BEGINNER\"}"))
            .andExpect(status().isForbidden());
    }

    @Test
    void courses_create_instructor_success() throws Exception {
        mockMvc.perform(post("/api/v1/courses")
                .header("Authorization", "Bearer " + instructorToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Test Course\",\"description\":\"Test\",\"category\":\"Programming\",\"price\":999,\"level\":\"BEGINNER\"}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.title").value("Test Course"))
            .andExpect(jsonPath("$.status").value("DRAFT"));
    }

    // ============ USER ENDPOINTS ============

    @Test
    void users_list_anonymous_returns401() throws Exception {
        mockMvc.perform(get("/api/v1/users"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void users_list_student_returns403() throws Exception {
        mockMvc.perform(get("/api/v1/users")
                .header("Authorization", "Bearer " + studentToken))
            .andExpect(status().isForbidden());
    }

    @Test
    void users_list_admin_success() throws Exception {
        mockMvc.perform(get("/api/v1/users")
                .header("Authorization", "Bearer " + adminToken))
            .andExpect(status().isOk());
    }

    @Test
    void users_me_authenticated() throws Exception {
        mockMvc.perform(get("/api/v1/users/me")
                .header("Authorization", "Bearer " + studentToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("student1@example.com"));
    }

    // ============ ENROLLMENT ENDPOINTS ============

    @Test
    void enrollments_my_anonymous_returns401() throws Exception {
        mockMvc.perform(get("/api/v1/enrollments/my"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void enrollments_my_student_success() throws Exception {
        mockMvc.perform(get("/api/v1/enrollments/my")
                .header("Authorization", "Bearer " + studentToken))
            .andExpect(status().isOk());
    }

    @Test
    void enrollments_my_instructor_returns403() throws Exception {
        mockMvc.perform(get("/api/v1/enrollments/my")
                .header("Authorization", "Bearer " + instructorToken))
            .andExpect(status().isForbidden());
    }

    // ============ ADMIN ENDPOINTS ============

    @Test
    void admin_users_anonymous_returns401() throws Exception {
        mockMvc.perform(get("/api/v1/admin/users"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void admin_users_student_returns403() throws Exception {
        mockMvc.perform(get("/api/v1/admin/users")
                .header("Authorization", "Bearer " + studentToken))
            .andExpect(status().isForbidden());
    }

    @Test
    void admin_users_instructor_returns403() throws Exception {
        mockMvc.perform(get("/api/v1/admin/users")
                .header("Authorization", "Bearer " + instructorToken))
            .andExpect(status().isForbidden());
    }

    // ============ HEALTH ENDPOINT ============

    @Test
    void health_public() throws Exception {
        mockMvc.perform(get("/api/v1/health"))
            .andExpect(status().isOk());
    }
}
