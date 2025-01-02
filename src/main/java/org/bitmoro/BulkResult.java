package org.bitmoro;

import java.util.Date;

public class BulkResult {
    String status;
    Report[] reports;
    int creditSpent;
    String messageId;
    String senderId;
    int errorCode;
    String errorMessage;
    int statusCode;

    public BulkResult(String status, Report[] reports, int creditSpent, String messageId, String senderId, int errorCode, String errorMessage, int statusCode){
        this.status = status;
        this.reports = reports;
        this.creditSpent = creditSpent;
        this.messageId = messageId;
        this.senderId = senderId;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.statusCode = statusCode;
    }
}
