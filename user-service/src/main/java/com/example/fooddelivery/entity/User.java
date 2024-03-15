package com.example.fooddelivery.entity;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.example.fooddelivery.config.authorization.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    // If performance concerns arise, they are more effectively 
    // addressed through proper indexing, query optimization, and database tuning rather than a fundamental change in how data is stored. 
    @OneToMany(mappedBy = "user")
    private List<Token> tokens;

    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    private Date lastPasswordResetDate;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        if (!credentialsNonExpired) {
            return false; // Manually marked as expired
        }
        
        // Assuming you still want to use lastPasswordResetDate to automatically determine expiration
        if (lastPasswordResetDate == null) {
            return true; // Never reset, so not expired
        }
        long diff = new Date().getTime() - lastPasswordResetDate.getTime();
        long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        return days < 90; // Assuming credentials expire after 90 days
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    // Management methods
    public void lockAccount() {
        this.accountNonLocked = false;
    }

    public void unlockAccount() {
        this.accountNonLocked = true;
    }

    public void expireAccount() {
        this.accountNonExpired = false;
    }

    public void unexpireAccount() {
        this.accountNonExpired = true;
    }

    public void expireCredentials() {
        this.credentialsNonExpired = false;
    }

    public void unexpireCredentials() {
        this.credentialsNonExpired = true;
    }

    public void enableAccount() {
        this.enabled = true;
    }

    public void disableAccount() {
        this.enabled = false;
    }

    public void updatePasswordResetDate() {
        this.lastPasswordResetDate = new Date();
    }
}
