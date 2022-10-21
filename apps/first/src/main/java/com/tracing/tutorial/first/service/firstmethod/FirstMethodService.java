package com.tracing.tutorial.first.service.firstmethod;

import com.tracing.tutorial.first.dto.RequestDto;
import com.tracing.tutorial.first.dto.ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FirstMethodService {

    private final Logger logger = LoggerFactory.getLogger(FirstMethodService.class);

    public FirstMethodService() {}

    public ResponseEntity<ResponseDto> firstMethod(
        RequestDto requestDto
    ) {

        logger.info("Value provided: " + requestDto.getValue());

        var model = new ResponseDto();
        model.setMessage("Succeeded.");
        model.setValue(requestDto.getValue());
        model.setTag(requestDto.getTag());

        var responseDto = new ResponseEntity(model, HttpStatus.OK);

        return responseDto;
    }
}
