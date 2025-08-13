package com.smileshark.controller.global;

import com.smileshark.common.Result;
import com.smileshark.service.global.EmailService;
import com.smileshark.service.global.GaoDeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/global")
@RequiredArgsConstructor
public class GlobalController {
    private final EmailService emailService;
    private final GaoDeService gaoDeService;

    @RequestMapping("/sendEmail")
    public Result<?> sendEmail(@RequestParam String email) {
        return emailService.sendEmail(email);
    }

    @GetMapping("/addressToPos")
    public Result<String> addressToPos(@RequestParam String address){
        return gaoDeService.addressToPos(address);
    }

}
