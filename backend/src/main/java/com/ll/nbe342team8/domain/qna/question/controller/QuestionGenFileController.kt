package com.ll.nbe342team8.domain.qna.question.controller

import com.ll.nbe342team8.domain.member.member.entity.Member
import com.ll.nbe342team8.domain.member.member.service.MemberService
import com.ll.nbe342team8.domain.oauth.SecurityUser
import com.ll.nbe342team8.domain.qna.question.entity.Question
import com.ll.nbe342team8.domain.qna.question.service.QuestionService
import com.ll.nbe342team8.global.config.AppConfig
import com.ll.nbe342team8.global.exceptions.ServiceException
import com.ll.nbe342team8.standard.util.fileuploadutil.FileUploadUtil
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import lombok.RequiredArgsConstructor
import org.springframework.core.io.InputStreamResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.FileInputStream


@Controller
@RequestMapping("/my/question/genFile")
@RequiredArgsConstructor
@Tag(name = "QuestionGenFileController", description = "파일 다운로드 등 다양한 기능 제공")
class QuestionGenFileController (
    private val questionService: QuestionService,
) {


    @GetMapping("/download/{questionId}/{fileNo}")
    @Operation(summary = "이미지 전송")
    fun loadImage(
        @PathVariable questionId: Long,
        @PathVariable fileNo: Int,
        request: HttpServletRequest,
        @AuthenticationPrincipal securityUser: SecurityUser?
    ): ResponseEntity<Resource> {

        val member: Member = securityUser?.member
            ?: throw ServiceException(HttpStatus.BAD_REQUEST.value(), "올바른 요청이 아닙니다. 로그인 상태를 확인하세요.")

        val question = questionService.findById(questionId)

        val genFile = question.genFiles
            .find { it.fileNo == fileNo }
            ?: throw ServiceException(HttpStatus.NOT_FOUND.value(),"사진 파일이 존재하지 않습니다.")

        val filePath = genFile.filePath
        val resource = InputStreamResource(FileInputStream(filePath))

        val contentType = request.servletContext.getMimeType(filePath) ?: "application/octet-stream"

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .body(resource)
    }

    @PostMapping("/{questionId}/{typeCode}", consumes = [MULTIPART_FORM_DATA_VALUE])
    @Operation(summary = "등록")
    fun makeNewFile(
        @PathVariable questionId: Long,
        @PathVariable typeCode: String,
        @RequestParam("file") @Schema(type = "string", format = "binary") file: MultipartFile,
        @AuthenticationPrincipal securityUser: SecurityUser?
    ): ResponseEntity<Void> {
        val member: Member = securityUser?.member
            ?: throw ServiceException(HttpStatus.BAD_REQUEST.value(), "올바른 요청이 아닙니다. 로그인 상태를 확인하세요.")

        val question = questionService.findById(questionId)

        // 파일 검사 과정 필요
        checkFileDanger(file)

        questionService.checkActorCanMakeNewGenFile(member, question)

        val filePath = FileUploadUtil.toFile(file, AppConfig.getTempDirPath())

        question.addGenFile(typeCode, filePath)

        questionService.flush()

        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @DeleteMapping("/{questionId}/{fileNo}/{typeCode}")
    @Operation(summary = "삭제")
    fun deleteImageFile(
        @PathVariable questionId: Long,
        @PathVariable fileNo: Int,
        @PathVariable typeCode: String,
        @AuthenticationPrincipal securityUser: SecurityUser?
    ): ResponseEntity<Void> {
        val member: Member = securityUser?.member
            ?: throw ServiceException(HttpStatus.BAD_REQUEST.value(), "올바른 요청이 아닙니다. 로그인 상태를 확인하세요.")

        val question = questionService.findById(questionId)

        question.deleteGenFile(typeCode, fileNo)

        questionService.flush()

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }


    fun checkFileDanger(file : MultipartFile) {
        if (file.size > 10 * 1024 * 1024) { throw ServiceException(HttpStatus.BAD_REQUEST.value(), "파일 크기가 너무 큽니다.") }
        val originalFilename = file.originalFilename
        if (originalFilename == null || !FileUploadUtil.isAllowedFileType(originalFilename, file.contentType)) {
            throw ServiceException(HttpStatus.BAD_REQUEST.value(), "허용되지 않은 파일 형식입니다.")
        }
        if(!FileUploadUtil.checkFileType(file)) { throw ServiceException(HttpStatus.BAD_REQUEST.value(), "허용되지 않은 파일 형식입니다.")}

    }
}