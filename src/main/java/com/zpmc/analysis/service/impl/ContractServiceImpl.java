package com.zpmc.analysis.service.impl;

import com.zpmc.analysis.common.Result;
import com.zpmc.analysis.service.ContractService;
import com.zpmc.analysis.utils.OCR;
import com.zpmc.analysis.utils.ZipUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: 倪涛涛
 * @create: 2023-12-14 15:35
 **/
@Service
public class ContractServiceImpl implements ContractService {
    @Override
    public synchronized Result importData(MultipartFile file) throws IOException {
        long currentTimeMillis = System.currentTimeMillis();
        //   file_contract//时间//root
        String filePath = "file_contract/" + currentTimeMillis + "/" + file.getOriginalFilename();
        String fileOriginalName = file.getOriginalFilename().split("\\.")[0];
        //获取文件名
        InputStream path = null;
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            path = file.getInputStream();
            fis = (FileInputStream) path;
            File dir = new File("file_contract/" + currentTimeMillis);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            fos = new FileOutputStream(filePath);
            int readlen = 0;
            //字节数组，一次读取8个字节
            byte[] buf = new byte[8];
            while ((readlen = fis.read(buf)) != -1) {
                fos.write(buf, 0, readlen);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            path.close();
            fis.close();
            fos.close();
        }

        //解压文件
        ZipUtils.unZip(filePath, "file_contract/" + currentTimeMillis, "gbk");
        //处理完去压缩 删除中间文件夹
        analysisContact("file_contract/" + currentTimeMillis + "/" + fileOriginalName);
        FileOutputStream fileOutputStream = new FileOutputStream("file_contract/" + currentTimeMillis + "/result_" + fileOriginalName + ".zip");
        ZipUtils.toZip("file_contract/" + currentTimeMillis + "/" + fileOriginalName, fileOutputStream, true, "gbk");
        fileOutputStream.close();
        return Result.success(currentTimeMillis + "result_" + fileOriginalName);
    }

    @Override
    public void exportData(HttpServletResponse response, String path) throws IOException {
        char[] chars = path.toCharArray();
        StringBuffer targetPath = new StringBuffer("");
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] <= '9') {
                targetPath.append(chars[i]);
            } else {
                targetPath.append("/");
                targetPath.append(path.substring(i));
                break;
            }
        }
        System.out.println(targetPath);
        File file = new File("file_contract/" + targetPath + ".zip");
        if (file.exists()) {
            response.setContentType("application/zip");
            response.addHeader("Content-Disposition", "attachment; filename=analysis_contract.zip");
            FileInputStream fileInputStream = new FileInputStream(file);
            ServletOutputStream outputStream = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            fileInputStream.close();
            outputStream.close();
        } else {
            // 处理文件不存在的情况
            response.getWriter().println("File not found");
        }
    }

    /**
     * 合同解析重命名
     * C:\Users\nitaotao\Desktop\root\刘鲁松 2022年7月6日-2025年7月5日
     * C:\Users\nitaotao\Desktop\root\吴涛涛 2023年7月8日 无固定
     *
     * @param filePath
     * @throws IOException
     */
    public static void analysisContact(String filePath) throws IOException {
        File root = new File(filePath);
        if (root.exists() && root.isDirectory()) {
            File[] files = root.listFiles();
            for (File file : files) {
                //调用百度OCR
//                "file_contract/" + currentTimeMillis + "/" + fileOriginalName
                String result = OCR.getResult(filePath + "/" + file.getName());
//                String result = OCR.getResult("C:\\Users\\nitaotao\\Desktop\\root\\" + file.getName());
                String pattern = "应聘签约人(\\S+)";
                Pattern regexPattern = Pattern.compile(pattern);
                Matcher matcher = regexPattern.matcher(result);
                String name = "识别失败";
                if (matcher.find()) {
                    String applicantName = matcher.group(1);
                    name = applicantName.split("身")[0];
                }
                String time = "识别失败";
                if (result.contains("无固定")) {
                    // 使用正则表达式匹配日期
                    String pattern2 = "\\d{4}年\\d{1,2}月\\d{1,2}日";
                    Pattern regexPattern2 = Pattern.compile(pattern2);
                    Matcher matcher2 = regexPattern2.matcher(result);
                    String date = "识别失败";
                    if (matcher2.find()) {
                        date = matcher2.group();
                    }
                    time = date + " 无固定";
                } else {
                    // 使用正则表达式匹配日期范围
                    String pattern2 = "\\d{4}年\\d{1,2}月\\d{1,2}日起\\d{4}年\\d{1,2}月\\d{1,2}日";
                    Pattern regexPattern2 = Pattern.compile(pattern2);
                    Matcher matcher2 = regexPattern2.matcher(result);

                    if (matcher2.find()) {
                        time = matcher2.group().replace("起", "-");
                    }
                }
                String fileName = name + " " + time;
                System.out.println(filePath + "/" + fileName);
                file.renameTo(new File(filePath + "/" + fileName + ".pdf"));
            }
        }
    }
}
