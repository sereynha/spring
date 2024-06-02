package com.school.project.annotation;

import com.school.project.config.SecurityContextFactoryTest;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = SecurityContextFactoryTest.class)
public @interface MockAdmin {
    String id() default "1";

    String email() default "King@gmial.com";

    String phone() default "+855 88 459955";
}
