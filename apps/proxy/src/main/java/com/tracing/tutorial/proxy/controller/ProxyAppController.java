package com.tracing.tutorial.proxy.controller;

import com.tracing.tutorial.proxy.dto.RequestDto;
import com.tracing.tutorial.proxy.dto.ResponseDto;
import com.tracing.tutorial.proxy.service.fifthmethod.FifthMethodService;
import com.tracing.tutorial.proxy.service.firstmethod.FirstMethodService;
import com.tracing.tutorial.proxy.service.fourthmethod.FourthMethodService;
import com.tracing.tutorial.proxy.service.secondmethod.SecondMethodService;
import com.tracing.tutorial.proxy.service.thirdmethod.ThirdMethodService;
import com.tracing.tutorial.proxy.sixthmethod.SixthMethodService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("proxy")
public class ProxyAppController {

    private final Logger logger = LoggerFactory.getLogger(ProxyAppController.class);

    @Autowired
    private FirstMethodService firstMethodService;

    @Autowired
    private SecondMethodService secondMethodService;

    @Autowired
    private ThirdMethodService thirdMethodService;

    @Autowired
    private FourthMethodService fourthMethodService;

    @Autowired
    private FifthMethodService fifthMethodService;

    @Autowired
    private SixthMethodService sixthMethodService;

    @GetMapping("default")
    public ResponseEntity<String> defaultMethod(
            @RequestParam String name
    ) {
        logger.info("Default method is triggered...");
        logger.info(" -> Name: " + name);

        ResponseEntity<String> responseDto;

        if (name.equalsIgnoreCase("bug")) {
            responseDto = new ResponseEntity<>(
                "Failed",
                HttpStatus.INTERNAL_SERVER_ERROR
            );
            logger.error("Default method is failed.");
        }
        else {
            responseDto = new ResponseEntity<>(
                "Succeeded",
                HttpStatus.OK
            );
        }

        logger.info("Default method is executed.");

        return responseDto;
    }

    @PostMapping("method1")
    public ResponseEntity<ResponseDto> firstMethod(
        @RequestBody RequestDto requestDto
    ) {
        logger.info("First method is triggered...");

        var responseDto = firstMethodService.firstMethod(requestDto);

        logger.info("First method is executed.");

        return responseDto;
    }

    @PostMapping("method2")
    public ResponseEntity<ResponseDto> secondMethod(
            @RequestHeader Map<String, String> headers,
            @RequestBody RequestDto requestDto
    ) {
        logger.info("Second method is triggered...");

        for (var header : headers.entrySet()) {
            logger.info("Key:"+ header.getKey());
            logger.info("Value:"+ header.getValue());
        }

        if (headers.containsKey("x-correlation-id"))
            logger.info("Correlation ID: " + headers.get("x-correlation-id"));

        var responseDto = secondMethodService.secondMethod(requestDto);

        logger.info("Second method is executed.");

        return responseDto;
    }

    @PostMapping("method3")
    public ResponseEntity<ResponseDto> thirdMethod(
        @RequestBody RequestDto requestDto
    ) {
        logger.info("Third method is triggered...");

        var responseDto = thirdMethodService.thirdMethod(requestDto);

        logger.info("Third method is executed.");

        return responseDto;
    }

    @PostMapping("method4")
    public ResponseEntity<ResponseDto> fourthMethod(
            @RequestBody RequestDto requestDto
    ) {
        logger.info("Fourth method is triggered...");

        var responseDto = fourthMethodService.fourthMethod(requestDto);

        logger.info("Fourth method is executed.");

        return responseDto;
    }

    @PostMapping("method5")
    public ResponseEntity<ResponseDto> fifthMethod(
        @RequestBody RequestDto requestDto
    ) {
        logger.info("Fifth method is triggered...");

        var responseDto = fifthMethodService.fifthMethod(requestDto);

        logger.info("Fifth method is executed.");

        return responseDto;
    }

    @PostMapping("method6")
    public ResponseEntity<ResponseDto> sixthMethod(
            @RequestBody RequestDto requestDto
    ) {
        logger.info("Sixth method is triggered...");

        var responseDto = sixthMethodService.sixthMethod(requestDto);

        logger.info("Sixth method is executed.");

        return responseDto;
    }
}
