package com.tracing.tutorial.fifth.service.fifthmethod;

import com.newrelic.api.agent.Trace;
import com.tracing.tutorial.fifth.config.Constants;
import com.tracing.tutorial.fifth.service.newrelic.NewRelicTracer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class FifthMethodService {

    private final Logger logger = LoggerFactory.getLogger(FifthMethodService.class);

    @Autowired
    private NewRelicTracer newRelicTracer;

    public FifthMethodService() {}

    @Trace(dispatcher = true)
    @KafkaListener(topics = Constants.TOPIC, groupId = Constants.GROUP_ID)
    public void listen(
        ConsumerRecord<String, String> record
    )
    {
        newRelicTracer.track(record);

        logger.info("Message tag  : " + record.key());
        logger.info("Message value: " + record.value());
    }
}
