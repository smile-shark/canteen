package com.smileshark.controller.global;

import com.smileshark.common.Result;
import com.smileshark.service.global.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/global")
@RequiredArgsConstructor
public class GlobalController {
    private final EmailService emailService;

    @RequestMapping("/sendEmail")
    public Result<?> sendEmail(@RequestParam String email) {
        return emailService.sendEmail(email);
    }
}
