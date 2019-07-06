package com.dtlonline.shop.util;

import com.dtlonline.api.shop.view.geo.Geo;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Request;
import com.ning.http.client.Response;
import io.alpha.app.core.util.JsonUtils;

import java.io.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

/**
 * @author xiaowenlong
 * @date 2015-12-25 下午3:25:56
 */
public class HttpRequestUtil {
	/**
	 * 发送get请求
	 * 
	 * @param url
	 * @param params
	 * @param headers
	 * @return
	 * @throws Exception
	 */
	public static String get(String url, Map<String, String> params, Map<String, String> headers) throws Exception {

		AsyncHttpClient http = new AsyncHttpClient();
		try {
			AsyncHttpClient.BoundRequestBuilder builder = http.prepareGet(url);
			builder.setBodyEncoding("UTF-8");
			if (params != null && !params.isEmpty()) {
				Set<String> keys = params.keySet();
				for (String key : keys) {
					builder.addQueryParameter(key, params.get(key));
				}
			}

			if (headers != null && !headers.isEmpty()) {
				Set<String> keys = headers.keySet();
				for (String key : keys) {
					builder.addHeader(key, params.get(key));
				}
			}
			Future<Response> f = builder.execute();
			return f.get().getResponseBody("UTF-8");
		} catch (Exception e) {
			throw e;
		} finally {
			if (http != null)
				http.close();
		}
	}

	/**
	 * 发送get请求
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static String get(String url) throws Exception {

		return get(url, null);
	}

	/**
	 * 发送get请求
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static String get(String url, Map<String, String> params) throws Exception {

		return get(url, params, null);
	}

	/**
	 * 发送post请求
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static String post(String url, Map<String, String> params) throws Exception {

		AsyncHttpClient http = new AsyncHttpClient();
		try {
			AsyncHttpClient.BoundRequestBuilder builder = http.preparePost(url);
			builder.setBodyEncoding("UTF-8");
			if (params != null && !params.isEmpty()) {
				Set<String> keys = params.keySet();
				for (String key : keys) {
					builder.addQueryParameter(key, params.get(key));
				}
			}
			Future<Response> future = builder.execute();
			return future.get().getResponseBody("UTF-8");
		} catch (Exception e) {
			throw e;
		} finally {
			if (http != null)
				http.close();
		}
	}

	/**
	 * 发送post请求
	 * 
	 * @param url
	 * @param jsonStrParam
	 * @return
	 * @throws Exception
	 */
	public static String post(String url, String jsonStrParam) throws Exception {

		String body = null;
		AsyncHttpClient http = null;
		try {
			http = new AsyncHttpClient();
			AsyncHttpClient.BoundRequestBuilder builder = http.preparePost(url);
			builder.setBodyEncoding("UTF-8");
			builder.setBody(jsonStrParam);
			Future<Response> future = builder.execute();
			body = future.get().getResponseBody("UTF-8");
			return body;
		} catch (Exception e) {
			throw e;
		} finally {
			if (http != null)
				http.close();
		}
	}

	/**
	 * 发送post请求
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static String post(String url) throws Exception {

		String body = null;
		AsyncHttpClient http = null;
		try {
			http = new AsyncHttpClient();
			AsyncHttpClient.BoundRequestBuilder builder = http.preparePost(url);
			builder.setBodyEncoding("UTF-8");
			Future<Response> future = builder.execute();
			body = future.get().getResponseBody("UTF-8");
			return body;
		} catch (Exception e) {
			throw e;
		} finally {
			if (http != null)
				http.close();
		}
	}

	/**
	 * 上传媒体文件
	 * 
	 * @param url
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 * @throws KeyManagementException
	 */
	public static String upload(String url, final File file) throws Exception {

		AsyncHttpClient http = new AsyncHttpClient();
		AsyncHttpClient.BoundRequestBuilder builder = http.preparePost(url);
		builder.setBodyEncoding("UTF-8");
		String BOUNDARY = "----WebKitFormBoundaryiDGnV9zdZA1eM1yL"; // 定义数据分隔线
		builder.setHeader("connection", "Keep-Alive");
		builder.setHeader("user-agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.107 Safari/537.36");
		builder.setHeader("Charsert", "UTF-8");
		builder.setHeader("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
		final byte[] end_data = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();// 定义最后数据分隔线
		builder.setBody(new Request.EntityWriter() {

			@Override
			public void writeEntity(OutputStream out) throws IOException {

				String BOUNDARY = "----WebKitFormBoundaryiDGnV9zdZA1eM1yL"; // 定义数据分隔线
				StringBuilder sb = new StringBuilder();
				sb.append("--");
				sb.append(BOUNDARY);
				sb.append("\r\n");
				sb.append("Content-Disposition: form-data;name=\"media\";filename=\"" + file.getName() + "\"\r\n");
				sb.append("Content-Type:application/octet-stream\r\n\r\n");
				byte[] data = sb.toString().getBytes();
				out.write(data);
				DataInputStream fs = new DataInputStream(new FileInputStream(file));
				int bytes = 0;
				byte[] bufferOut = new byte[1024];
				while ((bytes = fs.read(bufferOut)) != -1) {
					out.write(bufferOut, 0, bytes);
				}
				out.write("\r\n".getBytes()); // 多个文件时，二个文件之间加入这个
				fs.close();
				out.write(end_data);
				out.flush();
				out.close();
			}
		});

		Future<Response> f = builder.execute();
		String body = f.get().getResponseBody("UTF-8");
		http.close();
		return body;
	}

	public static Map<String,String> getLatAndLonByAddress(String address,String txnId){
		Map<String,String> feedbackMap = new HashMap();
		try {
			Map<String, String> paramMap = new HashMap();
			paramMap.put("key","3031adf00db69593ba5536066008983b");
			paramMap.put("address",address);
			String s = HttpRequestUtil.get("https://restapi.amap.com/v3/geocode/geo",paramMap);
			Geo geo = JsonUtils.parseJSON(s, Geo.class);
			if (geo.getStatus().equals("1") && geo.getInfo().equals("OK")){
				geo.getGeocodes().stream().forEach(goe ->{
					feedbackMap.put("china:lng:" + txnId,goe.getLocation().split(",")[0]);
					feedbackMap.put("china:lat:" + txnId,goe.getLocation().split(",")[1]);
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return feedbackMap;
	}
}