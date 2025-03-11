package com.ll.nbe342team8.domain.qna.question.dto

import com.ll.nbe342team8.domain.qna.question.entity.QuestionGenFile
import lombok.Getter
import java.time.LocalDateTime

data class QuestionGenFileDto(
    val id: Long,
    val createDate: LocalDateTime,
    val modifyDate: LocalDateTime,
    val postId: Long,
    val fileName: String,
    val typeCode: String,
    val fileExtTypeCode: String,
    val fileExtType2Code: String,
    val fileSize: Long,
    val fileNo: Long,
    val fileExt: String,
    val fileDateDir: String,
    val originalFileName: String
) {
    // 보조 생성자 추가
    constructor(questionGenFile: QuestionGenFile) : this(
        id = questionGenFile.id,
        createDate = questionGenFile.createDate,
        modifyDate = questionGenFile.modifyDate,
        postId = questionGenFile.question.id,
        fileName = questionGenFile.fileName,
        typeCode = questionGenFile.typeCode,
        fileExtTypeCode = questionGenFile.fileExtTypeCode,
        fileExtType2Code = questionGenFile.fileExtType2Code,
        fileSize = questionGenFile.fileSize.toLong(),
        fileNo = questionGenFile.fileNo.toLong(),
        fileExt = questionGenFile.fileExt,
        fileDateDir = questionGenFile.fileDateDir,
        originalFileName = questionGenFile.originalFileName
    )
}