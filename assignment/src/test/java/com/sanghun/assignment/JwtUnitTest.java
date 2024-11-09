package com.sanghun.assignment;

import com.sanghun.assignment.domain.entity.Authorities;
import com.sanghun.assignment.security.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Base64;
import java.util.Date;

public class JwtUnitTest {

    private JwtUtil jwtUtil;
    private String secretKey = Base64.getEncoder().encodeToString("very-strong-secret-key-of-32-characters!".getBytes());

    @BeforeEach
    public void setUp() {
        jwtUtil = new JwtUtil();
        jwtUtil.setSecretKey(secretKey);
        jwtUtil.init(); // 초기화
    }

    // Refresh Token 발행 테스트
    @Test
    public void testCreateRefreshToken() {
        // Refresh Token을 생성
        String refreshToken = jwtUtil.createRefreshToken(1L, "testUser", Authorities.USER);

        // Refresh Token이 null이 아니어야 함
        assertNotNull(refreshToken, "Refresh Token은 null이 아니어야 합니다");

        // Refresh Token이 'Bearer '로 시작하는지 확인
        assertTrue(refreshToken.startsWith(JwtUtil.BEARER_PREFIX), "Refresh Token은 'Bearer '로 시작해야 합니다");

        // 만료 시간을 확인 (현재보다 이후의 날짜여야 함)
        Date expirationDate = jwtUtil.extractExpiration(refreshToken);
        assertTrue(expirationDate.after(new Date()), "Refresh Token의 만료 시간이 현재보다 이후여야 합니다");
    }

    // Refresh Token 검증 테스트 (유효한 토큰)
    @Test
    public void testVerifyValidRefreshToken() {
        // 유효한 Refresh Token 생성
        String refreshToken = jwtUtil.createRefreshToken(1L, "testUser", Authorities.USER);

        // 토큰을 검증하고 claims 추출
        Claims claims = jwtUtil.extractClaims(refreshToken);

        // claims가 null이 아니어야 함
        assertNotNull(claims, "Claims는 null이 아니어야 합니다");

        // 주체(subject)가 사용자 ID와 일치하는지 확인
        assertEquals("1", claims.getSubject(), "주체(subject)는 사용자 ID와 일치해야 합니다");

        // 사용자 이름(username) 클레임이 올바르게 포함되어 있는지 확인
        assertEquals("testUser", claims.get("username"), "사용자 이름(username) 클레임은 일치해야 합니다");
    }

    // Refresh Token 검증 테스트 (만료된 토큰)
    @Test
    public void testVerifyExpiredRefreshToken() {
        // 만료된 Refresh Token 생성
        Date pastDate = new Date(System.currentTimeMillis() - 1000);
        String expiredToken = Jwts.builder()
                .setSubject("1")
                .claim("username", "expiredUser")
                .claim("role", Authorities.USER)
                .setExpiration(pastDate)
                .signWith(jwtUtil.getKey(), SignatureAlgorithm.HS256)
                .compact();

        // 만료된 토큰을 사용한 경우 예외가 발생해야 함
        Exception exception = assertThrows(RuntimeException.class, () -> {
            jwtUtil.extractClaims(JwtUtil.BEARER_PREFIX + expiredToken);
        });

        // 예외 메시지가 'JWT 검증 실패'를 포함하는지 확인
        assertTrue(exception.getMessage().contains("JWT 검증 실패"), "만료된 Refresh Token에 대해 'JWT 검증 실패' 메시지가 나와야 합니다");
    }

    // Refresh Token 검증 테스트 (변조된 토큰)
    @Test
    public void testVerifyTamperedRefreshToken() {
        // 변조된 토큰 (유효하지 않은 서명으로 생성)
        String tamperedToken = "tampered.token.here";

        // 변조된 토큰을 사용한 경우 예외가 발생해야 함
        Exception exception = assertThrows(RuntimeException.class, () -> {
            jwtUtil.extractClaims(tamperedToken);
        });

        // 예외 메시지가 'JWT 검증 실패'를 포함하는지 확인
        assertTrue(exception.getMessage().contains("JWT 검증 실패"), "변조된 Refresh Token에 대해 'JWT 검증 실패' 메시지가 나와야 합니다");
    }

