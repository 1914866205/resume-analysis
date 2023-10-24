package com.zpmc.analysis.controller;

import com.zpmc.analysis.service.ValidateCodeService;
import com.zpmc.analysis.utils.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/code")
public class ValidateCodeController {

    private final ValidateCodeService validateCodeService;

    public ValidateCodeController(ValidateCodeService validateCodeService) {
        this.validateCodeService = validateCodeService;
    }

    @PostMapping("/getCode")
    public ServerResponse getCode(String oldUuid) throws IOException {
        return validateCodeService.createCapcha(oldUuid);
    }
}
