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
import lombok.*;
import org.hibernate.annotations.BatchSize;

@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
@Table(
		name = "question",
		indexes = {
				@Index(name = "idx_question_createDate", columnList = "createDate") // createDate 인덱스 나중에 내림차순 인덱스 mysql에 직접 설정
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
	public Long id;

	@Column(nullable = true) // 질문 제목
	public String title;

	@Column(nullable = true) // 질문 내용
	public String content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false) // 회원이 반드시 존재해야 한다.
	public Member member;

	@BatchSize(size = 10)
	@OneToMany(mappedBy = "question")
	public List<Answer> answers;

	public Boolean isAnswer;

	@OneToMany(mappedBy = "question", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
	@Builder.Default
	public List<QuestionGenFile> genFiles = new ArrayList<>();


	public void updateQuestionInfo(ReqQuestionDto dto) {
		this.title= dto.getTitle();
		this.content=dto.getContent();
	}

	public static Question create(ReqQuestionDto dto,Member member) {
		Question question = Question.builder()
				.title(dto.getTitle())
				.content(dto.getContent())
				.member(member)
				.answers(new ArrayList<>())
				.isAnswer(false)
				.build();

		return question;
	}


	public Boolean getIsAnswer() {
		if(this.isAnswer ==null) {this.isAnswer= false;}
		return  this.isAnswer;
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

		//임시 저장 디렉토리에서 복사해 이동, 임시저장 경로는 주기적으로 초기화
		FileUploadUtil.copy(filePath, genFile.getFilePath());

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
				.filter(genFile -> genFile.getFileNo()==fileNo)
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



}