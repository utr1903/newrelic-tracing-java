package com.tracing.tutorial.proxy.service.thirdmethod;

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
public class ThirdMethodService {

    private final Logger logger = LoggerFactory.getLogger(ThirdMethodService.class);

    @Autowired
    private RestTemplate restTemplate;

    public ThirdMethodService() {}

    public ResponseEntity<ResponseDto> thirdMethod(
        RequestDto requestDto
    ) {

        logger.info("Value provided: " + requestDto.getValue());
        logger.info("Tag provided: " + requestDto.getTag());

        ResponseEntity<ResponseDto> responseDtoFromThirdService;

        try {
            var model = new ResponseDto();

            logger.info("Making a POST request to ThirdService...");

            responseDtoFromThirdService = makeRequestToThirdService(requestDto);

            var statusCode = responseDtoFromThirdService.getStatusCode();
            logger.info("Status code: " + statusCode);
            logger.info("Message: " + responseDtoFromThirdService.getBody().getMessage());

            if (statusCode == HttpStatus.OK) {
                logger.info("Value retrieved: " + responseDtoFromThirdService.getBody().getValue());
                logger.info("Tag retrieved: " + responseDtoFromThirdService.getBody().getTag());

                model.setMessage("Succeeded.");
                model.setValue(responseDtoFromThirdService.getBody().getValue());
                model.setTag(responseDtoFromThirdService.getBody().getTag());
            }
            else
                model.setMessage("Call to ThirdService has failed.");

            logger.info("POST request to ThirdService is executed successfully.");

            return new ResponseEntity(model, statusCode);
        }
        catch (Exception e) {
            logger.error(e.getMessage());

            var model = new ResponseDto();
            model.setMessage(e.getMessage());

            return new ResponseEntity(model, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<ResponseDto> makeRequestToThirdService(
        RequestDto requestDto
    ) {
        var url = "http://third.third.svc.cluster.local:8080/third";

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        var entity = new HttpEntity<>(requestDto, headers);

        return restTemplate.postForEntity(url, entity, ResponseDto.class);
    }
}
