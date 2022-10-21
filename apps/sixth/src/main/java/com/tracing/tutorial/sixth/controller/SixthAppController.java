package com.tracing.tutorial.sixth.controller;

import com.tracing.tutorial.sixth.dto.RequestDto;
import com.tracing.tutorial.sixth.dto.ResponseDto;
import com.tracing.tutorial.sixth.service.sixthmethod.SixthMethodService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("sixth")
public class SixthAppController {

    private final Logger logger = LoggerFactory.getLogger(SixthAppController.class);

    @Autowired
    private SixthMethodService sixthMethodService;

    @PostMapping
    public ResponseEntity<ResponseDto> sixthMethod(
        @RequestBody RequestDto requestDto
    ) {
        logger.info("Sixth Method is triggered...");

        ResponseEntity<ResponseDto> responseDto =
                sixthMethodService.sixthMethod(requestDto);

        logger.info("Sixth Method is executed successfully...");

        return responseDto;
    }
}
