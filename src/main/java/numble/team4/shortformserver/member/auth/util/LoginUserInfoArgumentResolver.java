package numble.team4.shortformserver.member.auth.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import numble.team4.shortformserver.member.auth.domain.MemberAdapter;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.MemberRepository;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoginUserInfoArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasMemberInfoAnnotation = parameter.hasParameterAnnotation(LoginUser.class);
        boolean hasMemberType = Member.class.isAssignableFrom(parameter.getParameterType());
        return hasMemberInfoAnnotation && hasMemberType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        MemberAdapter memberAdapter = (MemberAdapter) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        return memberAdapter.getMember();
    }
}
