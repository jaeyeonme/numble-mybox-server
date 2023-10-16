package io.jaeyeon.numblemybox.member.domain.entity;

import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "MEMBER")
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "MEMBER_ID")
  private Long id;

  @Column(name = "email", unique = true, nullable = false)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(name = "allocated_space", nullable = false)
  private Long allocatedSpace = 30 * 1024 * 1024 * 1024L;

  @Column(name = "used_space", nullable = false)
  private Long usedSpace = 0L;

  @Builder
  public Member(String email, String password) {
    this.email = email;
    this.password = password;
  }

  public boolean isPasswordMatching(String rawPassword, PasswordEncoder passwordEncoder) {
    return passwordEncoder.matches(rawPassword, this.password);
  }

  public void changePassword(String password) {
    this.password = password;
  }

  public void increaseUsedSpace(Long size) {
    this.usedSpace += size;
  }

  public void decreaseUsedSpace(Long size) {
    this.usedSpace -= size;
  }
}
