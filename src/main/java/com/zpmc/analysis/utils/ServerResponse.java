package com.zpmc.analysis.utils;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@Data
//usage example:  ApiResponse.success().code(HTTP_STATUS_OK).message("retrieve data successfully！“）.data(jsonData);
public class ServerResponse implements Serializable {
    private static final long serialVersionUID = 1L;

//    private Boolean success;
    private Integer code;
    private String msg;
    private Object data;

//    public static ServerResponse success(){
//        ServerResponse serverResponse = new ServerResponse();
//        serverResponse.setSuccess(true);
//        return serverResponse;
//    }
//
//    public static ServerResponse fail(){
//        ServerResponse serverResponse = new ServerResponse();
//        serverResponse.setSuccess(false);
//        return serverResponse;
//    }


    public static ServerResponse init() {
        return new ServerResponse();
    }

    public ServerResponse code(Integer resultCode){
        this.setCode(resultCode);
        return this;
    }

    public ServerResponse message(String resultMessage){
        this.setMsg(resultMessage);
        return this;
    }

    public ServerResponse data(Object resultData){
        this.setData(resultData);
        return this;
    }

}
