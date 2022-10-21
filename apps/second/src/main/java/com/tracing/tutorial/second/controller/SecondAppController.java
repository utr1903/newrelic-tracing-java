package com.tracing.tutorial.second.controller;

import com.tracing.tutorial.second.dto.RequestDto;
import com.tracing.tutorial.second.dto.ResponseDto;
import com.tracing.tutorial.second.service.secondmethod.SecondMethodService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("second")
public class SecondAppController {

    private final Logger logger = LoggerFactory.getLogger(SecondAppController.class);

    @Autowired
    private SecondMethodService secondMethodService;

    @PostMapping
    public ResponseEntity<ResponseDto> secondMethod(
            @RequestHeader Map<String, String> headers,
            @RequestBody RequestDto requestDto
    ) {
        logger.info("Second Method is triggered...");

        for (var header : headers.entrySet()) {
            logger.info("Key:"+ header.getKey());
            logger.info("Value:"+ header.getValue());
        }

        var responseDto = secondMethodService.secondMethod(requestDto);

        logger.info("Second Method is executed successfully...");

        return responseDto;
    }
}
