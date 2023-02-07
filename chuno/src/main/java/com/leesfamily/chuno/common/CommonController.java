package com.leesfamily.chuno.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.MalformedURLException;

@RestController
@RequestMapping("/resources")
public class CommonController {

    @Value("${spring.servlet.multipart.location}") //
    private String uploadFilePath;


    /*public Resource showImage(@PathVariable String filename) throws
            MalformedURLException, FileNotFoundException {
        InputStream file = new FileInputStream(uploadFilePath + "/" + filename);
        return new UrlResource("file:" + file.getPath());
    }*/

    @GetMapping("/images")
    public ResponseEntity<byte[]> userSearch(@RequestParam("path") String path) throws IOException {
        InputStream imageStream = new FileInputStream(uploadFilePath + "/" + path);
        byte[] imageByteArray = toByteArray(imageStream);
        imageStream.close();
        return new ResponseEntity<byte[]>(imageByteArray, HttpStatus.OK);
    }

    public byte[] toByteArray(InputStream imageStream) {
        InputStream is = imageStream;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        int length;
        byte[] buffer = new byte[2048];
        try {
            while ((length = is.read(buffer, 0, buffer.length)) != -1) {
                bos.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] byteArray = bos.toByteArray();
        return byteArray;
    }
}
