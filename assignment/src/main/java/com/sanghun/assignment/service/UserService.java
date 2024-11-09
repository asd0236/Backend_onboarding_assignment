package com.sanghun.assignment.service;

import com.sanghun.assignment.domain.dto.loginDto;
import com.sanghun.assignment.domain.dto.signupDto;
import com.sanghun.assignment.domain.entity.Authorities;
import com.sanghun.assignment.domain.entity.User;
import com.sanghun.assignment.repository.UserRepository;
import com.sanghun.assignment.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public signupDto.Response signup(signupDto.Request requestDto) {
        String username = requestDto.getUsername();

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        // 사용자 등록
        String password = passwordEncoder.encode(requestDto.getPassword());
        String nickname = requestDto.getNickname();

        return signupDto.Response.of(userRepository.save(new User(username, password, nickname, Authorities.USER)));

    }

    public loginDto.Response login(loginDto.Request requestDto) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();

        Optional<User> checkUsername = userRepository.findByUsername(username);

        // 회원 존재 여부 확인
        if (checkUsername.isEmpty()) {
            throw new UserNotFoundException("등록되지 않은 사용자입니다.");
        }

        User user = checkUsername.get();

        // 비밀번호 검증
        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtUtil.createToken(user.getUserId(), user.getUsername(), user.getAuthorities());
        return loginDto.Response.of(token);
    }

    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String username) {
            super("User not found: " + username);
        }
    }

}
