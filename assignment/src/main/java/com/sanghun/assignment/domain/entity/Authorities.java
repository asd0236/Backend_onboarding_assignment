package com.sanghun.assignment.domain.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import java.io.Serializable;

public enum Authorities implements Serializable {

    USER(Authority.USER),
    ADMIN(Authority.ADMIN);

    private final String authorityName;

    Authorities(String authority) {
        this.authorityName = authority;
    }

    @JsonValue // JSON 직렬화 시 이 메서드의 반환값이 사용되도록 설정
    public AuthorityDTO toJson() {
        return new AuthorityDTO(this.authorityName);
    }

    public String getAuthorityName() {
        return this.authorityName;
    }

    public static class Authority {
        public static final String USER = "ROLE_USER";
        public static final String ADMIN = "ROLE_ADMIN";
    }

    // JSON 변환을 위한 DTO 클래스
    public static class AuthorityDTO {
        private String authorityName;

        public AuthorityDTO(String authorityName) {
            this.authorityName = authorityName;
        }

        public String getAuthorityName() {
            return authorityName;
        }

        public void setAuthorityName(String authorityName) {
            this.authorityName = authorityName;
        }
    }
}
