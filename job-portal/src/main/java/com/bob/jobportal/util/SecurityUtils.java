package com.bob.jobportal.util;

import com.bob.db.entity.UserEntity;
import com.bob.db.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {
    @Autowired
    private  UserRepository userRepository;
    public boolean isAdmin() {
        UserEntity user = userRepository.findByOathUserId(getCurrentUserToken()).orElse(null);
        return user != null && AppConstants.USER_ADMIN.equals(user.getRole());
    }
    public String getCurrentUserRole() {
        UserEntity user = userRepository.findByOathUserId(getCurrentUserToken()).orElse(null);
        return user != null ? user.getRole() : null;
    }
    public String getCurrentUserToken() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getPrincipal() instanceof org.springframework.security.oauth2.jwt.Jwt jwt) {
            return jwt.getClaimAsString("sub");
        }
        return null;
    }
    public String getCurrentUserId(){
        UserEntity user = userRepository.findByOathUserId(getCurrentUserToken()).orElse(null);
        return ""+user.getUserId();
    }
}
