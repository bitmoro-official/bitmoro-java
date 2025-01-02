package org.bitmoro;

import java.net.URI;
import java.util.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

public class Bitmoro {
    private String token;
    private String api = "https://api.bitmoro.com/message/api";
    private String bulkApi = "https://api.bitmoro.com/message/bulk-api";
    private String dynamicApi = "https://api.bitmoro.com/message/dynamic-api";
    private String messageReport = "https://api.bitmoro.com/message/message-report/";
    public static ArrayList<Map<String, String>> contacts = new ArrayList<>();
    public  Bitmoro(String token){
        this.token = token;
    }


    public Result sendMessage(String[] number, String message, String senderId ) {
        try {
            // Create the HttpClient instance
            String numberArray = jsonArray(number);
            // Prepare the JSON body with your data
            String jsonInputString = "{\n" +
                    "  \"number\": " + numberArray + ",\n" +  // Example array in data
                    "  \"message\": \"" + message + "\"";  // Message text
            if(!senderId.equals("")){
                jsonInputString += ",\n  \"senderId\": \"" + senderId + "\"\n";
            }
            jsonInputString += "\n}";


            HttpResponse<String> response = sendHttpRequest(jsonInputString, "post", this.api);
            return extractReponseOtp(response.body(), response.statusCode());
        } catch (Exception e) {
            return new Result(0, "", "", 0, 0);
        }
    }
    public BulkResult sendBulkMessage(String[] number, String message, String senderId, long scheduledDate, String callBackURL ){
        try{
            String numberArray = jsonArray(number);
            String jsonInputString = "{\n" +
                    "  \"number\": " + numberArray + ",\n" +  // Example array in data
                    "  \"message\": \"" + message + "\",\n" +
                    "  \"scheduledDate\": " + scheduledDate;
            if(!senderId.equals("")){
                jsonInputString += ",\n  \"senderId\": \"" + senderId + "\"";
            }
            if(!callBackURL.equals("")){
                jsonInputString+=",\n  \"callBackUrl\": \"" + callBackURL + "\"";
            }
            jsonInputString+= "\n}";


            HttpResponse<String> response = sendHttpRequest(jsonInputString, "post", this.bulkApi);
            return extractResultBulk(response.body(), response.statusCode());

        } catch (Exception e) {
            Report obj = new Report("", "", 0,0);
            Report[] arr = {obj};
            return  new BulkResult("", arr, 0,"","",0, "", 0);
        }
    }


    public BulkResult sendDynamicBulkMessage(List<Map<String, String>> contacts, String message, long scheduledDate, Map<String, String> defaultValues, String callBackUrl, String senderId){
        try {
            String jsonContact = dynamicJson(contacts);
            String defaultValue = new JSONObject(defaultValues).toString();
            String jsonInputString = "{\n" +
                    "  \"contacts\": " + jsonContact + ",\n" +
                    "  \"message\": \"" + message + "\",\n" +
                    "  \"scheduledDate\": " + scheduledDate + ",\n"+
                    "  \"defaultValues\": " + defaultValue ;

            if(!senderId.equals("")){
                jsonInputString+=",\n  \"senderId\": \"" + senderId + "\"\n";
            }
            if(!callBackUrl.equals("")){
                jsonInputString+=",\n  \"callBackUrl\": \"" + callBackUrl + "\"\n";
            }
            jsonInputString += "\n}";
            HttpResponse<String> response = sendHttpRequest(jsonInputString,"post",this.dynamicApi);
            return extractResultBulk(response.body(), response.statusCode());

        }catch (Exception e){
            Report obj = new Report("", "", 0,0);
            Report[] arr = {obj};
            return  new BulkResult("", arr, 0,"","",0, "", 0);
        }

    }

