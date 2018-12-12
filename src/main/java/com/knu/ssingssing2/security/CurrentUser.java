package com.knu.ssingssing2.security;

// @AuthenticationPrincipal은 현재 유저의 auth 정보를 가져오게 하는 어노테이션인데 이를 CurrentUser 어노테이션으로 한번 감싸줌

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Target({ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AuthenticationPrincipal
public @interface CurrentUser {

}