    // 잘못된 형태의 Refresh Token을 사용한 경우 예외가 발생하는지 테스트
    @Test
    public void testVerifyInvalidFormatRefreshToken() {
        // 잘못된 형식의 토큰 (시작 부분이 'Bearer '가 아닌 경우)
        String invalidToken = "invalid.token.format";

        // 잘못된 형식의 토큰을 사용한 경우 예외가 발생해야 함
        Exception exception = assertThrows(RuntimeException.class, () -> {
            jwtUtil.extractClaims(invalidToken);
        });

        // 예외 메시지가 'JWT 검증 실패'를 포함하는지 확인
        assertTrue(exception.getMessage().contains("JWT 검증 실패"), "잘못된 형식의 Refresh Token에 대해 'JWT 검증 실패' 메시지가 나와야 합니다");
    }

    // 토큰 생성 기능에 대한 단위 테스트
    // 토큰이 생성되어 null이 아니고, 'Bearer '로 시작하는지 확인
    @Test
    public void testCreateToken() {
        String token = jwtUtil.createToken(1L, "testUser", Authorities.USER);
        assertNotNull(token, "토큰은 null이 아니어야 합니다");
        assertTrue(token.startsWith(JwtUtil.BEARER_PREFIX), "토큰은 'Bearer '로 시작해야 합니다");
    }

    // 클레임을 추출하고 해당 클레임 값들이 예상되는 값과 일치하는지 확인하는 테스트
    // 주체(subject), 사용자 이름(username), 역할(role) 클레임을 체크
    @Test
    public void testExtractClaims() {
        String token = jwtUtil.createToken(1L, "testUser", Authorities.USER);
        Claims claims = jwtUtil.extractClaims(token);

        assertNotNull(claims, "클레임은 null이 아니어야 합니다");
        assertEquals("1", claims.getSubject(), "주체(subject)는 사용자 ID와 일치해야 합니다");
        assertEquals("testUser", claims.get("username"), "사용자 이름(username) 클레임은 일치해야 합니다");

        // Json 응답에서 ROLE 부분 추출
        String input = claims.get("role").toString();
        int startIndex = input.indexOf("ROLE_") + "ROLE_".length();
        String role = input.substring(startIndex, input.length() - 1);

        assertEquals(Authorities.USER.toString(), role, "역할(role) 클레임은 일치해야 합니다");
    }

    // 유효하지 않은 토큰을 사용할 경우 예외가 발생하는지 테스트
    @Test
    public void testExtractClaimsWithInvalidToken() {
        String invalidToken = "invalid.token.here";

        Exception exception = assertThrows(RuntimeException.class, () -> {
            jwtUtil.extractClaims(invalidToken);
        });

        assertEquals("JWT 검증 실패", exception.getMessage(), "유효하지 않은 토큰일 경우 'JWT 검증 실패' 메시지가 나와야 합니다");
    }

    // 만료된 토큰을 사용할 경우 예외가 발생하는지 테스트
    @Test
    public void testExpiredToken() {
        // 만료 시간이 현재보다 이전으로 설정된 토큰을 생성
        Date pastDate = new Date(System.currentTimeMillis() - 1000);
        String expiredToken = Jwts.builder()
                .setSubject("1")
                .claim("username", "expiredUser")
                .claim("role", Authorities.USER)
                .setExpiration(pastDate)
                .signWith(jwtUtil.getKey(), SignatureAlgorithm.HS256)
                .compact();

        Exception exception = assertThrows(RuntimeException.class, () -> {
            jwtUtil.extractClaims(JwtUtil.BEARER_PREFIX + expiredToken);
        });

        assertTrue(exception.getMessage().contains("JWT 검증 실패"), "만료된 토큰에 대해 'JWT 검증 실패' 메시지가 나와야 합니다");
    }
}
