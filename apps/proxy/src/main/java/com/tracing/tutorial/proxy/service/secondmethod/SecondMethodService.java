package com.tracing.tutorial.proxy.service.secondmethod;

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
public class SecondMethodService {

    private final Logger logger = LoggerFactory.getLogger(SecondMethodService.class);

    @Autowired
    private RestTemplate restTemplate;

    public SecondMethodService() {}

    public ResponseEntity<ResponseDto> secondMethod(
        RequestDto requestDto
    ) {

        logger.info("Value provided: " + requestDto.getValue());
        logger.info("Tag provided: " + requestDto.getTag());

        ResponseEntity<ResponseDto> responseDtoFromSecondService;

        try {
            var model = new ResponseDto();

            logger.info("Making a POST request to SecondService...");

            responseDtoFromSecondService = makeRequestToSecondService(requestDto);

            var statusCode = responseDtoFromSecondService.getStatusCode();
            logger.info("Status code: " + statusCode);
            logger.info("Message: " + responseDtoFromSecondService.getBody().getMessage());

            if (statusCode == HttpStatus.OK) {
                logger.info("Value retrieved: " + responseDtoFromSecondService.getBody().getValue());
                logger.info("Tag retrieved: " + responseDtoFromSecondService.getBody().getTag());

                model.setMessage("Succeeded.");
                model.setValue(responseDtoFromSecondService.getBody().getValue());
                model.setTag(responseDtoFromSecondService.getBody().getTag());
            }
            else
                model.setMessage("Call to SecondService has failed.");

            logger.info("POST request to SecondService is executed successfully.");

            return new ResponseEntity(model, statusCode);
        }
        catch (Exception e) {
            logger.error(e.getMessage());

            var model = new ResponseDto();
            model.setMessage(e.getMessage());

            return new ResponseEntity(model, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<ResponseDto> makeRequestToSecondService(
        RequestDto requestDto
    ) {
        var url = "http://second.second.svc.cluster.local:8080/second";

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        var entity = new HttpEntity<>(requestDto, headers);

        return restTemplate.postForEntity(url, entity, ResponseDto.class);
    }
}
