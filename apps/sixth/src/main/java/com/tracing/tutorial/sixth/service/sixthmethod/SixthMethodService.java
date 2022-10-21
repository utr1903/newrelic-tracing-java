package com.tracing.tutorial.sixth.service.sixthmethod;

import com.tracing.tutorial.sixth.dto.RequestDto;
import com.tracing.tutorial.sixth.dto.ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SixthMethodService {

    private final Logger logger = LoggerFactory.getLogger(SixthMethodService.class);

    public SixthMethodService() {}

    public ResponseEntity<ResponseDto> sixthMethod(
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
