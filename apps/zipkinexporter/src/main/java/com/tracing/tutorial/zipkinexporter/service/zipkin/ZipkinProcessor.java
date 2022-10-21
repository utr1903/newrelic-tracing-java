package com.tracing.tutorial.zipkinexporter.service.zipkin;

import com.tracing.tutorial.zipkinexporter.service.zipkin.model.ZipkinTrace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

public class ZipkinProcessor implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(ZipkinProcessor.class);

    private static final int INTERVAL = 5000;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void run(String... args) throws InterruptedException {

        logger.info("Starting to process Zipkin traces...");

        while (true) {

            var allTraces = fetchZipkinTraces();
            for (var traces : allTraces) {
                for (var trace : traces) {
                    logger.info(" -> Trace ID: " + trace.getTraceId());
                    logger.info(" -> Span ID : " + trace.getId());
                }
            }

            if (!allTraces.isEmpty())
                sendZipkinTracesToNewrelic(allTraces);

            logger.info(" -> Waiting for " + INTERVAL + "ms till next fetch...");
            Thread.sleep(INTERVAL);
        }
    }

    private List<List<ZipkinTrace>> fetchZipkinTraces() {

        logger.info("Fetching Zipkin traces...");

        var url = "http://zipkinserver.zipkin.svc.cluster.local:9411/api/v2/traces?lookback="
            + INTERVAL;

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        var response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            new HttpEntity<>(headers),
            new ParameterizedTypeReference<List<List<ZipkinTrace>>>() {}
        );

        logger.info(" -> Zipkin traces are fetched successfully.");

        return response.getBody();
    }

    private void sendZipkinTracesToNewrelic(
        List<List<ZipkinTrace>> allTraces
    ) {

        try {
            logger.info("Sending Zipkin traces to Newrelic...");

            var url = "https://trace-api.eu.newrelic.com/trace/v1";

            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.set("Api-Key", System.getenv("NEWRELIC_LICENSE_KEY"));
            headers.set("Data-Format", "zipkin");
            headers.set("Data-Format-Version", "2");

            for (var traces : allTraces) {

                var traceId = traces.get(0).getTags().containsKey("existingTraceId") ?
                    traces.get(0).getTags().get("existingTraceId") :
                    traces.get(0).getTraceId();

                traces.forEach(x -> x.setTraceId(traceId));

                logger.info(" -> Sending trace with trace ID: " + traceId + "...");

                var entity = new HttpEntity<>(traces, headers);

                var response = restTemplate.postForEntity(url, entity, String.class);

                logger.info(" -> Status Code: " + response.getStatusCode());

                if (response.getStatusCode() == HttpStatus.ACCEPTED) {
                    logger.info(" -> Zipkin traces are sent successfully.");
                    logger.info("Response: " + response.getBody());
                }
                else {
                    logger.error(" -> Request is unsuccessful. Zipkin traces could not be sent.");
                    logger.error("Error: " + response.getBody());
                }
            }
        }
        catch (Exception e) {
            logger.error(" -> Unexpected error occurred. Zipkin traces could not be sent.");
            logger.error("Error: " + e.getMessage());
        }
    }
}
