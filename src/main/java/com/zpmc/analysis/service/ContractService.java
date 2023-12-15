package com.zpmc.analysis.service;

import com.zpmc.analysis.common.Result;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: 倪涛涛
 * @create: 2023-12-14 15:34
 **/
public interface ContractService {
    Result importData(MultipartFile file) throws IOException;

    void exportData(HttpServletResponse response, String path) throws IOException;
}
