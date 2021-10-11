package com.example.testintern.controller;

import com.example.testintern.dto.AuthenticationResponse;
import com.example.testintern.dto.LoginRequest;
import com.example.testintern.dto.RegisterRequest;
import com.example.testintern.model.User;
import com.example.testintern.service.AuthService;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.ServletContext;
import javax.validation.Valid;
import java.util.List;

import static org.springframework.web.servlet.function.ServerResponse.status;

@RestController
@AllArgsConstructor
public class AuthController  {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;


//    @InitBinder
//    public void initBinder(WebDataBinder webDataBinder){
//        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
//        webDataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
//    }
//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/register_success").setViewName("register_success");
//    }

//    @GetMapping("/register")
//    public String join(@ModelAttribute RegisterRequest registerRequest, Model model){
//        model.addAttribute("registerRequest", registerRequest);
//         return "register";
//
//    }
    @PostMapping("/register")
    public String save(@ModelAttribute @Valid RegisterRequest registerRequest, BindingResult bindingResult) throws Exception {

        if(authService.userExists(registerRequest.getEmail())){
            bindingResult.addError(new FieldError("registerRequest", "email", "Email already in use!"));
        }


        if (bindingResult.hasErrors()) {
            return  String.valueOf(bindingResult.getAllErrors().get(0).getDefaultMessage());

        } else {
            authService.save(registerRequest);
            return "register_success";
        }

    }
    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }
    @GetMapping("/allUsers")
    public List<User> getAllUser(){
        return authService.showUserList();
    }
    @GetMapping("/userDetail/{id}")
    public User getUserDetail(@PathVariable Long id){
        return authService.findUserById(id);
    }
    @PutMapping("/update/{id}")
    public String update(@ModelAttribute @Valid RegisterRequest registerRequest, @PathVariable Long id){
        authService.update(registerRequest, id);

        return "update_successful";
    }
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        authService.delete(id);
        return "delete successful";
    }

}