    public MessageReport getMessageReport(String messageId){
        try{
            HttpResponse<String> response = sendHttpRequest("","get",this.messageReport+messageId);
            return extractMessageReport(response.body(), response.statusCode());

        }catch(Exception e){
            return extractMessageReport("error", 500);
        }
    }
    private Result extractReponseOtp(String responseBody, int statusCode){
        JSONObject jsonObject = new JSONObject(responseBody);
        int numberOfFailed = 0;
        String messageId = "";
        String message = "";
        int errorCode = 0;

        if(statusCode == 200){
            numberOfFailed = jsonObject.getInt("numberOfFailed");
            messageId = jsonObject.getString("messageId");
        }else{
            message = jsonObject.getString("message");
            errorCode = jsonObject.getInt("errorCode");
        }
        return new Result(numberOfFailed, messageId,message, errorCode, statusCode);
    }

    private BulkResult extractResultBulk(String responseBody, int statusCode){
        JSONObject jsonObject = new JSONObject(responseBody);

        if(statusCode == 200){
            JSONArray reportObject = jsonObject.getJSONArray("report");
            Report[] arr = new Report[reportObject.length()];

            for (int i = 0; i<reportObject.length(); i++){
                JSONObject data = reportObject.getJSONObject(i);
                arr[i] = new Report(data.getString("number"),data.getString("message"),data.getInt("credit"),data.getInt("type"));
            }
            return new BulkResult(jsonObject.getString("status"),arr, jsonObject.getInt("creditSpent"),jsonObject.getString("messageId"), jsonObject.getString("senderId"), 0, "", statusCode);
        }
        Report obj = new Report("", "", 0,0);
        Report[] arr = {obj};
        return  new BulkResult("", arr, 0,"","",jsonObject.optInt("errorCode",0), jsonObject.getString("message"), statusCode);
    }

    private String dynamicJson(List<Map<String, String>> contacts){
        JSONArray jsonArray = new JSONArray();
        for (Map<String, String> contact: contacts){
            JSONObject jsonObject = new JSONObject(contact);
            jsonArray.put(jsonObject);
        }
        return jsonArray.toString();
    }

    private String jsonArray(String[] array) {
        JSONArray object = new JSONArray(array);
        return object.toString();
    }

    private MessageReport extractMessageReport(String response, int statusCode){
        if(statusCode == 200){
            JSONObject object = new JSONObject(response);
            return new MessageReport(object.getString("messageId"),object.getString("status"), object.getInt("total"), object.getInt("failed"),object.getInt("creditSpent"),object.getInt("refunded"), object.getString("senderName"), object.getString("deliveredDate"),object.getString("scheduleDate"),0,"", statusCode);
        }
        return  new MessageReport("", "",0,0,0,0,"","", "", 0, "", statusCode);
    }

    private HttpResponse<String> sendHttpRequest(String jsonInputString, String requestType, String api)throws  Exception{
        try{

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request;
            if(requestType.equals("post")){
                request = HttpRequest.newBuilder()
                        .uri(new URI(api))
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Bearer " + this.token)  // Replace 'Token' with your actual token
                        .POST(HttpRequest.BodyPublishers.ofString(jsonInputString))
                        .build();
            }else{
                request = HttpRequest.newBuilder()
                        .uri(new URI(api))
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Bearer " + this.token)  // Replace 'Token' with your actual token
                        .GET()
                        .build();
            }
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        }catch(Exception e){
            throw new Exception("Failed to send HTTP request", e);
        }
    }

    public static void main(String[] args) {
        Bitmoro bitmoro = new Bitmoro("JmTOJkoqIkOMkOkXUsYD-22cb17384ed34727e54ec3ef7804fbb4b6bdf6411bcb7a29b5ac495b50bc");
        OtpHandler otp = new OtpHandler(5, 500);
        int otpData = otp.registerOtp("userId");
        String[] arr = {"9862937055"};
        Map<String, String> contact2 = new HashMap<>();
        contact2.put("number", "9862937055");

        Map<String, String> defaultValue = new HashMap<>();
        defaultValue.put("name","user");
        defaultValue.put("city","Default City");
    }



}
