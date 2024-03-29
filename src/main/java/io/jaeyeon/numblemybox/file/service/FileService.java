package io.jaeyeon.numblemybox.file.service;

import io.jaeyeon.numblemybox.file.dto.UploadFileResponse;
import io.jaeyeon.numblemybox.member.domain.entity.Member;
import java.io.IOException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
  /* 파일 업로드 */
  UploadFileResponse upload(MultipartFile file, Long folderId, Member owner);

  /* 파일 다운로드 */
  Resource downloadFile(String encodedFileName, Member owner) throws IOException;

  /* 파일 삭제 */
  void deleteFile(Long fileId, Member member);
}
