package com.bob.masterdata.utils;

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
//        UserEntity user = userRepository.findByOathUserId(getCurrentUserId()).orElse(null);
//        return user != null && AppConstants.USER_ADMIN.equals(user.getRole());
    }
}

