package com.tsms.cache;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class InMemCache {
	private static final Map<String, String> OTP_STORE = new ConcurrentHashMap<>();	
	
	public void storeOtp(String email) {
	    String otp = String.valueOf(new Random().nextInt(900000) + 100000);

	    OTP_STORE.put(email, otp);

	}
	
	public boolean verifyOtp(String email, String otp) {
	    String storedOtp = OTP_STORE.get(email);
	    if (storedOtp != null && storedOtp.equals(otp)) {
	    	OTP_STORE.remove(email); 
	        return true;
	    }
	    return false;
	}
}
