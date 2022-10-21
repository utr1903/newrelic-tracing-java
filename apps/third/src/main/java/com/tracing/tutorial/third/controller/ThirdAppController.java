package com.tracing.tutorial.third.controller;

import brave.Tracer;
import com.tracing.tutorial.third.dto.RequestDto;
import com.tracing.tutorial.third.dto.ResponseDto;
import com.tracing.tutorial.third.service.thirdmethod.ThirdMethodService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("third")
public class ThirdAppController {

    private final Logger logger = LoggerFactory.getLogger(ThirdAppController.class);

    @Autowired
    private ThirdMethodService firstMethodService;

    @Autowired
    private Tracer tracer;

    @PostMapping
    public ResponseEntity<ResponseDto> thirdMethod(
            @RequestHeader Map<String, String> headers,
            @RequestBody RequestDto requestDto
    ) {
        logger.info("Third Method is triggered...");

        if (headers.containsKey("traceparent")) {
            logger.info("Trace ID already exists. Tagging...");

            var traceId = headers
                .get("traceparent")
                .trim()
                .split("-")[1];

            tracer.currentSpanCustomizer()
                .tag("existingTraceId", traceId);
        }

        var responseDto =
            firstMethodService.thirdMethod(requestDto);

        logger.info("Third Method is executed successfully.");

        return responseDto;
    }
}