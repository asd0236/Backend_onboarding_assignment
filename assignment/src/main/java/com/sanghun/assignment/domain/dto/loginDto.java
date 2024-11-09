package com.sanghun.assignment.domain.dto;

import com.sanghun.assignment.domain.entity.Authorities;
import com.sanghun.assignment.domain.entity.User;
import lombok.Builder;
import lombok.Data;

public interface loginDto {

    @Data
    @Builder
    class Request {
        private String username;
        private String password;
    }

    @Data
    @Builder
    class Response {
        private String token;

        public static Response of(String token) {
            return Response.builder()
                    .token(token)
                    .build();
        }
    }

}
