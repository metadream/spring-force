package com.arraywork.springforce.security;

import java.lang.reflect.Method;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.arraywork.springforce.util.Assert;

/**
 * Security Interceptor
 *
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/02/29
 */
@Component
public class SecurityInterceptor implements HandlerInterceptor, WebMvcConfigurer {

    @Autowired
    private SecuritySession session;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        if (handler instanceof HandlerMethod handlerMethod) {
            // Get @Authority annotation in controller method
            Method method = handlerMethod.getMethod();
            Authority authority = method.getAnnotation(Authority.class);

            // Annotation exists?
            if (authority != null) {
                // Is logged in?
                Principal principal = session.getPrincipal();
                Assert.notNull(principal, HttpStatus.UNAUTHORIZED);

                // Has any role?
                String[] roles = authority.value();
                boolean hasAuthority = roles == null || roles.length == 0 || principal.hasAnyRole(roles);
                Assert.isTrue(hasAuthority, HttpStatus.FORBIDDEN);
            }
        }
        return true;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this).addPathPatterns("/**");
    }

}