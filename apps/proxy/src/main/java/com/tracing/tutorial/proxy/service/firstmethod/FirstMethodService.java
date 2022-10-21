package com.tracing.tutorial.proxy.service.firstmethod;

import com.tracing.tutorial.proxy.dto.RequestDto;
import com.tracing.tutorial.proxy.dto.ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
public class FirstMethodService {

    private final Logger logger = LoggerFactory.getLogger(FirstMethodService.class);

    @Autowired
    private RestTemplate restTemplate;

    public FirstMethodService() {}

    public ResponseEntity<ResponseDto> firstMethod(
        RequestDto requestDto
    ) {

        logger.info("Value provided: " + requestDto.getValue());
        logger.info("Tag provided: " + requestDto.getTag());

        ResponseEntity<ResponseDto> responseDtoFromFirstService;

        try {
            var model = new ResponseDto();

            logger.info("Making a POST request to FirstService...");

            responseDtoFromFirstService = makeRequestToFirstService(requestDto);

            var statusCode = responseDtoFromFirstService.getStatusCode();
            logger.info("Status code: " + statusCode);
            logger.info("Message: " + responseDtoFromFirstService.getBody().getMessage());

            if (statusCode == HttpStatus.OK) {
                logger.info("Value retrieved: " + responseDtoFromFirstService.getBody().getValue());
                logger.info("Tag retrieved: " + responseDtoFromFirstService.getBody().getTag());

                model.setMessage("Succeeded.");
                model.setValue(responseDtoFromFirstService.getBody().getValue());
                model.setTag(responseDtoFromFirstService.getBody().getTag());
            }
            else
                model.setMessage("Call to FirstService has failed.");

            logger.info("POST request to FirstService is executed successfully.");

            return new ResponseEntity(model, statusCode);
        }
        catch (Exception e) {
            logger.error(e.getMessage());

            var model = new ResponseDto();
            model.setMessage(e.getMessage());

            return new ResponseEntity(model, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<ResponseDto> makeRequestToFirstService(
        RequestDto requestDto
    ) {
        var url = "http://first.first.svc.cluster.local:8080/first";

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        var entity = new HttpEntity<>(requestDto, headers);

        return restTemplate.postForEntity(url, entity, ResponseDto.class);
    }
}
