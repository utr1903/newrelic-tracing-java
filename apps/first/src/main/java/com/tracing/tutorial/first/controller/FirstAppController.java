package com.tracing.tutorial.first.controller;

import com.tracing.tutorial.first.dto.RequestDto;
import com.tracing.tutorial.first.dto.ResponseDto;
import com.tracing.tutorial.first.service.firstmethod.FirstMethodService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("first")
public class FirstAppController {

    private final Logger logger = LoggerFactory.getLogger(FirstAppController.class);

    @Autowired
    private FirstMethodService firstMethodService;

    @PostMapping
    public ResponseEntity<ResponseDto> firstMethod(
        @RequestBody RequestDto requestDto
    ) {
        logger.info("First Method is triggered...");

        ResponseEntity<ResponseDto> responseDto =
                firstMethodService.firstMethod(requestDto);

        logger.info("First Method is executed successfully...");

        return responseDto;
    }
}
