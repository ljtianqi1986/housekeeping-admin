package com.framework.utils.pay17;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * 证书信任管理器（用于HTTPS请求）
 * @author GengLong
 *
 */
public class Pay17X509TrustManager implements X509TrustManager
{

	public void checkClientTrusted(X509Certificate[] chain, String authType)
			throws CertificateException
	{
	}

	public void checkServerTrusted(X509Certificate[] chain, String authType)
			throws CertificateException
	{
	}

	public X509Certificate[] getAcceptedIssuers()
	{
		return null;
	}
}