package com.tracing.tutorial.proxy.service.fifthmethod;

import com.newrelic.api.agent.Trace;
import com.tracing.tutorial.proxy.config.Constants;
import com.tracing.tutorial.proxy.dto.RequestDto;
import com.tracing.tutorial.proxy.dto.ResponseDto;
import com.tracing.tutorial.proxy.service.newrelic.NewRelicTracer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class FifthMethodService {

    private final Logger logger = LoggerFactory.getLogger(FifthMethodService.class);

    @Autowired
    private KafkaProducer<String, String> producer;

    @Autowired
    private NewRelicTracer newRelicTracer;

    public FifthMethodService() {}

    @Trace(dispatcher = true)
    public ResponseEntity<ResponseDto> fifthMethod(
        RequestDto requestDto
    )
    {
        // Create Kafka producer record
        var record = createProducerRecord(requestDto);

        // Send record to topic
        producer.send(record, (recordMetadata, e) -> {
            if (e == null)
                logger.info("Record is successfully sent to topic.");
            else
                logger.error("Record is failed to be sent to topic: "
                        + e.getMessage());
        });

        producer.flush();

        return createResponseDto(requestDto);
    }

    private ProducerRecord<String, String> createProducerRecord(
            RequestDto requestDto
    ) {
        var record = new ProducerRecord<>(
            Constants.TOPIC,
            requestDto.getTag(),
            requestDto.getValue()
        );

        newRelicTracer.track(record);

        return record;
    }

    private ResponseEntity<ResponseDto> createResponseDto(
        RequestDto requestDto
    ) {
        logger.info("Request is successfully processed.");

        var model = new ResponseDto();

        model.setMessage("Succeeded.");
        model.setValue(requestDto.getValue());
        model.setTag(requestDto.getTag());

        return new ResponseEntity<>(model, HttpStatus.OK);
    }
}
