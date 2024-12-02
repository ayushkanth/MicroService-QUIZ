package com.ayush.quiz_service.service;

import com.ayush.quiz_service.Feign.QuizInteface;
import com.ayush.quiz_service.model.QuestionWrapper;
import com.ayush.quiz_service.model.Quiz;
import com.ayush.quiz_service.model.Response;
import com.ayush.quiz_service.repository.QuizRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuizService {

    @Autowired
    QuizRepo quizRepo;

    @Autowired
    QuizInteface quizInteface;

    public ResponseEntity<String> createQuiz(String category, int numQ, String title) {
        List<Integer> integers = quizInteface.getQuestionsForQuiz(category,numQ).getBody();
        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestionIds(integers);
        quizRepo.save(quiz);
        return new ResponseEntity<>("success", HttpStatus.CREATED);
    }

    public ResponseEntity<Integer> submitQuiz(Integer id, List<Response> responses) {
        ResponseEntity<Integer> score = quizInteface.getScore(responses);
        return score;
    }

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id) {
        Quiz quiz=quizRepo.findById(id).get();
        List<Integer> questionIds= quiz.getQuestionIds();
        ResponseEntity<List<QuestionWrapper>> questions= quizInteface.getQuestionsFromId(questionIds);
        return questions;
    }
}
