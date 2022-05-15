package kr.co.won.auth;

import kr.co.won.user.domain.type.UserRoleType;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(value = RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = TestMockUserFactory.class)
public @interface TestMockUser {
    String email() default "tester@co.kr";

    String name() default "tester";

    String password() default "1234";

    UserRoleType authLevel() default UserRoleType.USER;

}
