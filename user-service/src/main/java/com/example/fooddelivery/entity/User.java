package com.example.fooddelivery.entity;

import java.util.Collection;

import java.util.Set;
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

    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_additional_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> additionalRoles;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private PasswordCredential passwordCredential;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private UserProfile userProfile;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }
    
    // Implementing missing methods from UserDetails
    @Override
    public String getPassword() {
        return this.passwordCredential.getPassword();
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.passwordCredential.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.passwordCredential.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.passwordCredential.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return this.passwordCredential.isEnabled();
    }
}
