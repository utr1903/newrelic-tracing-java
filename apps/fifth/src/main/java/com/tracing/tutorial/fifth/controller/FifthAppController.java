package com.tracing.tutorial.fifth.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("fifth")
public class FifthAppController {

    private final Logger logger = LoggerFactory.getLogger(FifthAppController.class);

    @GetMapping
    public ResponseEntity<String> checkHealth() {
        logger.info("Health check, OK!");
        return new ResponseEntity<String>("OK!", HttpStatus.OK);
    }
}
