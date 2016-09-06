/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.utils.crypt;

import lombok.extern.slf4j.Slf4j;
import org.jasypt.util.text.BasicTextEncryptor;

/**
 * http://www.jasypt.org/easy-usage.html
 */
@Slf4j
public class EncryptorImpl {

	private static final String MY_ENCRIPTED_PASSWORD = "* http://www.jasypt.org/easy-usage.html";

	public static String decrypt(final String encryptedVal) {
		if (encryptedVal == null) {
			return null;
		}
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword(MY_ENCRIPTED_PASSWORD);
		return textEncryptor.decrypt(encryptedVal);

	}

	public static String encrypt(final String valueEnc) {
		if (valueEnc == null) {
			return null;
		}
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword(MY_ENCRIPTED_PASSWORD);
		return textEncryptor.encrypt(valueEnc);
	}

}
