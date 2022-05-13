package numble.team4.shortformserver.common.config;

import static numble.team4.shortformserver.member.member.domain.Role.ADMIN;
import static numble.team4.shortformserver.member.member.domain.Role.MEMBER;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import numble.team4.shortformserver.common.filter.JwtAuthenticationFilter;
import numble.team4.shortformserver.common.filter.JwtExceptionHandleFilter;
import numble.team4.shortformserver.member.auth.util.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .httpBasic().disable()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtExceptionHandleFilter(objectMapper), JwtAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET,"/v1/users/following/from", "/v1/users/following/to").permitAll()
                .antMatchers(HttpMethod.POST, "/v1/users/following/{followId}", "/v1/videos/{videoId}/likes").hasAnyRole(MEMBER.name(), ADMIN.name())
                .antMatchers(HttpMethod.DELETE, "/v1/videos/likes/{likesId}", "/v1/users/following/{toMemberId}").hasAnyRole(MEMBER.name(), ADMIN.name())
                .antMatchers(HttpMethod.GET, "/v1/videos/{videoId}").permitAll()
                .antMatchers(HttpMethod.GET, "/v1/videos").permitAll()
                .antMatchers(HttpMethod.GET, "/v1/search/**").permitAll()
                .antMatchers("/oauth/**", "/renew").permitAll()
                .anyRequest().authenticated();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
