package numble.team4.shortformserver.testCommon.mockUser;

import numble.team4.shortformserver.member.member.domain.Role;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomAdminSecurityContextFactory.class)
public @interface WithMockAdmin {

    Role role() default Role.ADMIN;

    String name() default "테스트관리자";

    String email() default "admin@numble.com";

    boolean emailVerified() default true;
}
