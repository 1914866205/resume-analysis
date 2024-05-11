package com.zpmc.analysis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName OCRDto
 * @Descriotion TODO
 * @Author nitaotao
 * @Date 2024/5/11 12:43
 * @Version 1.0
 **/
public class OCRDto {
    private List<WordsResult> words_result;
    private int words_result_num;
    private int pdf_file_size;
    private long log_id;
    public List<WordsResult> getWords_result() {
        return words_result;
    }

    public void setWords_result(List<WordsResult> words_result) {
        this.words_result = words_result;
    }

    public int getWords_result_num() {
        return words_result_num;
    }

    public void setWords_result_num(int words_result_num) {
        this.words_result_num = words_result_num;
    }

    public int getPdf_file_size() {
        return pdf_file_size;
    }

    public void setPdf_file_size(int pdf_file_size) {
        this.pdf_file_size = pdf_file_size;
    }

    public long getLog_id() {
        return log_id;
    }

    public void setLog_id(long log_id) {
        this.log_id = log_id;
    }
}

class WordsResult {
    private String words;
    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }
}