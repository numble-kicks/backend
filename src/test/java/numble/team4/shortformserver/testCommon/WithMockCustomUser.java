package numble.team4.shortformserver.testCommon;

import numble.team4.shortformserver.member.member.domain.Role;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {

    Role role() default Role.MEMBER;

    String name() default "테스트사용자";

    String email() default "test@email.com";

    boolean emailVerified() default true;

}
