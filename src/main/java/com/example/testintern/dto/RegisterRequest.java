package com.example.testintern.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$", message = "Username must contain at least one lowercase letter, one uppercase letter, and one number.")
    private String username;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"
            , message ="Password must be at least one lowercase letter, one uppercase letter, one number, one special character each." )
    private String password;
    @NotEmpty(message = "Name can't be empty")
    private String name;

    @Pattern(regexp = "^[a-z0-9]+@[a-z0-9]+\\.[a-z]{2,12}$"
    ,message = "Email must be in the form: abd@xxxx.xxx ")

    private String email;

    @Pattern(regexp = "\\(?([0-9]{3})\\)?([-]?)([0-9]{4})\\2([0-9]{4})", message =  "Phone Number must be in the form: xxx-xxxx-xxxx")

    private String phoneNumber;
    @NotEmpty(message = "Zip code can't be empty")
    private String zipCode;
    @NotEmpty(message = "Address can't be empty")
    private String addressDetail;

    private MultipartFile emojiFile;

}
