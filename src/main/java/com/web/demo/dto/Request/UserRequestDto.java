package com.web.demo.dto.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserRequestDto {


    @NotBlank(message = "Kullanıcı adı boş olamaz!")
    @Size(min = 3, max = 20, message = "Kullanıcı adı 3-20 karakter arasında olmalıdır!")
    private String username;

    @NotBlank(message = "Email boş olamaz!")
    @Email(message = "Geçerli bir email adresi giriniz!")
    private String email;

    @NotBlank(message = "Parola boş olamaz!")
    @Size(min = 8, max = 100, message = "Parola 8-100 karakter arasında olmalıdır!")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
        message = "Parola en az bir büyük harf, bir küçük harf, bir sayı ve bir özel karakter içermeli"
    )
    private String password;
    // Bu kurala göre parola: En az 1 küçük harf,1 büyük harf, 1 rakam, 1 özel karakter (@$!%*?&) içermeli


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
