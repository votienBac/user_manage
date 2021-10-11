package com.example.testintern.service;

import com.example.testintern.dto.AuthenticationResponse;
import com.example.testintern.dto.LoginRequest;
import com.example.testintern.dto.RegisterRequest;
import com.example.testintern.model.User;
import com.example.testintern.repo.UserRepository;
import com.example.testintern.security.JwtProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletContext;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    @Autowired
    ServletContext application;
    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private ImageService imageService;

    @Transactional
    public void save(RegisterRequest registerRequest) {
        User user = new User();
        String address = registerRequest.getZipCode()+", "+registerRequest.getAddressDetail();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(encodePassword(registerRequest.getPassword()));
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPhoneNumber((registerRequest.getPhoneNumber()));
        user.setAddress(address);

        //user.setEmojiLink(registerRequest.getEmoji());

            File file = fileUploadService.upload(registerRequest.getEmojiFile());
            String resizeResult = imageService.resizeImage(file);
            user.setEmojiLink(resizeResult);
            //file.delete();

        userRepository.save(user);

    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);//check user login or not login
        String token = jwtProvider.generateToken(authentication);
        return  AuthenticationResponse.builder()
                .authenticationToken(token)
                .username(loginRequest.getUsername())
                .build();
    }

    public boolean userExists(String email){
        return userRepository.findByEmail(email).isPresent();
    }
    @Transactional
    public List<User> showUserList(){
        return userRepository.findAll();
    }



    public User findUserById(Long id) {
       User user = userRepository.findById(id)
               .orElseThrow(()-> new IllegalStateException("User with id "+id+" not found!"));
       return user;
    }
    @Transactional
    public void update(RegisterRequest registerRequest, Long id) {
        User user = findUserById(id);
        String address = registerRequest.getZipCode()+", "+registerRequest.getAddressDetail();
        user.setUsername(registerRequest.getUsername());
        //user.setPassword(encodePassword(registerRequest.getPassword()));
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPhoneNumber((registerRequest.getPhoneNumber()));
        user.setAddress(address);

        //user.setEmojiLink(registerRequest.getEmoji());
        if(!registerRequest.getEmojiFile().isEmpty()) {
            File file = fileUploadService.upload(registerRequest.getEmojiFile());
            String resizeResult = imageService.resizeImage(file);
            user.setEmojiLink(resizeResult);
            //file.delete();
        }
         userRepository.save(user);

    }
    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public void delete(Long id) {
        userRepository.delete(userRepository.getById(id));
    }




}
