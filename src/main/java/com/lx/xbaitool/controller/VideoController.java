package com.lx.xbaitool.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tool")
public class VideoController {

    @RequestMapping("/video.html")
    public String video() {
        return "video";
    }
}
