package com.codecool.peermentoringbackend.controller;


import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class SearchController {

    @RequestMapping(value = "/search/{urlParam}", method = GET)
    @ResponseBody
    public String search( @PathVariable String urlParam) {
        String[] splitted = urlParam.split("\\s+");
        String joinedString = String.join(" AND ", splitted);
        return "helo";
    }



}
