package com.codecool.peermentoringbackend.controller;

import com.codecool.peermentoringbackend.model.Vote;
import com.codecool.peermentoringbackend.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vote")
public class VoteController {

    @Autowired
    private QuestionService questionService;




}
