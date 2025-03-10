package com.ll.nbe342team8.domain.qna.question.controller;

import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.member.member.service.MemberService;
import com.ll.nbe342team8.domain.oauth.SecurityUser;
import com.ll.nbe342team8.domain.qna.question.dto.QuestionGenFileDto;
import com.ll.nbe342team8.domain.qna.question.entity.Question;
import com.ll.nbe342team8.domain.qna.question.entity.QuestionGenFile;
import com.ll.nbe342team8.domain.qna.question.service.QuestionService;
import com.ll.nbe342team8.global.config.AppConfig;
import com.ll.nbe342team8.global.exceptions.ServiceException;
import com.ll.nbe342team8.standard.util.Ut;
import com.ll.nbe342team8.standard.util.fileuploadutil.FileUploadUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Optional;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@Controller
@RequestMapping("/my/question/genFile")
@RequiredArgsConstructor
@Tag(name = "QuestionGenFileController", description = "파일 다운로드 등 다양한 기능 제공")
public class QuestionGenFileController {

    private final QuestionService questionService;
    private final MemberService memberService;

    @GetMapping("/download/{questionId}/{fileName}")
    @Operation(summary = "이미지 전송")
    public ResponseEntity<Resource> download(
            @PathVariable long questionId,
            @PathVariable String fileName,
            HttpServletRequest request
    ) throws FileNotFoundException {
        Question question = questionService.findById(questionId).get();

        QuestionGenFile genFile = question.getGenFiles()
                .stream()
                .filter(f -> f.getFileName().equals(fileName))
                .findFirst()
                .get();

        String filePath = genFile.getFilePath();

        Resource resource = new InputStreamResource(new FileInputStream(filePath));

        String contentType = request.getServletContext().getMimeType(filePath);

        if (contentType == null) contentType = "application/octet-stream";

        //String downloadFileName = FileUploadUtil.encode(genFile.getOriginalFileName()).replace("%20", " ");

        //주석 처리 제거시 다운로드 가능
        return ResponseEntity.ok()
                //.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + downloadFileName + "\"")
                .contentType(MediaType.parseMediaType(contentType)).body(resource);


    }

    @PostMapping(value = "/{questionId}/{typeCode}", consumes = MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "등록")
    public ResponseEntity<?> makeNewFile(
            @PathVariable Long questionId,
            @PathVariable String typeCode,
            @NonNull @RequestParam("file") @Schema(type = "string", format = "binary") MultipartFile file
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal()  instanceof SecurityUser securityUser)) {
            throw new ServiceException(HttpStatus.UNAUTHORIZED.value(),"로그인을 해야합니다.");
        }

        String oauthId=securityUser.getMember().getOAuthId();

        Member member = memberService.findByOauthId(oauthId)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND.value(), "사용자를 찾을 수 없습니다."));

        Question question = questionService.findById(questionId)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND.value(), "질문을 찾을 수 없습니다."));

        checkActorCanMakeNewGenFile(member,question);

        String filePath = FileUploadUtil.toFile(file, AppConfig.getTempDirPath());

        QuestionGenFile postGenFile = question.addGenFile(
                typeCode,
                filePath
        );

        questionService.flush();

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @DeleteMapping("/{questionId}/{fileNo}/{typeCode}")
    @Operation(summary = "삭제")
    public ResponseEntity<?> deleteNewFile(
            @PathVariable Long questionId,
            @PathVariable int fileNo,
            @PathVariable String typeCode
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal()  instanceof SecurityUser securityUser)) {
            throw new ServiceException(HttpStatus.UNAUTHORIZED.value(),"로그인을 해야합니다.");
        }

        String oauthId=securityUser.getMember().getOAuthId();

        Member member = memberService.findByOauthId(oauthId)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND.value(), "사용자를 찾을 수 없습니다."));

        Question question = questionService.findById(questionId)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND.value(), "질문을 찾을 수 없습니다."));

        question.deleteGenFile(typeCode,fileNo);

        questionService.flush();

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @PutMapping("/{questionId}/{fileId}")
    @Operation(summary = "수정")
    public ResponseEntity<?> putNewFile(
            @PathVariable Long questionId,
            @PathVariable Long fileId,
            @NonNull @RequestPart("file") MultipartFile file
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal()  instanceof SecurityUser securityUser)) {
            throw new ServiceException(HttpStatus.UNAUTHORIZED.value(),"로그인을 해야합니다.");
        }

        String oauthId=securityUser.getMember().getOAuthId();

        Member member = memberService.findByOauthId(oauthId)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND.value(), "사용자를 찾을 수 없습니다."));

        Question question = questionService.findById(questionId)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND.value(), "질문을 찾을 수 없습니다."));

        QuestionGenFile questionGenFile = question.getGenFileById(fileId).orElseThrow(
                () -> new ServiceException(HttpStatus.NOT_FOUND.value(), "%d번 파일은 존재하지 않습니다.".formatted(fileId))
        );

        checkActorCanMakeNewGenFile(member,question);

        String filePath = FileUploadUtil.toFile(file, AppConfig.getTempDirPath());

        question.modifyGenFile(questionGenFile.getTypeCode(),questionGenFile.getFileNo(), filePath);



        questionService.flush();

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    public void checkActorCanMakeNewGenFile(Member member,Question question) {
        if (member == null) throw new ServiceException(404,"로그인 후 이용해주세요.");
        if (member.getId() != question.getMember().getId()) throw new ServiceException(404,"게시글 작성 유저만 업로드 할 수 있습니다.");

    }


}