package com.groommoa.aether_back_notification.domain.notifications.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/notifications")
@RestController
public class NotificationController {

    @GetMapping("")
    public String eurekaTest() {
        return "eurekaTest";
    }
}
