package io.jaeyeon.numblemybox.member;

import static org.mockito.BDDMockito.*;

import io.jaeyeon.numblemybox.folder.domain.repository.FolderRepository;
import io.jaeyeon.numblemybox.folder.dto.StorageInfo;
import io.jaeyeon.numblemybox.member.domain.entity.Member;
import io.jaeyeon.numblemybox.member.domain.repository.MemberRepository;
import io.jaeyeon.numblemybox.member.dto.ChangePasswordRequest;
import io.jaeyeon.numblemybox.member.dto.MemberRegistration;
import io.jaeyeon.numblemybox.member.service.GeneralMemberService;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

  @InjectMocks private GeneralMemberService memberService;

  @Mock private MemberRepository memberRepository;
  @Mock private FolderRepository folderRepository;

  @Mock private PasswordEncoder passwordEncoder;

  private MemberRegistration memberRegistration;
  private Member member;
  private ChangePasswordRequest passwordRequest;

  @BeforeEach
  void setUp() {
    memberRegistration = new MemberRegistration("test@gmail.com", "Password123!");
    member = MemberRegistration.toEntity(memberRegistration, passwordEncoder);
    passwordRequest = new ChangePasswordRequest("Password123!", "NewPassword123!");
  }

  @Test
  @DisplayName("회원등록시 저장 확인")
  void testRegistrationMember() throws Exception {
    // given
    given(memberRepository.existsByEmail(anyString())).willReturn(false);

    // when
    memberService.registrationMember(member);

    // then
    then(memberRepository).should().save(any(Member.class));
  }

  @Test
  @DisplayName("등록된 이메일 중복 여부 체크 확인")
  void testisDuplicatedEmail() throws Exception {
    // given
    given(memberRepository.existsByEmail(anyString())).willReturn(true);

    // when
    boolean isDuplicated = memberService.isDuplicatedEmail("test@gmail.com");

    // then
    Assertions.assertTrue(isDuplicated);
  }

  @Test
  @DisplayName("회원정보 이메일로 찾기 확인")
  void testFindMemberByEmail() throws Exception {
    // given
    given(memberRepository.findMemberByEmail(anyString())).willReturn(Optional.of(member));

    // when
    Member findMember = memberService.findMemberByEmail("test@gmail.com");

    // then
    Assertions.assertEquals(member, findMember);
  }

  @Test
  @DisplayName("회원정보 ID로 찾기 확인")
  void testFindMemberById() throws Exception {
    // given
    given(memberRepository.findMemberById(anyLong())).willReturn(Optional.of(member));

    // when
    Member findMember = memberService.findMemberById(1L);

    // then
    Assertions.assertEquals(member, findMember);
    ;
  }

  @Test
  @DisplayName("유효한 회원 확인")
  void testIsValidMember() throws Exception {
    // given
    given(memberRepository.findMemberByEmail(anyString())).willReturn(Optional.of(member));
    given(passwordEncoder.matches(eq(memberRegistration.password()), eq(member.getPassword())))
        .willReturn(true);

    // when
    Member result = memberService.validateAndFindMemberByEmail(memberRegistration, passwordEncoder);

    // then
    Assertions.assertEquals(result, member);
  }

  @Test
  @DisplayName("비밀번호 변경 확인")
  void testChangePassword() throws Exception {
    // given
    String newPassword = "NewPassword123!";
    given(passwordEncoder.encode(anyString())).willReturn(newPassword);

    // when
    memberService.changePassword(member, passwordRequest, passwordEncoder);

    // then
    Assertions.assertEquals(newPassword, member.getPassword());
  }

  @Test
  @DisplayName("ROOT 폴더 생성 및 초기 공간 설정 확인")
  void testCreateRootFolderAndSetInitialSpace() throws Exception {
    // given
    given(memberRepository.save(any(Member.class))).willReturn(member);

    // when
    memberService.registrationMember(member);

    // then
    then(memberRepository).should().save(any(Member.class));
  }

  @Test
  @DisplayName("사용 가능한 공간 정보 조회 확인")
  void testGetStorageInfo() throws Exception {
    // given
    given(memberRepository.findMemberById(anyLong())).willReturn(Optional.of(member));

    // when
    StorageInfo storageInfo = memberService.getStorageInfo(1L);

    // then
    Assertions.assertNotNull(storageInfo);
    Assertions.assertEquals(30.0, storageInfo.allocatedSpace());
    Assertions.assertEquals(0.0, storageInfo.usedSpace());
  }
}
