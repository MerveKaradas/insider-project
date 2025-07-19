package com.web.demo.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@EntityListeners(AuditingEntityListener.class) //entity (varlık) nesnelerinde otomatik olarak tarih/saat gibi izleme (auditing) alanlarını doldurmak için kullanılan bir dinleyicidir.JPA tarafından sağlanır.
@Table(name = "users")
public class User {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Kullanıcı adı boş olamaz!")
    @Size(min = 3, max = 20, message = "Kullanıcı adı 3-20 karakter arasında olmalıdır!")
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank(message = "Email boş olamaz!")
    @Size(max = 50, message = "Email 50 karakterden uzun olamaz!")
    @Email(message = "Geçerli bir email adresi giriniz!")
    @Column(nullable = false, unique = true)
    private String email;

    // Şifre hash'ini saklamak için kullanılır, bu nedenle boş olamaz. Burda validasyon yok çünü şifre hash'i genellikle uygulama tarafından oluşturulur ve doğrulanır.
    @Column(nullable = false)
    private String password_hash;
   
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_USER; // Varsayılan olarak ROLE_USER olarak ayarlanır

    @CreationTimestamp
    @Column(name = "created_at" ,nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public User() {
       
    }

    public User(String username, String email, String password_hash, Role role) {
        this.username = username;
        this.email = email;
        this.password_hash = password_hash;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPasswordHash() {
        return password_hash;
    }

    public void setPasswordHash(String password_hash) {
        this.password_hash = password_hash;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }


    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    
}
