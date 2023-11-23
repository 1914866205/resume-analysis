package com.zpmc.analysis.service;

import com.zpmc.analysis.common.Result;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: 倪涛涛
 * @create: 2023-10-19 12:11
 **/
public interface ResumeService {
    Result importData(MultipartFile file) throws IOException;

    void exportData(HttpServletResponse response, String path) throws IOException;
}
