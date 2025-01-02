# OTP Handler Application

## Overview

This Java application provides an OTP (One-Time Password) management system. It supports generating, registering, validating, and expiring OTPs. Additionally, it includes functionality to send message to user's phone number.

## Features
- **Register OTP**: Store OTPs with expiration times associated with user IDs.
- **Validate OTP**: Verify if an OTP is correct and still valid.
- **Expire OTP**: Automatically remove expired or used OTPs from storage.
- **Send Message**: Send OTPs via an HTTP-based messaging API.
- **Bulk Message**: Send SMS in bulk to different numbers at once.
- **Dynamic Message**: Send dynamic message to different number accordingly.
- **Message Report**: Get report of the message that was sent.


---

## Requirements
- **Java Version**: Java 11 or later

---

## Setup

- **API Key**: Get the API key from Bitmoro.com. You can follow this blog as well: [API Blog](https://bitmoro.com/blog/api-integration-for-bulk-sms-service-with-bitmoro/)
---
## Creating OTP
#### 1. Creating Instance
- Import bitmoro java package first.
  ``` java
  import org.bitmoro.*;
  ```
- Create a instance of a class that takes two different parameters.

&nbsp;&nbsp;&nbsp;&nbsp;  OtpHandler(length, exipryTime, token)

1. Length: It denots the length of the OTP. Minimun length is 4.
2. Expiry Time: It denotes the expiry time of the otp in seconds.
3. token: API token that user have taken from Bitmoro.

   ``` java
   OtpHandler otpHandlerObj = new OtpHandler(5,300, "token");
   ```

#### Registering OTP
- User can register OTP by calling registerOtp method.
1. Register Otp takes one parameter that is unique Id and returns an integer.
2. If Unique Id is already registered with the userId and the OTP is not expired then it will return 0.
3. If unique Id has been registered then it will give the provide the user with the OTP.
4. Unique Id must be string.


 ``` java
   int otp = otpHandlerObj.registerOtp("Unique Id");
   ```

#### Validate OTP
- User can validate OTP by calling validateOtp method.
1. Validate Otp takes two parameter that is ID and OTP and returns a boolean.
2. If the OTP is registered with that id and is not expired then it will return true.
3. If the OTP is not valid it will return false.
4. Unique Id must be string and OTP must be integer.

 ``` java
   boolean valid = otpHandlerObj.validOtp("Unique Id", OTP);
   ```
## Sending Message using Bitmoro API
#### Create Instance
- Import Bitmoro package.
   ``` java
   import org.bitmoro.*;
   ```
- At first user have to create a instance by passing API Token in parameter.
   ``` java
   Bitmoro bitmoro = new Bitmoro("Token");
   ```
  After creating instance user can send messages to different users.

#### Send Single Message
- User can send message by calling sendMessage method which takes 3 parameter and returns object of Result.
  #### Parameters:
  |S.N | Parameters    | Data Type    |Required| Description                  |
      |:-:|--------------|--------------|:-------:|-------------------------------|
  | 1|Number         |String        |  Yes  | Phone number to send messsage.|
  | 2|Message        |String        |  Yes  | Message to Send.              |
  | 3|SenderId       |String        | No    | Sender Id. If user do not have sender Id then send empty string|

  **Note**: If user wants to continue without value of those parameter which is not required. Then send default value of that datatype.

    ``` java
    OtpHandler otpHandlerObj = new OtpHandler(5,300, "token");
    //If User do not have sender Id then leave empty string
    Result result = otpHandlerObj.sendMessage("9800000000","Message", "SenderId");
    System.out.println(result.statusCode); //extracting the status code.
    ```

  **Note**: Object that sendMessage() method returns contains 5 different properties. Sucess will alawys gives 200 status code.

  | Properties    | Data Type    |Type| Description      |
      |---------------|---------------|-|---------------|
  | numberOfFailed  | int  |success| This contains the total number of failed contacts to send message.   |
  | messageId  | String  |success| This field will hold the id of the message user have just sent.  |
  | message  | String  |failure| This field will store the error message comming from server. |
  | errorCode  | int  |failure| This field will store the error code |
  | statusCode  | int  |both| This field will store the status code of the response.  |

  ##### Error Code
  Table of error message and error code this method will return.     
  | ErrorCode | ErrorMessage          | StatusCode |
  |-----------|-----------------------|------------|
  | 01      | Must include auth header | 400        |
  | 02      | Invalid Authorization Format | 400   |
  | 03      | Permission not granted for ip ${ip}    | 403|
  | 04      | Number length cannot exceed 100 for instant use     | 400 |
  | 05      | Cannot send message until the number is verified  | 403 |
  |06        | Insufficient credit for user with ID: ${userId}|400|
  |08|Invalid api token|403|
  |09|No Sender Id found |400|
  |10|Choosen senderId is not active contact admin|400|
  |11|Choosen senderId has crossed billing cycle contact admin for renewal |400|


#### Send Bulk Message
- User can send message to different users at once by invoking sendBulk method which takes 5 parameter and return object of BulkResult.
  #### Parameters:

  |S.N | Parameters    | Data Type    |Required| Description                  |
      |:-:|--------------|--------------|:-------:|-------------------------------|
  | 1|Number         |String[]       |  Yes  | Array of phone numbers.|
  | 2|Message        |String        |  Yes  | Message to Send.              |
  | 3|SenderId       |String        | No    | Sender Id. If user do not have sender Id then send empty string|
  | 4|Schedule Date       |long| No|A valid unix timestamp in future. if you do not want to schedule enter 0. |
  |5|Callback URL|String| No| callbackUrl is a valid URL to receive message report via POST request from our server. Send empty string if you do not have callbackurl.|

  **Note**: If user wants to continue without value of those parameter which is not required. Then send default value of that datatype.
    ``` java
    Bitmoro bitmoro = new Bitmoro("Token"); //creating instance of class Bitmoro
    String[] arr = {"9800000000", "9800000001"}; //array of numbers
    BulkResult bulkResult = bitmoro.sendBulkMessage(arr, "message", "BIT_MORE", 0, "Call Back URL" ); //sending bulk message to numbers in array.
    System.out.println(bulkResult.statusCode); //extracting the status code
    ```
  **Note**: The object sendBulkMessage() returns contains 8 different properties. Sucess will alawys gives 200 status code.

  | Properties    | Data Type    |Type| Description      |
      |---------------|---------------|-|---------------|
  | status|String|success|It contains the status of the message sent.|
  | reports  |Report[]|success|It contains the array of report object of message sent to different user.|
  |reports[index].number|String|success|Phone number of the user to whom message was sent.|
  | reports[index].message  |String|success| Message that was sent to that phone number|
  | reports[index].credit  |int|success| Credit spent to that number while sending number.|
  | reports[index].type  |int|success |Type of message sent to user. (1: ASCII message), (2: Unicode Message)|
  | creditSpent  |int|success|Total amount of credit spent while sendong message.|
  | messageId|String|success| It stores the message Id of the message sent by user. |
  | senderId|String|success| It displays from which senderId message was sent.  |
  | errorCode|int|failure| This field will store the error code.  |
  | errorMessage|String|failure| This field will store the error message sent from server.  |
  | statusCode| int|both| This field will store the status code of the response.|

  ##### Error Code
  Table of error message and error code this method will return.     
  | ErrorCode | ErrorMessage                                                   | StatusCode |
  |-----------|----------------------------------------------------------------|------------|
  | 01        | Must include auth header                                       | 400        |
  | 02        | Invalid Authorization Format                                   | 400        |
  | 03        | Permission not granted for ip ${ip}                            | 403        |
  | 05        | Cannot send message until the number is verified               | 403        |
  | 06        | Insufficient credit for user with ID: ${userId}                | 400        |
  | 08        | Invalid api token                                              | 403        |
  | 09        | No senderId found                                              | 400        |
  | 10        | Choosen senderId is not active contact admin                   | 400        |
  | 11        | Choosen senderId has crossed billing cycle contact admin for renewal | 400 |

  #### Callback Response

  | Field               | Type                 | Description                              |
      |---------------------|----------------------|-------------------------------------------|
  | messageId           | string              | A unique identifier for the message.      |
  | message             | string              | The body of the message.              |
  | status              | MESSAGE_STATUS      | The status of the message.            |
  | report              | object              | Detailed information about the message sent.  |
  | report.number       | string      | The phone number to which the message was sent.|
  | report.message| string (optional)| If there's an error, this contains the error message.|
  | report.status       |String  | The status of the message.               |
  | report.creditCount  | number| The number of credits spent to send the message.        |
  | senderId            | string| The sender ID of the message.                           |
  | deliveredDate       | Date                | The date when the message was delivered.|
  | refunded            | number              | The number of credits refunded.|

  #### CallBack Response example
    ``` json
    {
        messageId: 'Gy2tr24Ti3UgRTiXOZq3',
            message: 'Hello',
            status: 'sent',
            report: [
                {
                    number: '98123456789',
                    status: 'sent',
                    creditCount: 1
                },
                {
                    number: '9809876543',
                    status: 'failed',
                    message: 'number not available',
                    creditCount: 1
                }
          ],
          failed: 1,
          senderId: 'bit_alert',
          deliveredDate: '2024-12-16T08:01:01.762Z',
          refunded: 1
    }
    ```

#### Send Dynamic Message in Bulk
- User can send dynamic message to different users at once by invoking sendBulk method which takes 6 parameter and return object of BulkResult.
  #### Parameters:

  |S.N | Parameters    | Data Type    |Required| Description                  |
      |:-:|--------------|--------------|:-------:|-------------------------------|
  | 1|Number         |ArrayList<Map<String, String>>       |  Yes  | Array of phone numbers.|
  | 2|Message        |String        |  Yes  | Message to Send.              |
  | 3|SenderId       |String        | No    | Sender Id. If user do not have sender Id then send empty string|
  | 4|Schedule Date       |long| No|A valid unix timestamp in future. if you do not want to schedule enter 0. |
  |5|Callback URL|String| No| callbackUrl is a valid URL to receive message report via POST request from our server. Send empty string if you do not have callbackurl.|
  |6|Default Value|Map<String, String>|Yes|Default value to be sent for dynamic message|

  **Note 1**: If user wants to continue without value of those parameter which is not required. Then send default value of that datatype.
  **Note 2**: Bitmoro have default ArrayList for storing Map of numbers. User can access it by calling it from instance of bitmoro. Eg. bitmoroObj.contacts.add(Map<String, String>);

    ``` java
        Bitmoro bitmoro = new Bitmoro("Token");
    
        Map<String, String> contact1 = Map.of(
                "number", "9800000000",
                "name", "Harry"
        ); //creating first contacts.
        Map<String, String> contact2 = Map.of(
                "number", "9800000001",
                "location", "biratnagar"
        ); //creating second contact
        
        //Using bitmoro arraylist to store the list of numbers.
        //adding contacts to Arraylist of number
        bitmoro.contacts.add(contact1); 
        bitmoro.contacts.add(contact2);
        
        //creating message to send to users.
        String message = "Hello ${name}. You Live in ${location}";
        
        //creating default values to send to users,
        Map<String, String> defaultValues = Map.of(
                "name", "User",
                "location", "Kathmandu"
        );
        
        //sending dynamic bulk message to users.
        BulkResult bulkResult = bitmoro.sendDynamicBulkMessage(bitmoro.contacts, message, "BIT_MORE", 0, "callbackurl",defaultValues );
        System.out.println(bulkResult.statusCode);
    
    ```

  **Note**: The object sendBulkMessage() returns contains 8 different properties. Sucess will alawys gives 200 status code.

  | Properties    | Data Type    |Type| Description      |
      |---------------|---------------|-|---------------|
  | status|String|success|It contains the status of the message sent.|
  | reports  |Report[]|success|It contains the array of report object of message sent to different user.|
  |reports[index].number|String|success|Phone number of the user to whom message was sent.|
  | reports[index].message  |String|success| Message that was sent to that phone number|
  | reports[index].credit  |int|success| Credit spent to that number while sending number.|
  | reports[index].type  |int|success |Type of message sent to user. (1: ASCII message), (2: Unicode Message)|
  | creditSpent  |int|success|Total amount of credit spent while sendong message.|
  | messageId|String|success| It stores the message Id of the message sent by user. |
  | senderId|String|success| It displays from which senderId message was sent.  |
  | errorCode|int|failure| This field will store the error code.  |
  | errorMessage|String|failure| This field will store the error message sent from server.  |
  | statusCode| int|both| This field will store the status code of the response.|

  ##### Error Code
  Table of error message and error code this method will return.     
  | ErrorCode | ErrorMessage                                                   | StatusCode |
  |-----------|----------------------------------------------------------------|------------|
  | 01        | Must include auth header                                       | 400        |
  | 02        | Invalid Authorization Format                                   | 400        |
  | 03        | Permission not granted for ip ${ip}                            | 403        |
  | 05        | Cannot send message until the number is verified               | 403        |
  | 06        | Insufficient credit for user with ID: ${userId}                | 400        |
  | 08        | Invalid api token                                              | 403        |
  | 09        | No senderId found                                              | 400        |
  | 10        | Choosen senderId is not active contact admin                   | 400        |
  | 11        | Choosen senderId has crossed billing cycle contact admin for renewal | 400 |

  #### Callback Response

  | Field               | Type                 | Description                              |
      |---------------------|----------------------|-------------------------------------------|
  | messageId           | string              | A unique identifier for the message.      |
  | message             | string              | The body of the message.              |
  | status              | MESSAGE_STATUS      | The status of the message.            |
  | report              | object              | Detailed information about the message sent.  |
  | report.number       | string      | The phone number to which the message was sent.|
  | report.message| string (optional)| If there's an error, this contains the error message.|
  | report.status       |String  | The status of the message.               |
  | report.creditCount  | number| The number of credits spent to send the message.        |
  | senderId            | string| The sender ID of the message.                           |
  | deliveredDate       | Date                | The date when the message was delivered.|
  | refunded            | number              | The number of credits refunded.|

  #### CallBack Response example
    ``` json
    {
        messageId: 'Gy2tr24Ti3UgRTiXOZq3',
            message: 'Hello',
            status: 'sent',
            report: [
                {
                    number: '98123456789',
                    status: 'sent',
                    creditCount: 1
                },
                {
                    number: '9809876543',
                    status: 'failed',
                    message: 'number not available',
                    creditCount: 1
                }
          ],
          failed: 1,
          senderId: 'bit_alert',
          deliveredDate: '2024-12-16T08:01:01.762Z',
          refunded: 1
    }
    ```

#### Get Message Report
This feature allows users to retrieve a detailed report of their sent messages.It provides a clear overview for tracking and managing message performance and delivery outcomes.
- User can get message report by invoking getMessageReport method that takes 1 parameter.
  #### Parameters:

  |S.N | Parameters    | Data Type    |Required| Description                  |
      |:-:|--------------|--------------|:-------:|-------------------------------|
  | 1|Message Id         |String|  Yes  | Id of message.|

    ``` java
    Bitmoro bitmoro = new Bitmoro("Token"); //creating instance of bitmoro class.
    //invoking getMessageReport with message Id parameters.
    MessageReport report = bitmoro.getMessageReport("messageId"); 
    System.out.println(report.statusCode); //fetching status code of that response.
    ```
  **Note**: The object getMessageReport() returns object that have 12 different properties. Sucess will alawys gives 200 status code.

  | Properties    | Data Type    |Type| Description      |
      |---------------|---------------|-|---------------|
  | messageId|String|success|This stores the message Id.|
  | status|String|success|This shows the status of the message. (QUEUED, SCHEDULED, SENT, FAIL, CANCEL )|
  |total|int|success|Total number of messages attempted to send.|
  | failed  |int|success| Total number of failed messages.|
  | creditSpent  |int|success| Total credits used to send the message(s).|
  | refunded  |int|success |Total number of credits refunded.|
  | senderName  |String|success|Name of the sender.|
  | deliveryDate|String|success| Date and time when the message was delivered. |
  | scheduleDate|String|success| Date and time when the message is scheduled to be sent.  |
  | errorCode|int|failure| This field will store the error code.  |
  | errorMessage|String|failure| This field will store the error message sent from server.  |
  | statusCode| int|both| This field will store the status code of the response.|

  ##### Error Code
  Table of error message and error code this method will return.
  | ErrorCode | ErrorMessage                                      | StatusCode |
  |-----------|------------------------------------------------|------------|
  | 01        | Must include auth header                        | 400        |
  | 02        | Invalid Authorization Format                   | 400        |
  | 03        | Permission not granted for IP ${ip}            | 403        |
  | 07        | No message found with messageId :${messageId}  | 400        |
  | 08        | Invalid api token                               | 403        |
    
    
    
    
    
    
    
    
    
    


    
    
    
    
    
    
    
    
    








