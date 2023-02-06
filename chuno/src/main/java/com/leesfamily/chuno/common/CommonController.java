package com.leesfamily.chuno.common;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.net.MalformedURLException;

@RestController
@RequestMapping("/")
public class CommonController {
    @GetMapping("/images/{filename}")
    public Resource showImage(@PathVariable String filename) throws
            MalformedURLException {
        File file = new File(filename);
        return new UrlResource("file:" + file.getPath());
    }
}
