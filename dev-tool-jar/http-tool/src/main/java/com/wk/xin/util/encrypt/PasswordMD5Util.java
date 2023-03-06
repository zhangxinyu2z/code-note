package com.wk.xin.util.encrypt;

public class PasswordMD5Util {
	/**
	 * 密码MD5加密
	 *
	 * @param passportId 通行证ID
	 * @param password   原始密码
	 * @return MD5加密后的密码
	 */
	public static String genMD5Password(String passportId, String password) {
		return EncryptHelper.md5("~!@" + passportId + "#$%" + password + "^&*");
	}
}
