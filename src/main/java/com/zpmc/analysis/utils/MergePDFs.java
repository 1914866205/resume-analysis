package com.zpmc.analysis.utils;
/**
 * 针对 职称评审的证书 pdf 合并
 *
 * @ClassName MergePDFs
 * @Descriotion TODO
 * @Author nitaotao
 * @Date 2024/1/3 14:25
 * @Version 1.0
 **/

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;

import static com.zpmc.analysis.service.impl.ResumeServiceImpl.getName;

public class MergePDFs {

    public static void main(String[] args) {
        try {
            // 指定包含PDF文件的文件夹路径
            String folderPath = "D:/360MoveData/Users/lenovo/Desktop/root";

            // 获取文件夹中的所有PDF文件
            File folder = new File(folderPath);
            File[] pdfFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));

            if (pdfFiles != null && pdfFiles.length > 0) {
                // 遍历PDF文件并按原始顺序合并每两个文件
                for (int i = 0; i < pdfFiles.length; i += 2) {
                    if (i + 1 < pdfFiles.length) {

                        //获取姓名
                        String name = "未查到姓名";
                        String result = OCR.getResult(pdfFiles[i].getPath());
                        name = getName(result);
                        System.out.println("处理完成：" + name);
                        mergePDFs(pdfFiles[i], pdfFiles[i + 1], "D:/360MoveData/Users/lenovo/Desktop/root/" + name + ".pdf");
                        // 处理完合并后删除原始文件
                        pdfFiles[i].delete();
                        pdfFiles[i + 1].delete();
                    }
                }

                System.out.println("PDF合并并删除原始文件完成！");
            } else {
                System.out.println("文件夹中没有找到PDF文件。");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void mergePDFs(File pdf1, File pdf2, String outputFilePath) throws IOException {
        try (PDDocument document1 = PDDocument.load(pdf1);
             PDDocument document2 = PDDocument.load(pdf2)) {

            // 创建一个新的PDDocument对象来存储合并后的内容
            PDDocument mergedDocument = new PDDocument();

            // 将第一个PDF的页面添加到新文档
            PDPageTree pages1 = document1.getPages();
            for (PDPage page : pages1) {
                mergedDocument.addPage(page);
            }

            // 将第二个PDF的页面添加到新文档
            PDPageTree pages2 = document2.getPages();
            for (PDPage page : pages2) {
                mergedDocument.addPage(page);
            }

            // 保存合并后的文档
            mergedDocument.save(outputFilePath);
        }
    }
}
