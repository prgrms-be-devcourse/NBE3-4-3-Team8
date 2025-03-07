package com.ll.nbe342team8.domain.qna.question.repository

import com.ll.nbe342team8.domain.qna.question.entity.Question
import com.ll.nbe342team8.domain.qna.question.entity.QuestionGenFile
import org.springframework.data.jpa.repository.JpaRepository

interface QuestionGenFileRepository : JpaRepository<QuestionGenFile, Long> {
}