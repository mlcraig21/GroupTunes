package com.example.grouptunes;

import android.app.Activity;
import android.content.Context;

import com.android.volley.Response;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.iki.elonen.NanoFileUpload;
import fi.iki.elonen.NanoHTTPD;

public class WebServerActivity extends NanoHTTPD {
    public static final int PORT = 8765;
    private final Context context;
    private final String htmlInput;
    private final String cssInput;
    private final String svgInput;
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

        if (session.getMethod() == Method.POST) {
            try {
                List<FileItem> files = new NanoFileUpload(new DiskFileItemFactory()).parseRequest(session);
                File yourFile = null;
                for (FileItem file : files) {

                    //create folder
                    try {
                        String fileName = file.getName();
                        File pFile = new File(context.getFilesDir(),"mydir");
                        if(!pFile.exists()){
                            pFile.mkdir();
                        }
                        //upload file
                        File gpxfile = new File(pFile, fileName);
                        FileWriter writer = new FileWriter(gpxfile);
                        writer.append(file.getString());
                        writer.flush();
                        writer.close();

                        //accessFile
                        String yourFilePath = context.getFilesDir() + "/" + "index.html";
                        yourFile = new File( yourFilePath + yourFile.toString());


                    } catch (Exception exception) {
                        return newFixedLengthResponse(htmlInput);
                        //return newFixedLengthResponse(Response.Status.NOT_FOUND, MIME_PLAINTEXT,
                       //         exception.getMessage());
                    }
                }

                return newFixedLengthResponse(htmlInput);

                //return newFixedLengthResponse(Response.Status.OK, MIME_PLAINTEXT,
                  //      "Uploaded files " + " out of " +context.getFilesDir()+yourFile.toString());
            } catch (FileUploadException e) {
                return newFixedLengthResponse(htmlInput);
                // throw new IllegalArgumentException("Could not handle files from API request", e);
            }
        }
        return newFixedLengthResponse(htmlInput);

    }



}