package com.ll.nbe342team8.domain.qna.question.entity;


import com.ll.nbe342team8.global.config.AppConfig;
import com.ll.nbe342team8.global.jpa.entity.BaseTime;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionGenFile extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
    public Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    public Question question;

    public String typeCode;
    public String fileDateDir;
    public String originalFileName;
    public String fileExt;
    public String fileName;
    public int fileSize;
    public String fileExtTypeCode;
    public String fileExtType2Code;
    public String metadata;
    public int fileNo;


    public String getFilePath() {
        return AppConfig.getGenFileDirPath() + "/"  + typeCode + "/" + fileDateDir + "/" + fileName;
    }

    @Override
    public boolean equals(Object o) {
        if (id != null) return super.equals(o);
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        QuestionGenFile that = (QuestionGenFile) o;
        return fileNo == that.fileNo && Objects.equals(typeCode, that.typeCode);
    }
    @Override
    public int hashCode() {
        if (id != null) return super.hashCode();
        return Objects.hash(super.hashCode(), typeCode, fileNo);
    }

    public Long getId() {
        return id;
    }

    public Question getQuestion() {
        return question;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public String getFileDateDir() {
        return fileDateDir;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public String getFileExt() {
        return fileExt;
    }

    public String getFileName() {
        return fileName;
    }

    public int getFileSize() {
        return fileSize;
    }

    public String getFileExtTypeCode() {
        return fileExtTypeCode;
    }

    public String getFileExtType2Code() {
        return fileExtType2Code;
    }

    public String getMetadata() {
        return metadata;
    }

    public int getFileNo() {
        return fileNo;
    }


}
