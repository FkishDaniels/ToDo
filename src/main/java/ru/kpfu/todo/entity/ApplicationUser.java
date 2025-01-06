package ru.kpfu.todo.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ApplicationUser implements UserDetails {
    private Long id;
    private String username;
    private String email;
    private String password;

    private List<Todo> todoList;

    private List<GlobalPermission> globalPermissions;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return globalPermissions.stream()
                .map(gb -> new SimpleGrantedAuthority(gb.getName().name()))
                .toList();
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
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
