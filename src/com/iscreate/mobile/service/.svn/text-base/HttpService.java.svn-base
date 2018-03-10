package com.iscreate.mobile.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class HttpService {
	/**
	 * POST request
	 * 
	 * @param url
	 *            the URL which you are requesting
	 * @param cookie
	 *            cookies, not exist then set null
	 * @param nameValuePairs
	 *            a list of NameValuePair
	 * @return HttpResponse：the response to the request.
	 * @throws Exception
	 */
	public static HttpResponse postRequest(String url, String cookie,
			List<NameValuePair> nameValuePairs) throws Exception {
		UrlEncodedFormEntity urlEncodedFormEntity = null;
		if (nameValuePairs != null) {
			try {
				urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairs);
			} catch (Exception e) {
				throw (new Exception(
						"nameValuePairs不正确,无法创建UrlEncodedFormEntity!\n"
								+ e.getMessage()));
			}
		}
		HttpPost httpPost = null;
		try {
			httpPost = new HttpPost(url);
		} catch (Exception e) {
			throw (new Exception("URL不正确,无法创建HttpPost!\n" + e.getMessage()));
		}
		httpPost.addHeader("charset", HTTP.UTF_8);
		if (urlEncodedFormEntity != null) {
			httpPost.setEntity(urlEncodedFormEntity);
		}
		httpPost.setHeader("Cookie", cookie);
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpResponse response = httpClient.execute(httpPost);
			httpClient.getParams().setParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
			int statuscode = response.getStatusLine().getStatusCode();
			if (statuscode != HttpStatus.SC_OK) {
				String message = "httpClient: status code=" + statuscode;
				if (statuscode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
					message = message + "\n服务端错误:\n"
							+ getEntityFromHttpResponse(response);
				}
				throw (new Exception(message));
			}
			return (response);
		} catch (Exception e) {
			String causeMsg = null;
			if (e.getCause() != null) {
				causeMsg = e.getCause().getMessage();
			}
			throw (new Exception("HttpPost不正确,DefaultHttpClient执行错误!\n"
					+ e.getMessage()
					+ ((causeMsg == null) ? "" : ("\ncause:" + causeMsg))));
		}
	}

	/**
	 * POST request
	 * 
	 * @param url
	 *            the URL which you are requesting
	 * @param cookie
	 *            cookies, not exist then set null
	 * @param jsonArrayObj
	 *            a JSON string which you want to post
	 * @return Entity：the entity string from the response to the request.
	 * @throws Exception
	 */
	public static String post(String url, String cookie, String jsonArrayObj)
			throws Exception {
		List<NameValuePair> nameValuePairs = null;
		if (jsonArrayObj != null) {
			nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("jsonArrayObj",
					jsonArrayObj));
		}
		return (getEntityFromHttpResponse(HttpService.postRequest(url, cookie,
				nameValuePairs)));
	}

	/**
	 * GET request
	 * 
	 * @param url
	 *            the URL which you are requesting
	 * @param cookie
	 *            cookies, not exist then set null
	 * @return HttpResponse：the response to the request.
	 * @throws Exception
	 */
	public static HttpResponse getRequest(String url, String cookie)
			throws Exception {
		HttpGet httpGet = null;
		try {
			httpGet = new HttpGet(url);
		} catch (Exception e) {
			throw (new Exception("URL不正确,无法创建HttpGet!\n" + e.getMessage()));
		}
		httpGet.addHeader("charset", HTTP.UTF_8);
		httpGet.setHeader("Cookie", cookie);
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
			HttpResponse response = httpClient.execute(httpGet);
			int statuscode = response.getStatusLine().getStatusCode();
			if (statuscode != HttpStatus.SC_OK) {
				String message = "httpClient: status code=" + statuscode;
				if (statuscode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
					message = message + "\n服务端错误:\n"
							+ getEntityFromHttpResponse(response);
				}
				throw (new Exception(message));
			}
			return (response);
		} catch (Exception e) {
			String causeMsg = null;
			if (e.getCause() != null) {
				causeMsg = e.getCause().getMessage();
			}
			throw (new Exception("httpGet不正确,DefaultHttpClient执行错误!\n"
					+ e.getMessage()
					+ ((causeMsg == null) ? "" : ("\ncause:" + causeMsg))));
		}
	}

	/**
	 * GET request
	 * 
	 * @param url
	 *            the URL which you are requesting
	 * @param cookie
	 *            cookies, not exist then set null
	 * @return Entity：the entity string from the response to the request.
	 * @throws Exception
	 */
	public static String get(String url, String cookie) throws Exception {
		return (getEntityFromHttpResponse(HttpService.getRequest(url, cookie)));
	}

	/**
	 * take out the entity from HttpResponse
	 * 
	 * @param httpResponse
	 *            the response to the request.
	 * @return the entity string from the response to the request.
	 * @throws Exception
	 */
	public static String getEntityFromHttpResponse(HttpResponse httpResponse)
			throws Exception {
		if (httpResponse != null) {
			HttpEntity httpEntity = httpResponse.getEntity();
			if (httpEntity != null) {
				try {
					return (EntityUtils.toString(httpEntity));
				} catch (Exception e) {
					throw (new Exception("解析HttpEntity出错(" + e.getMessage()
							+ ")!"));
				}
			}
		}
		return (null);
	}
}