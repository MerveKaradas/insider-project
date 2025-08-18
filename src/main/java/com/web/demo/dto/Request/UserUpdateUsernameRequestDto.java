package com.web.demo.dto.Request;

import java.io.Serializable;

import jakarta.validation.constraints.Size;

public class UserUpdateUsernameRequestDto implements Serializable {

    private static final long serialVersionUID = 1012L;

    @Size(min = 3, max = 20, message = "Kullanıcı adı 3-20 karakter arasında olmalıdır!")
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    

    
}
