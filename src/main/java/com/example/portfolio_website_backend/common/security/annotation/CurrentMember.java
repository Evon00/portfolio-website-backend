package com.example.portfolio_website_backend.common.security.annotation;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CurrentMember {
}
