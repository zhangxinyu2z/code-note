package com.wk.xin.util.encrypt;

import org.springframework.util.DigestUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author xinyu.zhang
 * @since 2023/2/6 12:59
 */
public class Md5Util {
    public static String md52(byte[] input) {
        try {
            return DigestUtils.md5DigestAsHex(new ByteArrayInputStream(input));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
        //return getMd5Digest().digest(input);
    }

    public static byte[] md5(byte[] input) {
        return getMd5Digest().digest(input);
    }


    public static String md5(String origin) {
        try {
            byte[] digest = getMd5Digest().digest(origin.getBytes("utf-8"));
            return new String(digest);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static MessageDigest getMd5Digest() {
        return getDigest("MD5");
    }


    public static MessageDigest getDigest(String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * MD5加密
     *
     * @param s 加密字符串
     * @return 密文
     * @author ChangYing.Chen
     */
    public static String getMD5(String s) {

        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

        try {
            byte[] btInput = s.getBytes(StandardCharsets.UTF_8);
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }



    /**
     * MD5加密字符串（32位大写）
     *
     * @param string 需要进行MD5加密的字符串
     * @return 加密后的字符串（大写）
     */
    public static String md5Encrypt32Upper(String string) {
        byte[] hash;
        try {
            //创建一个MD5算法对象，并获得MD5字节数组,16*8=128位
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        //转换为十六进制字符串
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString().toUpperCase();
    }

    /**
     * MD5加密字符串（32位小写）
     *
     * @param string 需要进行MD5加密的字符串
     * @return 加密后的字符串（小写）
     */
    public static String md5Encrypt32Lower(String string) {
        //直接上面的方法转换成小写就可以了
        return md5Encrypt32Upper(string).toLowerCase();
    }

    /**
     * 将二进制字节数组转换为十六进制字符串
     *
     * @param bytes 二进制字节数组
     * @return 十六进制字符串
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuffer hexStr = new StringBuffer();
        int num;
        for (int i = 0; i < bytes.length; i++) {
            num = bytes[i];
            if (num < 0) {
                num += 256;
            }
            if (num < 16) {
                hexStr.append("0");
            }
            hexStr.append(Integer.toHexString(num));
        }
        return hexStr.toString().toUpperCase();
    }
}
