package com.zpmc.analysis.controller;

import com.zpmc.analysis.common.Result;
import com.zpmc.analysis.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: 倪涛涛
 * @create: 2023-10-19 12:05
 **/
@RestController
@RequestMapping("/resume")
public class ResumeController {

    @Autowired
    ResumeService resumeService;


    @PostMapping("/import")
    public Result importData(@RequestParam(value = "file") MultipartFile file) throws IOException {
        return resumeService.importData(file);
    }

    @GetMapping("/export/{path}")
    public void exportData(HttpServletResponse response, @PathVariable String path) throws IOException {
        resumeService.exportData(response, path);
    }
}
