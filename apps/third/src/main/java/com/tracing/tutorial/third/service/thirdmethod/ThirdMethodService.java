package com.tracing.tutorial.third.service.thirdmethod;

import com.tracing.tutorial.third.dto.RequestDto;
import com.tracing.tutorial.third.dto.ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ThirdMethodService {

    private final Logger logger = LoggerFactory.getLogger(ThirdMethodService.class);

    public ThirdMethodService() {}

    public ResponseEntity<ResponseDto> thirdMethod(
        RequestDto requestDto
    ) {

        logger.info("Value provided: " + requestDto.getValue());
        logger.info("Tag provided: " + requestDto.getTag());

        var model = new ResponseDto();
        model.setMessage("Succeeded.");
        model.setValue(requestDto.getValue());
        model.setTag(requestDto.getTag());

        var responseDto = new ResponseEntity(model, HttpStatus.OK);

        return responseDto;
    }
}
