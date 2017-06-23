package com.rx.helpers;

import com.rx.dao.User;
import com.rx.dao.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;

public class AuthenticatedUser implements UserDetails {

    private Long id;

    private String username;

    private String password;

    private String lastName;

    private String firstName;

    private String middleName;

    private UserRole userRole;

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean credentialsNonExpired;

    private boolean enabled;

    private Collection<GrantedAuthority> authorities;

    private AuthenticatedUser(AuthenticatedUserBuilder authenticatedUserBuilder) {
        this.id = authenticatedUserBuilder.id;
        this.username = authenticatedUserBuilder.username;
        this.password = authenticatedUserBuilder.password;
        this.lastName = authenticatedUserBuilder.lastName;
        this.firstName = authenticatedUserBuilder.firstName;
        this.middleName = authenticatedUserBuilder.middleName;
        this.userRole = authenticatedUserBuilder.userRole;

        this.accountNonExpired = authenticatedUserBuilder.accountNonExpired;
        this.accountNonLocked = authenticatedUserBuilder.accountNonLocked;
        this.enabled = authenticatedUserBuilder.enabled;
        this.credentialsNonExpired = authenticatedUserBuilder.credentialsNonExpired;
        this.authorities = authenticatedUserBuilder.authorities;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public UserRole getUserRole() {
        return userRole;
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
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    public static AuthenticatedUserBuilder user(User user) {
        return new AuthenticatedUserBuilder(user);
    }

    public static class AuthenticatedUserBuilder {

        private Long id;

        private String username;

        private String password;

        private String lastName;

        private String firstName;

        private String middleName;

        private UserRole userRole;

        private boolean accountNonExpired = true;

        private boolean accountNonLocked = true;

        private boolean credentialsNonExpired = true;

        private boolean enabled = true;

        private Collection<GrantedAuthority> authorities = new HashSet<>();


        private AuthenticatedUserBuilder(User daoUser) {
            this.id = daoUser.getId();
            this.username = daoUser.getUsername();
            this.password = daoUser.getPassword();
            this.lastName = daoUser.getLastName();
            this.firstName = daoUser.getFirstName();
            this.middleName = daoUser.getMiddleName();
            this.userRole = daoUser.getUserRole();

            this.authorities.add(new SimpleGrantedAuthority(daoUser.getUserRole().toString()));
        }

        public AuthenticatedUserBuilder accountNonExpired(boolean accountNonExpired) {
            this.accountNonExpired = accountNonExpired;
            return this;
        }

        public AuthenticatedUserBuilder accountNonLocked(boolean accountNonLocked) {
            this.accountNonExpired = accountNonLocked;
            return this;
        }

        public AuthenticatedUserBuilder credentialsNonExpired(boolean credentialsNonExpired) {
            this.credentialsNonExpired = credentialsNonExpired;
            return this;
        }

        public AuthenticatedUserBuilder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public AuthenticatedUser build() {
            return new AuthenticatedUser(this);
        }
    }
}
