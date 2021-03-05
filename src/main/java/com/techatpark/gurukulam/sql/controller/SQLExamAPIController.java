package com.techatpark.gurukulam.sql.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techatpark.gurukulam.sql.model.Exam;
import com.techatpark.gurukulam.sql.model.Question;
import com.techatpark.gurukulam.sql.service.AnswerService;
import com.techatpark.gurukulam.sql.service.QuestionService;
import com.techatpark.gurukulam.sql.service.SQLExamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Tag(name = "exams", description = "Resource to manage exams")
@RestController
@RequestMapping("/api/exams/sql")
class SQLExamAPIController {

        private final SQLExamService sqlExamService;
        private final QuestionService questionService;
        private final AnswerService answerService;
        private final ObjectMapper objectMapper;

        SQLExamAPIController(final SQLExamService sqlExamService, final QuestionService questionService,
                        final AnswerService answerService,final ObjectMapper objectMapper) {
                this.sqlExamService = sqlExamService;
                this.questionService = questionService;
                this.answerService = answerService;
                this.objectMapper = objectMapper;
        }

        @Operation(summary = "Creates a new exam", description = "Can be called only by users with 'auth management' rights.")
        @ApiResponses(value = { @ApiResponse(responseCode = "201", description = "exam created successfully"),
                        @ApiResponse(responseCode = "400", description = "exam is invalid") })
        @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseEntity<Optional<Exam>> create(@RequestPart("exam") String examAsJson,
                                                     @RequestPart("scripts") @Valid @NotNull @NotBlank MultipartFile[] scripts) throws IOException {

                Exam exam = objectMapper.readValue(examAsJson,Exam.class);
                InputStream[] sciptStreams = new InputStream[scripts.length];
                for (int i = 0; i < scripts.length; i++) {
                        sciptStreams[i] = scripts[i].getInputStream();
                }
                return ResponseEntity.status(HttpStatus.CREATED).body(sqlExamService.create(exam,
                         sciptStreams));
        }

        @Operation(summary = "Get exam with given id")
        @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "exam"),
                        @ApiResponse(responseCode = "404", description = "exam not found") })
        @GetMapping("/{id}")
        public ResponseEntity<Exam> findById(@PathVariable Integer id) {
                return ResponseEntity.of(sqlExamService.read(id));
        }

        @Operation(summary = "lists all the exam", description = "Can be Invoked by auth users only")
        @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Listing all the exam"),
                        @ApiResponse(responseCode = "204", description = "exam are not available") })
        @GetMapping
        public ResponseEntity<Page<Exam>> findAll(@NotNull final Pageable pageable) {
                Page<Exam> exams = sqlExamService.list(pageable);
                return exams.isEmpty() ? new ResponseEntity<Page<Exam>>(HttpStatus.NO_CONTENT)
                                : ResponseEntity.ok(exams);
        }

        @Operation(summary = "Updates the exam by given id", description = "Can be called only by users with 'auth management' rights.")
        @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "exam updated successfully"),
                        @ApiResponse(responseCode = "400", description = "exam is invalid"),
                        @ApiResponse(responseCode = "404", description = "exam not found") })
        @PutMapping("/{id}")
        public ResponseEntity<Optional<Exam>> update(@PathVariable Integer id, @Valid @RequestBody Exam exam) {
                Optional<Exam> updatedexam = sqlExamService.update(id, exam);
                return updatedexam == null ? new ResponseEntity<Optional<Exam>>(HttpStatus.NOT_FOUND)
                                : ResponseEntity.ok(updatedexam);
        }

        @Operation(summary = "Deletes the exam by given id")
        @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "exam deleted successfully"),
                        @ApiResponse(responseCode = "404", description = "exam not found") })
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteExamById(@PathVariable Integer id) {
                return sqlExamService.delete(id) ? ResponseEntity.ok().build()
                                : new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }

        @Operation(summary = "Creates a new question", description = "Can be called only by users with 'auth management' rights.")
        @ApiResponses(value = { @ApiResponse(responseCode = "201", description = "question created successfully"),
                        @ApiResponse(responseCode = "400", description = "question is invalid") })
        @ResponseStatus(HttpStatus.CREATED)
        @PostMapping("/{examId}/questions")
        public ResponseEntity<Optional<Question>> create(@PathVariable Integer examId,
                        @Valid @RequestBody Question question) {
                return ResponseEntity.status(HttpStatus.CREATED).body(questionService.create(examId, question));
        }

        @Operation(summary = "Get question with given id")
        @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "question"),
                        @ApiResponse(responseCode = "404", description = "question not found") })
        @GetMapping("/{examId}/questions/{id}")
        public ResponseEntity<Question> findQuestionById(@PathVariable Integer id) {
                return ResponseEntity.of(questionService.read(id));
        }

        @Operation(summary = "lists all the questions", description = " Can be invoked by auth users only")
        @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Listing all the questions"),
                        @ApiResponse(responseCode = "204", description = "questions are not available") })
        @GetMapping("/{examId}/questions")
        public ResponseEntity<List<Question>> findAllQuestions(@PathVariable Integer examId) {
                List<Question> questions = questionService.list(examId);
                return questions.isEmpty() ? new ResponseEntity<List<Question>>(HttpStatus.NO_CONTENT)
                                : ResponseEntity.ok(questions);
        }

        @Operation(summary = "Updates the question by given id", description = "Can be called only by users with 'auth management' rights.")
        @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "question updated successfully"),
                        @ApiResponse(responseCode = "400", description = "question is invalid"),
                        @ApiResponse(responseCode = "404", description = "question not found") })
        @PutMapping("/{examId}/questions/{id}")
        public ResponseEntity<Optional<Question>> update(@PathVariable Integer id,
                        @Valid @RequestBody Question question) {
                Optional<Question> updatedQuestion = questionService.update(id, question);
                return updatedQuestion == null ? new ResponseEntity<Optional<Question>>(HttpStatus.NOT_FOUND)
                                : ResponseEntity.ok(updatedQuestion);
        }

        @Operation(summary = "Deletes the question by given id")
        @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "question deleted successfully"),
                        @ApiResponse(responseCode = "404", description = "question not found") })
        @DeleteMapping("/{examId}/questions/{id}")
        public ResponseEntity<Void> delete(@PathVariable Integer id) {
                return questionService.delete(id) ? ResponseEntity.ok().build()
                                : new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }

        @Operation(summary = "Answer a question", description = "Can be called only by users with 'auth management' rights.")
        @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Answered a question successfully"),
                        @ApiResponse(responseCode = "406", description = "Answer is invalid") })
        @ResponseStatus(HttpStatus.ACCEPTED)
        @PostMapping("/{examId}/questions/{questionId}/answer")
        public ResponseEntity<Void> answer(@PathVariable Integer questionId, @RequestBody String answer) {
                return answerService.answer(questionId, answer) ? ResponseEntity.status(HttpStatus.ACCEPTED).build()
                                : ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }

}