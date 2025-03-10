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
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Question question;

    private String typeCode;
    private String fileDateDir;
    private String originalFileName;
    private String fileExt;
    private String fileName;
    private int fileSize;
    private String fileExtTypeCode;
    private String fileExtType2Code;
    private String metadata;
    private int fileNo;


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


}
