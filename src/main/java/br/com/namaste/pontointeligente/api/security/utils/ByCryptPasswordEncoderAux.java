package br.com.namaste.pontointeligente.api.security.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class ByCryptPasswordEncoderAux extends BCryptPasswordEncoder {
	
	private final Log logger = LogFactory.getLog(getClass());
 
	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		if (encodedPassword == null || encodedPassword.length() == 0) {
			logger.warn("Empty encoded password");
			return false;
		}
		return equalsNoEarlyReturn(rawPassword.toString(), encodedPassword);
	}
	
	private boolean equalsNoEarlyReturn(String a, String b) {
		char[] caa = a.toCharArray();
		char[] cab = b.toCharArray();
 
		if (caa.length != cab.length)
			return false;		
 
		byte ret = 0;
		for (int i = 0; i < caa.length; i++)
			ret |= caa[i] ^ cab[i];
		
		return ret == 0;
	}
 
}