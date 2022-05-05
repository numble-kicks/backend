package numble.team4.shortformserver.member.auth.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import numble.team4.shortformserver.member.auth.util.dto.JwtTokenDto;
import numble.team4.shortformserver.member.member.domain.Member;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static io.jsonwebtoken.SignatureAlgorithm.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final UserDetailsService userDetailsService;

    @Value("${jwt.secretKey}")
    private String SECRET_KEY;

    @Value("${jwt.refreshKey}")
    private String REFRESH_KEY;

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer";
    private static final String TOKEN_DELIMITER = " ";
    private static final long ACCESS_TOKEN_VALID_TIME = 30 * 60 * 1000L;
    private static final long REFRESH_TOKEN_VALID_TIME  = 30 * 60 * 1000L * 2 * 24;

    public JwtTokenDto createTokens(Member member) {
        String accessToken = createToken(member, SECRET_KEY, ACCESS_TOKEN_VALID_TIME);
        String refreshToken = createToken(member, REFRESH_KEY, REFRESH_TOKEN_VALID_TIME);

        return new JwtTokenDto(accessToken, refreshToken);
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUserIdFromAccessToken(token).toString());
        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
    }

    public String createToken(Member member, String encodingKey, long validTime) {
        Claims claims = Jwts.claims().setSubject(member.getId().toString());
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + validTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(HS256, encodingKey)
                .compact();
    }

    public Long getUserIdFromAccessToken(String token) {
        return Long.valueOf(Jwts.parser().setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject());
    }

    public Long getUserIdFromRefreshToken(String token) {
        return Long.valueOf(Jwts.parser().setSigningKey(REFRESH_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject());
    }

    public String extractToken(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION);
        if (!StringUtils.hasText(header) || !header.startsWith(BEARER)) {
            return header;
        }

        return header.split(TOKEN_DELIMITER)[1].trim();
    }

    public boolean validateTokenSecretToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validateTokenRefreshToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(REFRESH_KEY).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
