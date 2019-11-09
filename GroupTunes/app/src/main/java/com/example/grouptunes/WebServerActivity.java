package com.example.grouptunes;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import fi.iki.elonen.NanoHTTPD;

public class WebServerActivity extends NanoHTTPD {
    public static final int PORT = 8765;
    private final String htmlInput;
    private final String cssInput;
    private final String svgInput;

    public WebServerActivity(InputStream htmlInput, InputStream cssInput, InputStream svgInput) throws IOException {
        super(PORT);
        this.htmlInput = IOUtils.toString(htmlInput, Charset.defaultCharset());
        this.cssInput = IOUtils.toString(cssInput, Charset.defaultCharset());
        this.svgInput = IOUtils.toString(svgInput, Charset.defaultCharset());
    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();

        if (uri.equals("/hello")) {
            String response = "HelloWorld";
            return newFixedLengthResponse(response);
        } else if (uri.equals("/index.html")) {
            return newFixedLengthResponse(htmlInput);
        } else if (uri.equals("/style.css")) {
            return newFixedLengthResponse(cssInput);
        } else if (uri.equals("/play.svg")) {
            return  newFixedLengthResponse(svgInput);
        }
        return  null;
    }


}