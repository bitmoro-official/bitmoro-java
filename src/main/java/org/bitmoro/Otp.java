package org.bitmoro;

public class Otp {
    int otp;
    long expiryTime;
    public Otp(int otp, long expiryTime){
        this.otp = otp;
        this.expiryTime = expiryTime;
    }
}