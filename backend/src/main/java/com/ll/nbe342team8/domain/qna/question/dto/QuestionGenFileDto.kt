package com.ll.nbe342team8.domain.qna.question.dto;

import com.ll.nbe342team8.domain.qna.question.entity.QuestionGenFile;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDateTime;

@Getter
public class QuestionGenFileDto {

    private long id;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    private long postId;

    private String fileName;

    private String typeCode;

    private String fileExtTypeCode;

    private String fileExtType2Code;

    private long fileSize;

    private long fileNo;

    private String fileExt;

    private String fileDateDir;

    private String originalFileName;

    public QuestionGenFileDto(QuestionGenFile questionGenFile) {
        this.id = questionGenFile.getId();
        this.createDate = questionGenFile.getCreateDate();
        this.modifyDate = questionGenFile.getModifyDate();
        this.postId = questionGenFile.getQuestion().getId();
        this.fileName = questionGenFile.getFileName();
        this.typeCode = questionGenFile.getTypeCode();
        this.fileExtTypeCode = questionGenFile.getFileExtTypeCode();
        this.fileExtType2Code = questionGenFile.getFileExtType2Code();
        this.fileSize = questionGenFile.getFileSize();
        this.fileNo = questionGenFile.getFileNo();
        this.fileExt = questionGenFile.getFileExt();
        this.fileDateDir = questionGenFile.getFileDateDir();
        this.originalFileName = questionGenFile.getOriginalFileName();
    }
}