package com.bob.candidateportal.util;

import com.bob.db.entity.UserEntity;
import com.bob.db.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {
    @Autowired
    private UserRepository userRepository;
    public boolean isAdmin() {
        return true;
//        UserEntity user = userRepository.findByOathUserId(getCurrentUserToken()).orElse(null);
//        return user != null && AppConstants.USER_ADMIN.equals(user.getRole());
    }
    public String getCurrentUserToken() {
        return "00000000-0000-0000-0000-000000000000";

//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth.getPrincipal() instanceof org.springframework.security.oauth2.jwt.Jwt jwt) {
//            return jwt.getClaimAsString("sub");
//        }
//        return null;
    }
    public Long getCurrentUserId(){
        return 1l;
//        UserEntity user = userRepository.findByOathUserId(getCurrentUserToken()).orElse(null);
//        return user != null?user.getUserId():null;
    }

    public String getCurrentUserRole() {
        return "Admin";
//        UserEntity user = userRepository.findByOathUserId(getCurrentUserToken()).orElse(null);
//        return user != null ? user.getRole() : null;
    }
}