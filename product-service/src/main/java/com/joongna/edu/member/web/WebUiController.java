package com.joongna.edu.member.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebUiController {

    @GetMapping("/ui/members/subscription")
    public void subscriptionQueryView() {
    }

}
