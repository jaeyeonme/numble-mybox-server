package io.jaeyeon.numblemybox.member.dto;

import io.jaeyeon.numblemybox.member.domain.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import org.springframework.security.crypto.password.PasswordEncoder;

public record MemberRegistration(
    @NotEmpty
        @Email(
            message = "유효하지 않은 이메일 형식입니다.",
            regexp =
                "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")
        @Schema(description = "사용자 이메일", example = "test@gmail.com")
        String email,
    @NotEmpty
        @Pattern(
            message = "최소 한개 이상의 대소문자와 숫자, 특수문자를 포함한 8자 이상 16자 이하의 비밀번호를 입력해야 합니다.",
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#!~$%^&-+=()])(?=\\S+$).{8,16}$")
        @Schema(description = "사용자 비밀번호", example = "test1234!")
        String password) {

  public static Member toEntity(MemberRegistration dto, PasswordEncoder passwordEncoder) {
    return Member.builder().email(dto.email).password(passwordEncoder.encode(dto.password)).build();
  }
}
