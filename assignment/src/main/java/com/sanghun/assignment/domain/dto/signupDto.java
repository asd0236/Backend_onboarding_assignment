package com.sanghun.assignment.domain.dto;

import com.sanghun.assignment.domain.entity.Authorities;
import com.sanghun.assignment.domain.entity.User;
import lombok.Builder;
import lombok.Data;

public interface signupDto {

    @Data
    @Builder
    class Request {
        private String username;
        private String password;
        private String nickname;
    }

    @Data
    @Builder
    class Response {
        private String username;
        private String password;
        private Authorities authorities;

        public static Response of(User user) {
            return Response.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .authorities(user.getAuthorities())
                    .build();
        }
    }

}
