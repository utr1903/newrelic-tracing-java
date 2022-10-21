package com.tracing.tutorial.zipkinexporter.service.zipkin.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RemoteEndpoint {

    private String ipv4;
    private int port;
}
