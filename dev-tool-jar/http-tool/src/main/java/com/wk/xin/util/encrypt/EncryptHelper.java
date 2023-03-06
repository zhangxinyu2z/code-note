package com.wk.xin.util.encrypt;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

/**
 * MD5工具
 *
 * MD5无法解密，但是相同的用户名和密码加密后是一致的
 * @author xinyu.zhang
 */
public class EncryptHelper {
    private static final String passwordStr = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static String SECRET_KEY = "B@4f9fdc&1ETCde";

    public EncryptHelper() {
    }

    public static String getCheckSum(Map<String, String> nvMap) {
        String SEPARATOR = ":";
        String checksumString = "";
        String computedChecksum = null;
        TreeMap<String, String> treeMap = new TreeMap(nvMap);
        Set<String> keys = treeMap.keySet();
        Collection<String> values = treeMap.values();
        if (keys.size() != values.size()) {
        }

        Iterator<String> itKeys = keys.iterator();
        Iterator<String> itValues = values.iterator();
        StringBuffer sb = new StringBuffer();

        while(itKeys.hasNext()) {
            sb.append((String)itKeys.next() + "=" + (String)itValues.next() + ":");
        }

        if (sb.length() > 0) {
            checksumString = sb.substring(0, sb.length() - 1);
        }

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            computedChecksum = toHex(md.digest(checksumString.getBytes()));
        } catch (NoSuchAlgorithmException var12) {
            var12.printStackTrace();
        }

        return computedChecksum;
    }

    public static String toHex(byte[] ba) {
        char[] hexdigit = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        StringBuffer sb = new StringBuffer("");

        for(int i = 0; i < ba.length; ++i) {
            sb.append(hexdigit[ba[i] >> 4 & 15]);
            sb.append(hexdigit[ba[i] & 15]);
        }

        return sb.toString();
    }

    public static String md5(String str) {
        if (str == null) {
            return null;
        } else {
            try {
                MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                messageDigest.update(str.getBytes(), 0, str.length());
                String hashedPass = (new BigInteger(1, messageDigest.digest())).toString(16);
                if (hashedPass.length() < 32) {
                    hashedPass = "0" + hashedPass;
                }

                return hashedPass.toUpperCase();
            } catch (NoSuchAlgorithmException var3) {
                return null;
            }
        }
    }

    public static String md5(byte[] bytes) {
        if (bytes == null) {
            return null;
        } else {
            try {
                MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                messageDigest.update(bytes, 0, bytes.length);
                String hashedPass = (new BigInteger(1, messageDigest.digest())).toString(16);
                if (hashedPass.length() < 32) {
                    hashedPass = "0" + hashedPass;
                }

                return hashedPass.toUpperCase();
            } catch (NoSuchAlgorithmException var3) {
                return null;
            }
        }
    }

    public static String getRandomPassword() {
        StringBuffer sb = new StringBuffer();
        Random r = new Random();

        for(int i = 0; i < 8; ++i) {
            sb.append("1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".charAt(r.nextInt("1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".length())));
        }

        return sb.toString();
    }

    public static String md5_old(String data) {
        if (data == null) {
            return null;
        } else {
            StringBuffer buf = new StringBuffer(32);

            try {
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                md5.update(data.getBytes("ISO-8859-1"));
                byte[] bytes = md5.digest();

                for(int i = 0; i < bytes.length; ++i) {
                    buf.append(Integer.toHexString(255 & bytes[i] | -256).substring(6));
                }
            } catch (Exception var5) {
                var5.printStackTrace();
            }

            return buf.toString();
        }
    }

    public static String encrypt3DES(String data, byte[] key) {
        if (data != null && key != null) {
            try {
                byte[] enData = doCrypt("DESede", 1, key, data.getBytes("UTF-16LE"));
                return byte2hex(enData);
            } catch (Exception var3) {
                throw new RuntimeException(var3);
            }
        } else {
            return data;
        }
    }

    public static String encrypt3DES(String data) {
        try {
            return encrypt3DES(data, SECRET_KEY.getBytes("ISO-8859-1"));
        } catch (Exception var2) {
            throw new RuntimeException(var2);
        }
    }

    public static String decrypt3DES(String data, byte[] key) {
        if (data != null && key != null) {
            try {
                byte[] enData = hex2byte(data);
                byte[] deData = doCrypt("DESede", 2, key, enData);
                return new String(deData, "UTF-16LE");
            } catch (Exception var4) {
                throw new RuntimeException(var4);
            }
        } else {
            return data;
        }
    }

    public static String decrypt3DES(String data) {
        try {
            return decrypt3DES(data, SECRET_KEY.getBytes("ISO-8859-1"));
        } catch (Exception var2) {
            throw new RuntimeException(var2);
        }
    }

    private static byte[] doCrypt(String algorithm, int mode, byte[] key, byte[] data) {
        byte[] result = new byte[0];

        try {
            byte[] md5Key = md5(new String(key, "ISO-8859-1")).substring(0, 24).getBytes("ISO-8859-1");
            SecretKey deskey = new SecretKeySpec(md5Key, algorithm);
            Cipher c1 = Cipher.getInstance(algorithm);
            c1.init(mode, deskey);
            result = c1.doFinal(data);
        } catch (UnsupportedEncodingException var8) {
        } catch (NoSuchAlgorithmException var9) {
            var9.printStackTrace();
        } catch (NoSuchPaddingException var10) {
            var10.printStackTrace();
        } catch (InvalidKeyException var11) {
            var11.printStackTrace();
        } catch (IllegalBlockSizeException var12) {
            var12.printStackTrace();
        } catch (BadPaddingException var13) {
            var13.printStackTrace();
        }

        return result;
    }

    private static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";

        for(int n = 0; n < b.length; ++n) {
            stmp = Integer.toHexString(b[n] & 255);
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }

            if (n < b.length - 1) {
                hs = hs + "";
            }
        }

        return hs.toUpperCase();
    }

    private static byte[] hex2byte(String s) {
        int length = s.length() / 2;
        byte[] bs = new byte[length];

        for(int i = 0; i < length; ++i) {
            String substr = s.substring(i * 2, (i + 1) * 2);
            bs[i] = (byte)Integer.parseInt(substr, 16);
        }

        return bs;
    }

    public static void main(String[] args) {
        System.out.println(md5((String)null));
        System.out.println(md5(""));
        System.out.println(md5("123"));
        System.out.println(md5("xxxxalkjsdlkfj��ð�����-"));
    }

    public static void main2(String[] args) throws Exception {
        String inputData = "Hello, China !";
        String enData = encrypt3DES(inputData, "MY_SECRET_KEY".getBytes());
        String deData = decrypt3DES(enData, "MY_SECRET_KEY".getBytes());
        System.out.println("Source data: " + inputData + "[length:" + inputData.length() + "]");
        System.out.println("Encrypt data: " + enData + "[length:" + enData.length() + "]");
        System.out.println("Decrypt data: " + deData + "[length:" + deData.length() + "]");
    }
}
