package com.tracing.tutorial.proxy.sixthmethod;

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
public class SixthMethodService {

    private final Logger logger = LoggerFactory.getLogger(SixthMethodService.class);

    @Autowired
    private RestTemplate restTemplate;

    public SixthMethodService() {}

    public ResponseEntity<ResponseDto> sixthMethod(
        RequestDto requestDto
    ) {

        logger.info("Value provided: " + requestDto.getValue());
        logger.info("Tag provided: " + requestDto.getTag());

        ResponseEntity<ResponseDto> responseDtoFromSixthService;

        try {
            var model = new ResponseDto();

            logger.info("Making a POST request to SixthService...");

            responseDtoFromSixthService = makeRequestToSixthService(requestDto);

            var statusCode = responseDtoFromSixthService.getStatusCode();
            logger.info("Status code: " + statusCode);
            logger.info("Message: " + responseDtoFromSixthService.getBody().getMessage());

            if (statusCode == HttpStatus.OK) {
                logger.info("Value retrieved: " + responseDtoFromSixthService.getBody().getValue());
                logger.info("Tag retrieved: " + responseDtoFromSixthService.getBody().getTag());

                model.setMessage("Succeeded.");
                model.setValue(responseDtoFromSixthService.getBody().getValue());
                model.setTag(responseDtoFromSixthService.getBody().getTag());
            }
            else
                model.setMessage("Call to SixthService has failed.");

            logger.info("POST request to SixthService is executed successfully.");

            return new ResponseEntity(model, statusCode);
        }
        catch (Exception e) {
            logger.error(e.getMessage());

            var model = new ResponseDto();
            model.setMessage(e.getMessage());

            return new ResponseEntity(model, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<ResponseDto> makeRequestToSixthService(
        RequestDto requestDto
    ) {
        var url = "http://sixth.sixth.svc.cluster.local:8080/sixth";

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        var entity = new HttpEntity<>(requestDto, headers);

        return restTemplate.postForEntity(url, entity, ResponseDto.class);
    }
}
