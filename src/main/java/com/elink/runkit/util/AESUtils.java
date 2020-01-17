package com.elink.runkit.util;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils {

    // 加密
    public static String encryptDataToStr(String content, String key, String deviceNo, String appId) {
        return base64Encrypt(encrypt(content, getSecret(key, deviceNo)));
    }

    /**
     * 获取密钥
     *
     * @param key
     * @param deviceNo
     * @return secret
     */
    public static String getSecret(String key, String deviceNo) {
        String secret = key + deviceNo + "c8befec76ae5bcd";
        secret = md5(secret);

        String str = "8cae7f3f1b4e658b";
        secret = secret.length() < 16 ? secret + str.substring(0, 16 - secret.length()) : secret.substring(0, 16);

        return secret;
    }

    public static String md5(String str) {
        String md5str = "";
        try {
            // 初始化为md5算法对象
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 转换成byte数组
            byte[] input = str.getBytes();
            // 计算后获得125字节数组
            byte[] buff = md.digest(input);
            // 把数组每一字节（一个字节占八位）换成16进制连成md5字符串
            md5str = bytesToHex(buff);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5str;
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuffer md5str = new StringBuffer();
        // 把数组每一字节换成16进制连成md5字符串
        int digital;
        for (int i = 0; i < bytes.length; i++) {
            digital = bytes[i];

            if (digital < 0) {
                digital += 256;
            }
            if (digital < 16) {
                md5str.append("0");
            }
            md5str.append(Integer.toHexString(digital));
        }
        return md5str.toString().toUpperCase();
    }

    /**
     * AES加密字符串(JDK1.8.0_192)
     *
     * @param
     * @param secret 根据token从redis获取的密钥
     * @param appId app类型相当于岗位
     * @return str 加密后的字符串
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String encryptDataToStr(String content, String secret, String appId) {

        return base64Encrypt(encrypt(content, secret));
    }

    /**
     * AES加密字符串(JDK1.8.0_192)
     *
     * @param content  需要被加密的字符串
     * @param key 加密需要的密码
     * @return 密文
     */
    public static byte[] encrypt(String content, String key) {
        try {
            return encrypt(content.getBytes("utf-8"), key);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    /**
     * AES加密byte[](JDK1.8.0_192)
     *
     * @param  bytecontent  需要被加密的byte[]
     * @param key 加密需要的密码
     * @return 密文
     */
    public static byte[] encrypt(byte[] bytecontent, String key) {
        try {
            SecretKeySpec keyval = new SecretKeySpec(key.getBytes(), "AES");// 转换为AES专用密钥

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");// 创建密码器

            cipher.init(Cipher.ENCRYPT_MODE, keyval);// 初始化为加密模式的密码器

            byte[] result = cipher.doFinal(bytecontent);// 加密

            return result;

        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressLint("NewApi")
    public static String base64Encrypt(byte[] b) {
        String a;
        try {
            a = new String(Base64.encode(b, Base64.NO_WRAP), "utf-8");
            return a;
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    //------------------------>
    // 解密
    public static String decryptDataToJson(String dataStr, String key, String deviceNo, String appId) {
        // TODO Auto-generated catch block
        String jsonObject ="";

        try {
            jsonObject= new String(decrypt(base64Decrypt(dataStr), getSecret(key, deviceNo)),
                    "utf-8");

        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return jsonObject;
    }
    /**
     * 解密AES加密过的字符串(JDK1.8.0_192)
     *
     * @param content AES加密过的内容
     * @param key     加密时的密码
     * @return 明文
     */
    public static byte[] decrypt(byte[] content, String key) {
        try {
            SecretKeySpec keyval = new SecretKeySpec(key.getBytes(), "AES");// 转换为AES专用密钥

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, keyval);// 初始化为解密模式的密码器
            byte[] result = cipher.doFinal(content);
            return result; // 明文

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] base64Decrypt(String b) {
        byte[] bytes;
        try {
            bytes =  Base64.decode(b.getBytes("utf-8"),Base64.NO_WRAP);
            return bytes;
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}