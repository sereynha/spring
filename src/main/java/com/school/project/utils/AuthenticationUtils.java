package com.school.project.utils;

import com.school.project.exception.DuplicatedException;
import com.school.project.model.User;
import lombok.Data;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@Data
public class AuthenticationUtils {

    public static String extractUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof AnonymousAuthenticationToken) {
            throw new DuplicatedException(Constants.ERROR_CODE.ACCESS_DENIED, "Invalid Use");
        }

        UserDetails principal = (UserDetails) authentication.getPrincipal();

        return principal.getUsername();
    }
}
