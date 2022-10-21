package com.tracing.tutorial.fourth.controller;

import com.tracing.tutorial.fourth.dto.RequestDto;
import com.tracing.tutorial.fourth.dto.ResponseDto;
import com.tracing.tutorial.fourth.service.fourthmethod.FourthMethodService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("fourth")
public class FourthAppController {

    private final Logger logger = LoggerFactory.getLogger(FourthAppController.class);

    @Autowired
    private FourthMethodService fourthMethodService;

    @PostMapping
    public ResponseEntity<ResponseDto> fourthMethod(
            @RequestBody RequestDto requestDto
    ) {
        logger.info("Fourth Method is triggered...");

        var responseDto = fourthMethodService.fourthMethod(requestDto);

        logger.info("Fourth Method is executed successfully...");

        return responseDto;
    }
}
