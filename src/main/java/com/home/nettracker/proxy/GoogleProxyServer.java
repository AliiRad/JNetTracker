package com.home.nettracker.proxy;

import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.littleshoot.proxy.HttpFiltersAdapter;
import org.littleshoot.proxy.HttpFiltersSourceAdapter;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Component
public class GoogleProxyServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleProxyServer.class);
    private static final String GOOGLE_DOMAIN = "google";

    private DefaultHttpProxyServer server;

    @PostConstruct
    public void startProxy() {
        server = (DefaultHttpProxyServer) DefaultHttpProxyServer.bootstrap()
                .withPort(8888)  // Proxy server will run on port 8888
                .withFiltersSource(new HttpFiltersSourceAdapter() {
                    @Override
                    public HttpFiltersAdapter filterRequest(HttpRequest originalRequest) {
                        return new HttpFiltersAdapter(originalRequest) {
                            @Override
                            public io.netty.handler.codec.http.HttpResponse clientToProxyRequest(
                                    HttpObject httpObject) {
                                // Log requests that contain "google" in the URI
                                if (originalRequest.uri().contains(GOOGLE_DOMAIN)) {
                                    LOGGER.info("Request to Google:");
                                    LOGGER.info("URI: " + originalRequest.uri());
                                    LOGGER.info("Method: " + originalRequest.method());
                                    LOGGER.info("Headers: " + originalRequest.headers());
                                }
                                return null;
                            }

                            @Override
                            public HttpObject serverToProxyResponse(
                                    HttpObject httpObject) {
                                if (originalRequest.uri().contains(GOOGLE_DOMAIN) &&
                                        httpObject instanceof io.netty.handler.codec.http.HttpResponse) {

                                    // Log response from Google
                                    io.netty.handler.codec.http.HttpResponse response =
                                            (io.netty.handler.codec.http.HttpResponse) httpObject;
                                    LOGGER.info("Response from Google:");
                                    LOGGER.info("Status Code: " + response.status());
                                    LOGGER.info("Headers: " + response.headers());
                                }
                                return httpObject;
                            }
                        };
                    }
                }).start();
        LOGGER.info("Proxy server started on port 8888");
    }

    @PreDestroy
    public void stopProxy() {
        if (server != null) {
            server.stop();
            LOGGER.info("Proxy server stopped");
        }
    }
}