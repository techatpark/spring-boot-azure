package com.techatpark.gurukulam.service;

import com.techatpark.gurukulam.model.Choice;
import com.techatpark.gurukulam.model.Question;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * The type Answer service.
 */
@Service
public class AnswerService {

    /**
     * * this is used to execute a connection with a database.
     */
    private final JdbcTemplate jdbcTemplate;

    /**
     * question Service.
     */
    private final QuestionService questionService;

    /**
     * Constructs Answer Service.
     *
     * @param aJdbcTemplate     the a jdbc template
     * @param anQuestionService the an question service
     */
    AnswerService(final JdbcTemplate aJdbcTemplate,
                  final QuestionService anQuestionService) {
        this.jdbcTemplate = aJdbcTemplate;
        this.questionService = anQuestionService;
    }

    /**
     * checks whether the given answer is correct.returns true if correct.
     *
     * @param questionId the question id
     * @param answer     the answer
     * @return true boolean
     */
    public final Boolean answer(final Integer questionId,
                                final String answer) {
        Boolean isRigntAnswer = false;
        final Optional<Question> oQuestion = questionService.read(questionId);
        if (oQuestion.isPresent()) {
            final Question question = oQuestion.get();
            switch (question.getType()) {
                case "sql":
                    final String verificationSQL =
                            "SELECT COUNT(*) FROM ( " + question.getAnswer()
                                    + " except " + answer
                                    + " ) AS TOTAL_ROWS";
                    final Integer count = jdbcTemplate
                            .queryForObject(verificationSQL, Integer.class);
                    isRigntAnswer = (count == 0);
                    break;
                case "choose-the-best":
                    Optional<Choice> rightChoice = question.getChoices()
                            .stream()
                            .filter(Choice::isAnswer)
                            .findFirst();
                    if (rightChoice.isPresent()) {
                        isRigntAnswer = rightChoice.get()
                                .getId()
                                .toString()
                                .equals(answer);
                    }
                    break;
                default:
                    isRigntAnswer = answer.toLowerCase().equals(
                            question.getAnswer().toLowerCase()
                    );
                    break;
            }

        }
        return isRigntAnswer;
    }

}
