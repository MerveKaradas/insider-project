package com.web.demo.dto.Request;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserUpdatePasswordRequestDto implements Serializable {

    private static final long serialVersionUID = 1011L;

    @NotBlank(message = "Eski parola boş olamaz.")
    private String oldPassword;
    

    @NotBlank(message = "Yeni parola boş olamaz.")
    @Size(min = 8, max = 100, message = "Parola 8-100 karakter arasında olmalıdır!")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
        message = "Parola en az bir büyük harf, bir küçük harf, bir sayı ve bir özel karakter içermeli"
    )
    private String newPassword;


    public String getOldPassword() {
        return oldPassword;
    }


    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }


    public static long getSerialversionuid() {
        return serialVersionUID;
    }


    public String getNewPassword() {
        return newPassword;
    }


    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }


 
    
    
}
