package com.zpmc.analysis.utils;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;
import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
/**
 * @author: 倪涛涛
 * @create: 2023-10-19 13:47
 **/

public class ZipUtils {

    private static Integer BUFFER_SIZE = 4 * 1024;


    public static synchronized boolean unZip(String zipFileName, String extPlace,String encode) {
        try {
            return unZipFiles(zipFileName, extPlace,encode);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 解压zip格式文件到指定位置
     * @param zipFileName
     * @param extPlace
     * @return
     */
    private static boolean unZipFiles(String zipFileName, String extPlace,String encode) {
        try {
            (new File(extPlace)).mkdirs();
            File file = new File(zipFileName);
            ZipFile zipFile = new ZipFile(zipFileName,encode);

            if ((!file.exists()) && (file.length() <= 0)) {
                throw new Exception("要解压文件不存在");
            }

            String strPath,gbkPath,strtemp;

            File tempFile = new File(extPlace);
            strPath = tempFile.getAbsolutePath();
            Enumeration<ZipEntry> e = zipFile.getEntries();
            while (e.hasMoreElements()) {
                ZipEntry zipEnt = e.nextElement();
                gbkPath = zipEnt.getName();
                if (zipEnt.isDirectory()) {
                    strtemp = strPath + File.separator + gbkPath;
                    File dir = new File(strtemp);
                    dir.mkdirs();
                    continue;
                }else {
                    //读写文件
                    InputStream is = zipFile.getInputStream(zipEnt);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    gbkPath = zipEnt.getName();
                    strtemp= strPath + File.separator + gbkPath;
                    //建目录
                    String strsubdir = gbkPath;

                    for (int i = 0; i < strsubdir.length(); i++) {
                        if (strsubdir.substring(i, i + 1).endsWith("/")) {
                            String temp = strPath + File.separator + strsubdir.substring(0, i);
                            File subdir = new File(temp);
                            if (!subdir.exists()) {
                                subdir.mkdir();
                            }
                        }
                    }

                    FileOutputStream fos = new FileOutputStream(strtemp);
                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                    int c;
                    while ((c = bis.read()) != -1) {
                        bos.write((byte) c);
                    }
                    bos.close();
                    fos.close();
                }

            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     *  压缩成zip 方法1
     *
     * @param srcDir 压缩文件的路径
     * @param out 压缩文件输出流
     * @param keepDirStructure 是否保持原来的目录结构，true ：保留目录结构
     *                         false :所有文件会跑到压缩包根目录下（如果出现相同包名会出现压缩失败）
     */
    public static void toZip(String srcDir, OutputStream out, boolean keepDirStructure,String encode) {
        long start = System.currentTimeMillis();
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(out);
            zos.setEncoding(encode);
            File sourceFile = new File(srcDir);
            compress(sourceFile, zos, sourceFile.getName(), keepDirStructure);
            long end = System.currentTimeMillis();
//            System.out.println("压缩完成，耗时：" + (end - start) + "ms");
        } catch (Exception e) {
            throw new RuntimeException("zip error fail", e);
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public static void toZip(List<File> srcFiles, OutputStream out,String encode) throws RuntimeException{
        long start = System.currentTimeMillis();
        ZipOutputStream zos = null;
        try {

            zos = new ZipOutputStream(out);
            // 设置压缩的编码，解决压缩路径中的中文乱码问题
            zos.setEncoding(encode);
            for (File srcFile : srcFiles) {
                byte[] buf = new byte[BUFFER_SIZE];
                zos.putNextEntry(new ZipEntry(srcFile.getName()));
                int len;
                FileInputStream in = new FileInputStream(srcFile);
                while ((len = in.read(buf)) != -1) {
                    zos.write(buf, 0, len);
                }
                zos.closeEntry();
                in.close();
            }
            long end= System.currentTimeMillis();
//            System.out.println("完成压缩，耗时" +(end - start) + "ms");
        } catch (Exception e) {
            throw new RuntimeException("zip error fail", e);
        }finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void compress(File sourceFile, ZipOutputStream zos,
                                 String name, boolean keepDirStructure) throws Exception {
        byte[] buf = new byte[BUFFER_SIZE];
        if(sourceFile.isFile()){
            //向zip输出流中添加一个zip实体，构造器中name为实体的文件的名字
            zos.putNextEntry(new ZipEntry(name));
            //copy文件到zip输出流
            int len;

            FileInputStream in = new FileInputStream(sourceFile);
            while ((len = in.read(buf)) != -1) {
                zos.write(buf, 0, len);
            }

            zos.closeEntry();
            in.close();
        }else {
            File[] listFiles = sourceFile.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                //需要保留原来文件结构，需要对空文件夹进行处理
                if (keepDirStructure) {
                    //空文件夹的处理
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    //没有文件，不需要copy
                    zos.closeEntry();
                }
            }else{
                for (File file : listFiles) {
                    if (keepDirStructure) {
                        //注意：file.getName() 前面需要带上父文件夹的名字加一斜杠
                        //不然最后压缩包中就不能保留原来文件结构，所有文件都跑到根目录下面了

                        compress(file, zos, name + "/" + file.getName(), keepDirStructure);
                    }else {
                        compress(file, zos, file.getName(), keepDirStructure);
                    }
                }
            }

        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        //输出路径
//        FileOutputStream fos = new FileOutputStream(new File("D:\\work\\project\\resume-analysis\\file\\root.zip"));
//        toZip("D:\\work\\project\\resume-analysis\\file\\root",fos,true,"gbk");
//        unZip("D:\\work\\project\\resume-analysis\\file\\root.zip","D:\\work\\project\\resume-analysis\\file","gbk");
    }
}
