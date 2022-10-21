package com.tracing.tutorial.zipkinexporter.service.zipkin.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ZipkinTrace {

    private String id;
    private String traceId;
    private String parentId;
    private String name;
    private long timestamp;
    private long duration;
    private String kind;
    private LocalEndpoint localEndpoint;
    private RemoteEndpoint remoteEndpoint;
    private Map<String, String> tags;
}
