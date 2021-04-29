package com.airong.springcloud.service.gateway;

public class Response {

    private boolean success;

    private String errCode;

    private String errMessage;


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

    public static Response failed(String errCode, String errMessage) {
        Response response = new Response();
        response.setErrCode(errCode);
        response.setErrMessage(errMessage);
        response.setSuccess(false);
        return response;
    }

}
