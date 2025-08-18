package com.web.demo.dto.Request;

import java.io.Serializable;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserUpdateRequestDto implements Serializable {

    private static final long serialVersionUID = 1010L;
   
    @Size(min = 3, max = 20, message = "Kullanıcı adı 3-20 karakter arasında olmalıdır!")
    private String username;

  
    @Email(message = "Geçerli bir email adresi giriniz!")
    private String email;

   
    @Size(min = 8, max = 100, message = "Parola 8-100 karakter arasında olmalıdır!")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
        message = "Parola en az bir büyük harf, bir küçük harf, bir sayı ve bir özel karakter içermeli"
    )
    private String password;

    public UserUpdateRequestDto() {
    }

 
    public UserUpdateRequestDto(
            @Size(min = 3, max = 20, message = "Kullanıcı adı 3-20 karakter arasında olmalıdır!") String username,
            @Email(message = "Geçerli bir email adresi giriniz!") String email,
            @Size(min = 8, max = 100, message = "Parola 8-100 karakter arasında olmalıdır!") @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Parola en az bir büyük harf, bir küçük harf, bir sayı ve bir özel karakter içermeli") String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    
    
}
