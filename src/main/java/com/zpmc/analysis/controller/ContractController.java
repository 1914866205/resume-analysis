package com.zpmc.analysis.controller;

import com.zpmc.analysis.common.Result;
import com.zpmc.analysis.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: 倪涛涛
 * @create: 2023-12-14 15:33
 **/
@RestController
@RequestMapping("/contract")
public class ContractController {

    @Autowired
    ContractService contractService;


    @PostMapping("/import")
    public Result importData(@RequestParam(value = "file") MultipartFile file) throws IOException {
        return contractService.importData(file);
    }

    @GetMapping("/export/{path}")
    public void exportData(HttpServletResponse response, @PathVariable String path) throws IOException {
        contractService.exportData(response, path);
    }
}
