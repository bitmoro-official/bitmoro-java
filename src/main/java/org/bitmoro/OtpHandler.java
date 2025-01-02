package org.bitmoro;


import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONArray;
import org.json.JSONObject;

public class OtpHandler {
    int length;
    int expiryTime;

    public OtpHandler(int length, int expiryTime){
        this.length = length;
        this.expiryTime = expiryTime;

    }
    static HashMap<String, Otp> validOtp = new HashMap<String, Otp>();

    public  int generateOtp(int length ){
        if (length <= 4) {
            this.length = 4;
        }else{
            this.length = length;
        }

        int min = (int) Math.pow(10, this.length - 1); // Smallest number with the given length
        int max = (int) Math.pow(10, this.length) - 1; // Largest number with the given length

        // Generate random OTP in the range [min, max]
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public int registerOtp(String id){
        boolean exist = validOtp.containsKey(id);
        long timestampInSeconds = (System.currentTimeMillis() / 1000)+this.expiryTime;
        if(exist){
            long timestamp = validOtp.get(id).expiryTime;
            if(timestamp < (System.currentTimeMillis() / 1000)){
                expireOtp(id);
            }else{
                return 0;
            }
        }
        int otp = generateOtp(this.length);
        Otp obj = new Otp(otp,timestampInSeconds);
        validOtp.put(id, obj);
        return otp;
    }

    public boolean validateOtp(String id, int otp){
        boolean exist = validOtp.containsKey(id);
        if(!exist){
            return false;
        }else{
            if(otp == validOtp.get(id).otp){
                System.out.println("Otp Validated");
                expireOtp(id);
                return true;
            }
        }
        return false;
    }

    private boolean expireOtp(String id){
        boolean exist = validOtp.containsKey(id);
        if(exist){
            Otp remove = validOtp.remove(id);
            System.out.println("Data removed Successfully");
            return true;
        }
        return false;
    }


    public static void main(String[] args){
        OtpHandler obj = new OtpHandler(10, 5000);

        String[] number = { "9862937055","9869363132","9842882495","9863441309"};
//        Result result =  obj.sendMessage(number,"hello","fe");
//        System.out.println("Status Code: "+result.statusCode);
//        System.out.println("Number Of Failed: "+result.numberOfFailed);
//        System.out.println("Message Id: "+result.messageId);
//        System.out.println("Error Code: "+result.errorCode);
//        System.out.println("Error MEssage: "+result.message);
//        long timeMillis= System.currentTimeMillis() + 600000;
//        BulkResult result2 = obj.sendBulkMessage(number, "hi", "BIT_MORE",timeMillis,"");
//        System.out.println("Status: "+result2.reports.length);
//        for(int k = 0; k<result2.reports.length; k++){
//            System.out.print("Number: "+result2.reports[k].number);
//            System.out.print(", Message: "+result2.reports[k].message);
//            System.out.print(", Type: "+result2.reports[k].type);
//            System.out.println(", Credit: "+result2.reports[k].credit);
//        }
//
//        System.out.println(result2.message);
//        System.out.println(result2.reports[0].creditCount);
//
//        Map<String, String> contact1 = new HashMap<>();
//        contact1.put("number", "9809876543");
//        contact1.put("name", "joe");
//
//        Map<String, String> contact2 = new HashMap<>();
//        contact2.put("number", "9863441339");
//
//        Map<String, String> contact3 = new HashMap<>();
//        contact3.put("number", "9876543210");
//        contact3.put("city", "kathmandu");
//        contact3.put("name", "alice");
//
//        contacts.add(contact1);
//        contacts.add(contact2);
//        contacts.add(contact3);
//
//        Map<String, String> defaultValues = Map.of(
//                "name", "ramu",
//                "city", "Biratnagar"
//        );
//
//        BulkResult result2 =  obj.sendDynamicBulkMessage(contacts, "Hi ${name} how are you. You Live in ${city}", 10, defaultValues,"","BIT_MORE" );
//
//        for(int k = 0; k<result2.reports.length; k++){
//            System.out.print("Number: "+result2.reports[k].number);
//            System.out.print(", Message: "+result2.reports[k].message);
//            System.out.print(", Type: "+result2.reports[k].type);
//            System.out.println(", Credit: "+result2.reports[k].credit);
//        }
    }


}
