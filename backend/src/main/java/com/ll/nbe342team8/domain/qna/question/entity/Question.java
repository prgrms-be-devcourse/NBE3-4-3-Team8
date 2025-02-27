package com.ll.nbe342team8.domain.qna.question.entity;

import java.util.*;
import java.util.stream.Collectors;

import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.qna.answer.entity.Answer;
import com.ll.nbe342team8.domain.qna.question.dto.ReqQuestionDto;
import com.ll.nbe342team8.global.exceptions.ServiceException;
import com.ll.nbe342team8.global.jpa.entity.BaseTime;
import com.ll.nbe342team8.standard.util.Ut;

import com.ll.nbe342team8.standard.util.fileuploadutil.FileUploadUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
		name = "question",
		indexes = {
				@Index(name = "idx_question_createDate", columnList = "createDate") // createDate 인덱스 추가
		},
		uniqueConstraints = {
				@UniqueConstraint(
						name = "unique_question",
						columnNames = {"member_id", "title", "content", "create_date"} // 유니크 제약 조건 적용
				)
		} // 거의 같은 시간에 같은 질문이 2개 등록되는 경우 방지
)
public class Question extends BaseTime {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
	private Long id;

	@Column(nullable = true) // 질문 제목
	private String title;

	@Column(nullable = true) // 질문 내용
	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false) // 회원이 반드시 존재해야 한다.
	private Member member;

	@BatchSize(size = 10)
	@OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
	private List<Answer> answers;

	private Boolean isAnswer;

	@OneToMany(mappedBy = "question", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
	@Builder.Default
	private List<QuestionGenFile> genFiles = new ArrayList<>();


	public void updateQuestionInfo(ReqQuestionDto dto) {
      this.title= dto.title();
      this.content=dto.content();
  	}

  	public static Question create(ReqQuestionDto dto,Member member) {
		Question question = Question.builder()
				.title(dto.title())
				.content(dto.content())
				.member(member)
				.build();

		return question;
  	}

	public void addAnswer(Answer answer) {
		this.answers.add(answer);
		if (answer.getQuestion() != this) {
			answer.setQuestion(this);
		}
	}

	public void removeAnswer(Answer answer) {
		this.answers.remove(answer);
		answer.setQuestion(null);
	}

    public QuestionGenFile addGenFile(String typeCode, String filePath) {
        return addGenFile(typeCode, 0, filePath);
    }

    private QuestionGenFile addGenFile(String typeCode, int fileNo, String filePath){

		String originalFileName = FileUploadUtil.getOriginalFileName(filePath);
		String fileExt = FileUploadUtil.getFileExt(filePath);
		String fileName = UUID.randomUUID() + "." + fileExt;
		int fileSize = FileUploadUtil.getFileSize(filePath);
        fileNo = fileNo == 0 ? getNextGenFileNo(typeCode) : fileNo;
		String fileExtTypeCode = FileUploadUtil.getFileExtTypeCodeFromFileExt(fileExt);
		String fileExtType2Code = FileUploadUtil.getFileExtType2CodeFromFileExt(fileExt);

		Map<String, Object> metadata = FileUploadUtil.getMetadata(filePath);
		String metadataStr = metadata
				.entrySet()
				.stream()
				.map(entry -> entry.getKey() + "-" + entry.getValue())
				.collect(Collectors.joining(";"));

		QuestionGenFile genFile = QuestionGenFile.builder()
				.question(this)
				.typeCode(typeCode)
				.fileNo(fileNo)
				.originalFileName(originalFileName)
				.fileDateDir(Ut.date.getCurrentDateFormatted("yyyy_MM_dd"))
				.fileExtTypeCode(fileExtTypeCode)
				.fileExtType2Code(fileExtType2Code)
				.metadata(metadataStr)
				.fileExt(fileExt)
				.fileName(fileName)
				.fileSize(fileSize)
				.build();
		genFiles.add(genFile);

		FileUploadUtil.mv(filePath, genFile.getFilePath());

		return genFile;
	}

	private int getNextGenFileNo(String typeCode) {
		return genFiles.stream()
				.filter(genFile -> genFile.getTypeCode().equals(typeCode))
				.mapToInt(QuestionGenFile::getFileNo)
				.max()
				.orElse(0) + 1;
	}

	public Optional<QuestionGenFile> getGenFileByTypeCodeAndFileNo(String typeCode, int fileNo) {
		return genFiles.stream()
				.filter(genFile -> genFile.getTypeCode().equals(typeCode))
				.filter(genFile -> genFile.getFileNo() == fileNo)
				.findFirst();
	}

	public void deleteGenFile(String typeCode, int fileNo) {
		getGenFileByTypeCodeAndFileNo(typeCode, fileNo)
				.ifPresent(genFile -> {
					String filePath = genFile.getFilePath();
					genFiles.remove(genFile);
					FileUploadUtil.rm(filePath);
				});
	}

	public void modifyGenFile(String typeCode, int fileNo, String filePath) {
		getGenFileByTypeCodeAndFileNo(
				typeCode,
				fileNo
		)
				.ifPresent(genFile -> {
					FileUploadUtil.rm(genFile.getFilePath());
					String originalFileName = FileUploadUtil.getOriginalFileName(filePath);
					String fileExt = FileUploadUtil.getFileExt(filePath);
					String fileExtTypeCode = FileUploadUtil.getFileExtTypeCodeFromFileExt(fileExt);
					String fileExtType2Code = FileUploadUtil.getFileExtType2CodeFromFileExt(fileExt);
					Map<String, Object> metadata = FileUploadUtil.getMetadata(filePath);
					String metadataStr = metadata
							.entrySet()
							.stream()
							.map(entry -> entry.getKey() + "-" + entry.getValue())
							.collect(Collectors.joining(";"));
					String fileName = UUID.randomUUID() + "." + fileExt;
					int fileSize = FileUploadUtil.getFileSize(filePath);
					genFile.setOriginalFileName(originalFileName);
					genFile.setMetadata(metadataStr);
					genFile.setFileDateDir(Ut.date.getCurrentDateFormatted("yyyy_MM_dd"));
					genFile.setFileExt(fileExt);
					genFile.setFileExtTypeCode(fileExtTypeCode);
					genFile.setFileExtType2Code(fileExtType2Code);
					genFile.setFileName(fileName);
					genFile.setFileSize(fileSize);
					FileUploadUtil.mv(filePath, genFile.getFilePath());
				});
	}

    public void putGenFile(String typeCode, int fileNo, String filePath) {
        Optional<QuestionGenFile> opQuestionGenFile = getGenFileByTypeCodeAndFileNo(
                typeCode,
                fileNo
        );

        if (opQuestionGenFile.isPresent()) {
            modifyGenFile(typeCode, fileNo, filePath);
        } else {
            addGenFile(typeCode, fileNo, filePath);
        }
    }

	public Optional<QuestionGenFile> getGenFileById(Long id) {
		return genFiles.stream()
				.filter(genFile -> genFile.getId().equals(id))
				.findFirst();
	}




}
