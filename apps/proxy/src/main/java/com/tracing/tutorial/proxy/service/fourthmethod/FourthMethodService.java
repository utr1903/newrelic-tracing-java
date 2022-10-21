package com.tracing.tutorial.proxy.service.fourthmethod;

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
public class FourthMethodService {

    private final Logger logger = LoggerFactory.getLogger(FourthMethodService.class);

    @Autowired
    private RestTemplate restTemplate;

    public FourthMethodService() {}

    public ResponseEntity<ResponseDto> fourthMethod(
            RequestDto requestDto
    ) {

        logger.info("Value provided: " + requestDto.getValue());
        logger.info("Tag provided: " + requestDto.getTag());

        ResponseEntity<ResponseDto> responseDtoFromFourthService;

        try {
            var model = new ResponseDto();

            logger.info("Making a POST request to FourthService...");

            responseDtoFromFourthService = makeRequestToFourthService(requestDto);

            var statusCode = responseDtoFromFourthService.getStatusCode();
            logger.info("Status code: " + statusCode);
            logger.info("Message: " + responseDtoFromFourthService.getBody().getMessage());

            if (statusCode == HttpStatus.OK) {
                logger.info("Value retrieved: " + responseDtoFromFourthService.getBody().getValue());
                logger.info("Tag retrieved: " + responseDtoFromFourthService.getBody().getTag());

                model.setMessage("Succeeded.");
                model.setValue(responseDtoFromFourthService.getBody().getValue());
                model.setTag(responseDtoFromFourthService.getBody().getTag());
            }
            else
                model.setMessage("Call to FourthService has failed.");

            logger.info("POST request to FourthService is executed successfully.");

            return new ResponseEntity(model, statusCode);
        }
        catch (Exception e) {
            logger.error(e.getMessage());

            var model = new ResponseDto();
            model.setMessage(e.getMessage());

            return new ResponseEntity(model, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<ResponseDto> makeRequestToFourthService(
            RequestDto requestDto
    ) {
        var url = "http://fourth.fourth.svc.cluster.local:80/fourth";

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        var entity = new HttpEntity<>(requestDto, headers);

        return restTemplate.postForEntity(url, entity, ResponseDto.class);
    }
}
