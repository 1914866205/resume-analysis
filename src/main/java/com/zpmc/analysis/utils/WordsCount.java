package com.zpmc.analysis.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName WordsCount
 * @Descriotion 统计PDF中的字数
 * @Author nitaotao
 * @Date 2024/5/11 12:11
 * @Version 1.0
 **/
public class WordsCount {
    public static void main(String[] args) throws IOException {
        /**
         * 1. 获取文件夹，遍历所有文件
         * 2. 每个文件中，OCR识别打印
         * 3. 字符串统计
         * 4. 文本结果打印：文件名+字数
         */
        getWordsCount("D:\\360MoveData\\Users\\lenovo\\Desktop\\root");
    }

    private static void getWordsCount(String filePath) throws IOException {
        File root = new File(filePath);
        String result = "";
        if (root.exists() && root.isDirectory()) {
            //各个文件
            File[] files = root.listFiles();
            for (File file : files) {
                int count = getFileWordsCount(file.getPath());
                String fileCount = file.getName() + "------------" + (count -(count / 225 * 40));
                result += fileCount + "\r\n";
            }
        }
        System.out.println(result);
    }

    /**
     * 第一个元素表示中文字符数量，第二个元素表示英文字符数量（仅在单词中）。
     *
     * @param path
     * @return
     * @throws IOException
     */
    private static int getFileWordsCount(String path) throws IOException {
        //调用百度OCR
        String resultJson = OCR.getResult(path);
//        System.out.println(jsonConv(resultJson));
        return countCharacters(resultJson);
    }

    private static StringBuilder jsonConv(String resultJson) {
        StringBuilder result = new StringBuilder("");
        try {
            // 将 JSON 字符串解析为 JSONObject
            JSONObject jsonObject = new JSONObject(resultJson);

            // 获取 words_result 字段的值，它是一个 JSON 数组
            JSONArray wordsResultArray = jsonObject.getJSONArray("words_result");

            // 遍历数组中的每个对象，并输出它们的 words 字段值
            for (int i = 0; i < wordsResultArray.length(); i++) {
                JSONObject wordsObject = wordsResultArray.getJSONObject(i);
                String words = wordsObject.getString("words");
                result.append(words);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static int countCharacters(String text) {
        int[] counts = new int[2];

        // 中文字符的正则表达式
        String chineseRegex = "[\\u4e00-\\u9fa5]";
        Pattern chinesePattern = Pattern.compile(chineseRegex);
        Matcher chineseMatcher = chinesePattern.matcher(text);

        // 匹配中文字符
        while (chineseMatcher.find()) {
            counts[0]++;
        }

        // 英文单词字符的正则表达式
        String englishRegex = "\\b[a-zA-Z]+\\b";
        Pattern englishPattern = Pattern.compile(englishRegex);
        Matcher englishMatcher = englishPattern.matcher(text);

        // 匹配包含在单词中的英文字符
        while (englishMatcher.find()) {
            counts[1] += englishMatcher.group().length();
        }

        return counts[0] + counts[1];
    }
}
