package com.example.grouptunes;

import android.app.Activity;
import android.content.Context;

import com.android.volley.Response;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
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

    public WebServerActivity(Context context) throws IOException {
        super(PORT);
        this.context = context;
    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();

        if (uri.equals("/hello")) {
            String response = "HelloWorld";
            return newFixedLengthResponse(response);
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