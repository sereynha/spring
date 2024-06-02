package com.school.project.config;

import com.school.project.annotation.MockAdmin;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class SecurityContextFactoryTest  implements WithSecurityContextFactory<MockAdmin>{
    @Override
    public SecurityContext createSecurityContext(MockAdmin user) {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        Stream.of("users:read").map(a -> grantedAuthorities.add(new SimpleGrantedAuthority(a)));

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.email(),
                null,
                grantedAuthorities
        );

        securityContext.setAuthentication(authentication);

        return securityContext;
    }
}
