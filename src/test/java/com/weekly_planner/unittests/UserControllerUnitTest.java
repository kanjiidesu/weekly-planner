package com.weeklyPlanner.unittests;

import com.weeklyPlanner.config.JwtTokenUtil;
import com.weeklyPlanner.controller.UserController;
import com.weeklyPlanner.controller.UserController.JwtResponse;
import com.weeklyPlanner.exception.ResourceNotFoundException;
import com.weeklyPlanner.model.LoginRequest;
import com.weeklyPlanner.model.User;
import com.weeklyPlanner.repository.DayRepository;
import com.weeklyPlanner.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerUnitTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DayRepository dayRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetails userDetails;

    // This will run before each test, to set up the mocks
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Testcase that will verify that createUser works as it should (sunshine scenario)
    @Test
    void testCreateUser() {
        //create the neccessary user info
        User inputUser = new User();
        inputUser.setUsername("john");
        inputUser.setPassword("pass");

        User savedUser = new User();
        savedUser.setUsername("john");

        // mocking the encoding of the password
        when(passwordEncoder.encode("pass")).thenReturn("hashedPass");
        // Mock the save method of the user repository
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // call the real method to create the user
        User result = userController.createUser(inputUser);

        // verify  that the user was saved with the right username
        assertEquals("john", result.getUsername());

        // verify that the dayrepository was called 7 times because it will create the 7 days for the user
        verify(dayRepository, times(7)).save(any());  // 7 days created
    }

    @Test
    void testLoginSuccess() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("john");
        loginRequest.setPassword("pass");

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("john");

        when(jwtTokenUtil.generateToken(authentication)).thenReturn("jwt-token");

        User user = new User();
        user.setUsername("john");
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));

        ResponseEntity<?> response = userController.login(loginRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof JwtResponse);
        assertEquals("jwt-token", ((JwtResponse) response.getBody()).getToken());
    }

    @Test
    void testGetUserByIdNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            userController.getUserById(1L);
        });

        assertTrue(exception.getMessage().contains("User either could not be found"));
    }
}
