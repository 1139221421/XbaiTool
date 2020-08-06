package com.lxl.video.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/video")
public class VideoController {

    @RequestMapping("/index.html")
    public String index() {
        return "index";
    }
}
