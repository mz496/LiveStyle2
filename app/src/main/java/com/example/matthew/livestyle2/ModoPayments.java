package com.example.matthew.livestyle2;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import retrofit.RestAdapter;


public class ModoPayments {

    private static final RestAdapter REST_ADAPTER = new RestAdapter.Builder().setEndpoint(ModoConfig.API_URL).build();
    private static final ModoService SERVICE = REST_ADAPTER.create(ModoService.class);
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static ModoService getService() {
        return SERVICE;
    }

    public static String HMAC_SHA256(String message, byte[] secret) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret, "HmacSHA256");
            sha256_HMAC.init(secret_key);

//            String hash = Base64.encodeBase64String(sha256_HMAC.doFinal(message.getBytes()));
//            String hash = new String(sha256_HMAC.doFinal(message.getBytes()));
            String hash = bytesToHex(sha256_HMAC.doFinal(message.getBytes()));
            System.out.println(hash);
            return hash;
        }
        catch (Exception e){
            System.out.println("Error");
            return "";
        }
    }

    public static byte[] HMAC_SHA256_BINARY(String message, String secret) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);

//            String hash = Base64.encodeBase64String(sha256_HMAC.doFinal(message.getBytes()));
//            String hash = new String(sha256_HMAC.doFinal(message.getBytes()));
            //String hash = bytesToHex(sha256_HMAC.doFinal(message.getBytes()));
            return sha256_HMAC.doFinal(message.getBytes());
//            hash = new BigInteger(hash, 16).toString(2);
//            System.out.println(hash);
//            return hash;
        }
        catch (Exception e){
            System.out.println("Error");
            return new byte[0];
        }
    }

    public static String SHA256(String message) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(message.getBytes("UTF-8"));
            String hashed = bytesToHex(hash);
            return hashed;
        } catch (NoSuchAlgorithmException e) {
            Log.e("NO_SUCH_ALG", e.getMessage());
        } catch (UnsupportedEncodingException e) {
            Log.e("UNSUPPORTED_ENC", e.getMessage());
        }
        return "";
    }

    public static String createSignature(String requestURI, long timeStamp, String requestBody,
                                         String key, String secret) {
        String signString = String.valueOf(timeStamp) + "&" + requestURI + "&" + SHA256(requestBody).toLowerCase();
        Log.d("SHA256", SHA256(requestBody));
        Log.d("signString", signString);
        byte[] signingKey = HMAC_SHA256_BINARY(String.valueOf(timeStamp), "MODO1" + secret);
        System.out.println(signingKey);
        String signature = HMAC_SHA256(signString, signingKey);
//        Log.d("signature", signature);
//        int decimal = Integer.parseInt(signingKey, 2);
//        String hexStr = Integer.toString(decimal,16);
//        Log.d("hexStr",hexStr);
        String authString = "MODO1 " + "key=" + key + ", sig=" + signature.toLowerCase();
        return authString;
    }
}