package com.ll.nbe342team8.domain.qna.question.controller

import com.ll.nbe342team8.domain.member.member.entity.Member
import com.ll.nbe342team8.domain.member.member.service.MemberService
import com.ll.nbe342team8.domain.oauth.SecurityUser
import com.ll.nbe342team8.domain.qna.question.entity.Question
import com.ll.nbe342team8.domain.qna.question.service.QuestionService
import com.ll.nbe342team8.global.config.AppConfig
import com.ll.nbe342team8.global.exceptions.ServiceException
import com.ll.nbe342team8.standard.util.fileuploadutil.FileUploadUtil
import com.ll.nbe342team8.standard.util.fileuploadutil.FileUploadUtil.isAllowedFileType
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
    private val memberService: MemberService
    ) {


    @GetMapping("/download/{questionId}/{fileNo}")
    @Operation(summary = "이미지 전송")
    fun loadImage(
        @PathVariable questionId: Long,
        @PathVariable fileNo: Int,
        request: HttpServletRequest
    ): ResponseEntity<Resource> {

        val authentication = SecurityContextHolder.getContext().authentication
        val securityUser = authentication?.principal as? SecurityUser
            ?: throw ServiceException(HttpStatus.UNAUTHORIZED.value(), "로그인을 해야 합니다.")

        val member = memberService.findByOauthId(securityUser.member.oAuthId)
            .orElseThrow { ServiceException(HttpStatus.NOT_FOUND.value(), "사용자를 찾을 수 없습니다.") }

        val question = questionService.findById(questionId).orElseThrow { NoSuchElementException("질문을 찾을 수 없습니다") }

        val genFile = question.genFiles
            .find { it.fileNo == fileNo }
            ?: throw NoSuchElementException("File not found")

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
        @RequestParam("file") @Schema(type = "string", format = "binary") file: MultipartFile
    ): ResponseEntity<Void> {
        val authentication = SecurityContextHolder.getContext().authentication
        val securityUser = authentication?.principal as? SecurityUser
            ?: throw ServiceException(HttpStatus.UNAUTHORIZED.value(), "로그인을 해야 합니다.")

        val member = memberService.findByOauthId(securityUser.member.oAuthId)
            .orElseThrow { ServiceException(HttpStatus.NOT_FOUND.value(), "사용자를 찾을 수 없습니다.") }

        val question = questionService.findById(questionId)
            .orElseThrow { ServiceException(HttpStatus.NOT_FOUND.value(), "질문을 찾을 수 없습니다.") }

        // 파일 검사 과정 필요


        checkActorCanMakeNewGenFile(member, question)

        val filePath = FileUploadUtil.toFile(file, AppConfig.getTempDirPath())

        val postGenFile = question.addGenFile(typeCode, filePath)

        questionService.flush()

        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @DeleteMapping("/{questionId}/{fileNo}/{typeCode}")
    @Operation(summary = "삭제")
    fun deleteNewFile(
        @PathVariable questionId: Long,
        @PathVariable fileNo: Int,
        @PathVariable typeCode: String
    ): ResponseEntity<Void> {
        val authentication = SecurityContextHolder.getContext().authentication
        val securityUser = authentication?.principal as? SecurityUser
            ?: throw ServiceException(HttpStatus.UNAUTHORIZED.value(), "로그인을 해야 합니다.")

        val member = memberService.findByOauthId(securityUser.member.oAuthId)
            .orElseThrow { ServiceException(HttpStatus.NOT_FOUND.value(), "사용자를 찾을 수 없습니다.") }

        val question = questionService.findById(questionId)
            .orElseThrow { ServiceException(HttpStatus.NOT_FOUND.value(), "질문을 찾을 수 없습니다.") }

        question.deleteGenFile(typeCode, fileNo)

        questionService.flush()

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }


    fun checkActorCanMakeNewGenFile(member: Member?, question: Question) {
        if (member == null) throw ServiceException(404, "로그인 후 이용해주세요.")
        if (member.id !== question.member.id) throw ServiceException(404, "게시글 작성 유저만 업로드 할 수 있습니다.")
        if (question.genFiles.size >= 5) {
            throw ServiceException(HttpStatus.CONFLICT.value(), "질문 하나에 이미지는 5개까지 설정할수있습니다.")
        }
    }

    fun checkFileDanger(file : MultipartFile) {
        if (file.size > 10 * 1024 * 1024) { throw ServiceException(HttpStatus.BAD_REQUEST.value(), "파일 크기가 너무 큽니다.") }
        val originalFilename = file.originalFilename
        if (originalFilename == null || !FileUploadUtil.isAllowedFileType(originalFilename, file.contentType)) {
            throw ServiceException(HttpStatus.BAD_REQUEST.value(), "허용되지 않은 파일 형식입니다.")
        }

    }
}