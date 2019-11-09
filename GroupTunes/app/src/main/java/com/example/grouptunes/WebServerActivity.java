package com.example.grouptunes;

import com.android.volley.Response;

import java.io.IOException;

import fi.iki.elonen.NanoHTTPD;

public class WebServerActivity extends NanoHTTPD {
    public static final int PORT = 8765;

    public WebServerActivity() throws IOException {
        super(PORT);
    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();

        if (uri.equals("/hello")) {
            String response = "HelloWorld";
            return newFixedLengthResponse(response);
        }
        return  null;
    }
}