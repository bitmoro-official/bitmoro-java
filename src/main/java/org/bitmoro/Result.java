package org.bitmoro;


public class Result {
    int numberOfFailed;
    String messageId;
    String message;
    int errorCode;
    int statusCode;

    public Result(int numberOfFailed, String messageId, String message,int errorCode, int statusCode){
        this.numberOfFailed = numberOfFailed;
        this.messageId = messageId;
        this.message = message;
        this.errorCode = errorCode;
        this.statusCode = statusCode;
    }
}
