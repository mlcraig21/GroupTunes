package com.example.grouptunes;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import fi.iki.elonen.NanoFileUpload;
import fi.iki.elonen.NanoHTTPD;

public class WebServerActivity extends NanoHTTPD {
    public static final int PORT = 8765;
    private final Context context;
    private final String htmlInput;
    private final String cssInput;
    private final String svgInput;
    private ArrayList<Song> queue = new ArrayList<>();

    public WebServerActivity(Context context, InputStream htmlInput, InputStream cssInput, InputStream svgInput) throws IOException {
        super(PORT);
        this.context = context;
        this.htmlInput = IOUtils.toString(htmlInput, Charset.defaultCharset());
        this.cssInput = IOUtils.toString(cssInput, Charset.defaultCharset());
        this.svgInput = IOUtils.toString(svgInput, Charset.defaultCharset());

    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();

        Log.d("hi","hi from "+ session.getMethod());
        if (uri.equals("/hello")) {
            String response = "HelloWorld";
            return newFixedLengthResponse(response);
        } else if (uri.equals("/index.html")) {
            return newFixedLengthResponse(htmlInput);
        } else if (uri.equals("/style.css")) {
            return newFixedLengthResponse(cssInput);
        } else if (uri.equals("/play.svg")) {
            return newFixedLengthResponse(svgInput);
        }
        if (session.getMethod() == Method.GET) {
            return newFixedLengthResponse(generateJSON(session));
        } else if (session.getMethod() == Method.POST) {
            Log.d("hi", "hi from post");
            uri = session.getUri();
            Log.d("hi", uri);
            if (uri.equals(("/indexupload.html"))) {
                Log.d("hi", "hi from post");
                return newFixedLengthResponse(upload(session));
            }
        }
        return newFixedLengthResponse(htmlInput);
    }

    private String generateJSON(IHTTPSession session) {
        String uri = session.getUri();
        if (uri.equals(("/playlist.json"))) {
            Log.d("hi", "hi from get");
            String json = new Gson().toJson(queue);
            Log.d(json, json);
            Log.d("hi","hi from json");

            return json;
        }

        return htmlInput;
    }


    public String upload(IHTTPSession session) {
        try {
            List<FileItem> files = new NanoFileUpload(new DiskFileItemFactory()).parseRequest(session);
            File yourFile = null;
            Log.d("hi","hi from upload");
            for (FileItem file : files) {

                //create folder
                try {
                    String fileName = file.getName();
                    File pFile = new File(context.getFilesDir(), "mydir");
                    if (!pFile.exists()) {
                        pFile.mkdir();
                    }
                    //upload file
                    File gpxfile = new File(pFile, fileName);
                    FileWriter writer = new FileWriter(gpxfile);
                    writer.append(file.getString());
                    writer.flush();
                    writer.close();
                    addToQueue(fileName);
                    //accessFile
                    String yourFilePath = context.getFilesDir() + "/" + "index.html";
                    yourFile = new File(yourFilePath + yourFile.toString());
                    Log.d("hi","hi from upload");

                } catch (Exception exception) {
                    Log.d("hi","hi from uploaderror");
                    return htmlInput;
                    //return newFixedLengthResponse(Response.Status.NOT_FOUND, MIME_PLAINTEXT,
                    //         exception.getMessage());
                }
            }

            return htmlInput;

            //return newFixedLengthResponse(Response.Status.OK, MIME_PLAINTEXT,
            //      "Uploaded files " + " out of " +context.getFilesDir()+yourFile.toString());
        } catch (FileUploadException e) {
            return htmlInput;
            // throw new IllegalArgumentException("Could not handle files from API request", e);
        }
    }

    private void addToQueue(String fileName) {
        String name = fileName;
        Duration duration = getDuration(fileName);
        Song s = new Song(name,duration);
        queue.add(s);
        Log.d("hi","hi from upload");
    }

    private Duration getDuration(String fileName) {
        String pathStr = context.getFilesDir() + File.separator + fileName;
        Uri uri = Uri.parse(pathStr);
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(context.getApplicationContext(),uri);
        String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        int millSecond = Integer.parseInt(durationStr);
        return Duration.ofMillis(millSecond);
    }
}