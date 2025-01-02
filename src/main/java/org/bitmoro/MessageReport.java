package org.bitmoro;

import java.util.Date;

public class MessageReport {
    String messageId;
    String status;
    int total;
    int failed;
    int creditSpent;
    int refunded;
    String senderName;
    String deliveryDate;
    String scheduleDate;
    int errorCode;
    String errorMessage;
    int statusCode;

    public MessageReport(String messageId, String status, int total, int failed, int creditSpent, int refunded, String senderName, String deliveryDate, String scheduleDate, int errorCode, String errorMessage, int statusCode){
        this.messageId = messageId;
        this.status = status;
        this.total = total;
        this.failed = failed;
        this.creditSpent = creditSpent;
        this.refunded = refunded;
        this.senderName = senderName;
        this.deliveryDate = deliveryDate;
        this.scheduleDate = scheduleDate;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.statusCode = statusCode;
    }

}
