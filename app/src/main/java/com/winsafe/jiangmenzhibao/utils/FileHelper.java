package com.winsafe.jiangmenzhibao.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import org.apache.http.util.EncodingUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * 文件上传下载类
 * 
 * @author Aaron.Zhao
 */
public class FileHelper extends ServerHelper {
	private final static int CONNECT_TIME_OUT = 5 * 1000;
	private final static int READ_TIME_OUT = 5 * 1000;
	/** 服务器返回码相关 */
	private final static String RETURN_CODE = "0,200";

	private FileHelper() {
	}

	/**
	 * 获取指定数目的分隔符
	 * 
	 * @param num
	 *            指定数目
	 * @return 返回分隔符
	 */
	private static String getSeparator(int num) {
		String result = "";

		if (num > 0) {
			String strSeparator = "-";
			for (int i = 0; i < num; i++) {
				result += strSeparator;
			}
		}

		return result;
	}

	/**
	 * 上传XML文件
	 * 
	 * @param targetURL
	 *            上传文件的目标地址
	 * @param xml
	 *            xml文件
	 * @param encoding
	 *            编码
	 * @return 返回上传成功后的服务端返回的数据
	 */
	public static byte[] uploadXML(String targetURL, String xml, String encoding) {
		byte[] data;
		URL url;
		HttpURLConnection conn;
		OutputStream outStream = null;
		try {
			data = xml.getBytes(encoding);
			url = new URL(targetURL);

			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type", "text/xml; charset=" + encoding);
			conn.setRequestProperty("Content-Length", String.valueOf(data.length));
			conn.setConnectTimeout(5 * 1000);

			outStream = conn.getOutputStream();
			outStream.write(data);
			outStream.flush();
			if (conn.getResponseCode() == 200) {
				return readStream(conn.getInputStream());
			}
		} catch (UnsupportedEncodingException e) {
			LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
					"UnsupportedEncodingException:" + e.getMessage(), true);
		} catch (MalformedURLException e) {
			LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
					"MalformedURLException:" + e.getMessage(), true);
		} catch (IOException e) {
			LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
					"IOException:" + e.getMessage(), true);
		} finally {
			if (outStream != null) {
				try {
					outStream.close();
				} catch (IOException e) {
					LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
							"IOException:" + e.getMessage(), true);
				}
			}
		}

		return null;
	}

	/**
	 * 上传文件
	 * 
	 * @param targetURL
	 *            上传文件的目标地址
	 * @param formFile
	 *            格式化的文件
	 * @return 返回上传结果：true-成功；false-失败
	 */
	public static boolean uploadFileByHttp(String targetURL, FormFile formFile) {
		boolean result = false;

		result = uploadFileByHttp(targetURL, null, formFile);

		return result;
	}

	/**
	 * 上传文件
	 * 
	 * @param targetURL
	 *            上传文件的目标地址
	 *            格式化的文件
	 * @return 返回上传结果：true-成功；false-失败
	 */
	public static boolean uploadFileByHttp(String targetURL, FormFile[] formFiles) {
		boolean result = false;

		result = uploadFileByHttp(targetURL, null, formFiles);

		return result;
	}

	/**
	 * 多文件上传
	 * 
	 * @param targetURL
	 * @param params
	 * @param formFiles
	 * @return
	 */
	public static boolean uploadFileByHttp(String targetURL, Map<String, String> params, FormFile[] formFiles) {
		boolean result = false;

		final String strRandom = "7da2137580612";
		final String BOUNDARY = getSeparator(27) + strRandom; // 边界标识 随机生成
		final String LINE_END = "\r\n";
		final String CONTENT_TYPE = "multipart/form-data"; // 内容类型
		final String STAR_TLINE = getSeparator(2) + BOUNDARY + LINE_END;
		final String END_LINE = getSeparator(2) + BOUNDARY + getSeparator(2) + LINE_END;
		final String CHARSET = ConfigHelper.TextCode.UTF_8;
		final int TIME_OUT = 10 * 60 * 1000;

		DataOutputStream dataOutputStream = null;
		BufferedReader reader = null;
		try {
			HttpsURLConnection httpsURLConnection = null;
			HttpURLConnection httpURLConnection = null;

			URL url = new URL(targetURL);

			if (targetURL.substring(0, 5).equalsIgnoreCase("https")) {
				SSLContext context = SSLContext.getInstance("TLS");
				context.init(null, new TrustManager[] { new TrustAllManager() }, null);
				HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
				HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
					@Override
					public boolean verify(String arg0, SSLSession arg1) {
						return true;
					}
				});

				httpsURLConnection = (HttpsURLConnection) url.openConnection();
				httpsURLConnection.setReadTimeout(TIME_OUT);
				httpsURLConnection.setConnectTimeout(TIME_OUT);
				httpsURLConnection.setDoInput(true); // 允许输入流
				httpsURLConnection.setDoOutput(true); // 允许输出流
				httpsURLConnection.setUseCaches(false); // 不允许使用缓存
				httpsURLConnection.setRequestMethod("POST"); // 请求方式
				httpsURLConnection.setRequestProperty("Charset", CHARSET); // 设置编码
				httpsURLConnection.setRequestProperty("connection", "keep-alive");
				httpsURLConnection.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);

				dataOutputStream = new DataOutputStream(httpsURLConnection.getOutputStream());
			} else {
				httpURLConnection = (HttpURLConnection) url.openConnection();
				httpURLConnection.setReadTimeout(TIME_OUT);
				httpURLConnection.setConnectTimeout(TIME_OUT);
				httpURLConnection.setDoInput(true); // 允许输入流
				httpURLConnection.setDoOutput(true); // 允许输出流
				httpURLConnection.setUseCaches(false); // 不允许使用缓存
				httpURLConnection.setRequestMethod("POST"); // 请求方式
				httpURLConnection.setRequestProperty("Charset", CHARSET); // 设置编码
				httpURLConnection.setRequestProperty("connection", "keep-alive");
				httpURLConnection.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);

				dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
			}

			// 参数不为空，包装参数并上传
			if (params != null) {
				StringBuilder textEntity = new StringBuilder();
				for (Map.Entry<String, String> entry : params.entrySet()) {
					textEntity.append(STAR_TLINE);
					textEntity.append(String.format("Content-Disposition: form-data; name=\"%s\"%s%s", entry.getKey(),
							LINE_END, LINE_END));
					textEntity.append(StringHelper.nullValue(entry.getValue(), ""));
					textEntity.append(LINE_END);
				}
				dataOutputStream.write(textEntity.toString().getBytes());
			}

			// 当文件不为空，把文件包装并且上传
			if (formFiles != null) {
				for (FormFile formFile : formFiles) {
					// 空文件过滤
					if (formFile.getInputStream() == null) {
						continue;
					}
					StringBuffer fileEntity = new StringBuffer();
					fileEntity.append(STAR_TLINE);
					fileEntity.append(String.format("Content-Disposition: form-data; name=\"%s\"; filename=\"%s\"%s",
							formFile.getParamName(), formFile.getFileName(), LINE_END));
					fileEntity.append("Content-Type: " + formFile.getContentType() + "; charset=" + CHARSET + LINE_END);
					fileEntity.append(LINE_END);
					dataOutputStream.write(fileEntity.toString().getBytes());

					if (formFile.getInputStream() != null) {
						byte[] bytes = new byte[1024];
						int length = 0;
						while ((length = formFile.getInputStream().read(bytes)) != -1) {
							dataOutputStream.write(bytes, 0, length);
						}
						formFile.getInputStream().close();
					} else {
						dataOutputStream.write(formFile.getData(), 0, formFile.getData().length);
					}
					dataOutputStream.write(LINE_END.getBytes());
				}

				dataOutputStream.write(END_LINE.getBytes());
				dataOutputStream.flush();

				int responseCode = 0;
				if (targetURL.substring(0, 5).equalsIgnoreCase("https"))
					responseCode = httpsURLConnection.getResponseCode();
				else
					responseCode = httpURLConnection.getResponseCode();

				if (responseCode == 200) {
					InputStream returnInputStream;
					if (targetURL.substring(0, 5).equalsIgnoreCase("https"))
						returnInputStream = httpsURLConnection.getInputStream();
					else
						returnInputStream = httpURLConnection.getInputStream();

					reader = new BufferedReader(new InputStreamReader(returnInputStream));
					String line = "";
					StringBuffer bulder = new StringBuffer();
					while ((line = reader.readLine()) != null) {
						bulder.append(line);
					}
					
					// 根据服务器返回码，判断是否上传成功
					if (TextUtils.isEmpty(bulder.toString())) {
						return result;
					}
					
					JSONObject jsonObject = JSONHelper.getJSONObject(bulder.toString());
					if (RETURN_CODE.contains(JSONHelper.getString(jsonObject, "returnCode"))) {
						result = true;
					}
				}
			}
		} catch (KeyManagementException e) {
			LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
					"KeyManagementException:" + e.getMessage(), true);
		} catch (NoSuchAlgorithmException noSuchAlgorithmException) {
			LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
					"NoSuchAlgorithmException:" + noSuchAlgorithmException.getMessage(), true);
		} catch (MalformedURLException malformedURLException) {
			LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
					"MalformedURLException:" + malformedURLException.getMessage(), true);
		} catch (IOException iOException) {
			LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
					"IOException:" + iOException.getMessage(), true);
		} finally {
			try {
				if (dataOutputStream != null) {
					dataOutputStream.close();
				}
				if (reader != null) {
					reader.close();
				}
				if (formFiles != null) {
					for (int i = 0; i < formFiles.length; i++) {
						if (formFiles[i].getInputStream() != null) {
							formFiles[i].getInputStream().close();
						}
					}
				}
			} catch (IOException e) {
				LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
						"IOException:" + e.getMessage(), true);
			}
		}

		return result;
	}

	public static String uploadFileByHttp1(String targetURL, Map<String, String> params, FormFile[] formFiles) {
		String result = "";

		final String strRandom = "7da2137580612";
		final String BOUNDARY = getSeparator(27) + strRandom; // 边界标识 随机生成
		final String LINE_END = "\r\n";
		final String CONTENT_TYPE = "multipart/form-data"; // 内容类型
		final String STAR_TLINE = getSeparator(2) + BOUNDARY + LINE_END;
		final String END_LINE = getSeparator(2) + BOUNDARY + getSeparator(2) + LINE_END;
		final String CHARSET = ConfigHelper.TextCode.UTF_8;
		final int TIME_OUT = 10 * 60 * 1000;

		try {
			HttpsURLConnection httpsURLConnection = null;
			HttpURLConnection httpURLConnection = null;
			DataOutputStream dataOutputStream = null;

			URL url = new URL(targetURL);

			if (targetURL.substring(0, 5).equalsIgnoreCase("https")) {
				SSLContext context = SSLContext.getInstance("TLS");
				context.init(null, new TrustManager[] { new TrustAllManager() }, null);
				HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
				HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
					@Override
					public boolean verify(String arg0, SSLSession arg1) {
						return true;
					}
				});

				httpsURLConnection = (HttpsURLConnection) url.openConnection();
				httpsURLConnection.setReadTimeout(TIME_OUT);
				httpsURLConnection.setConnectTimeout(TIME_OUT);
				httpsURLConnection.setDoInput(true); // 允许输入流
				httpsURLConnection.setDoOutput(true); // 允许输出流
				httpsURLConnection.setUseCaches(false); // 不允许使用缓存
				httpsURLConnection.setRequestMethod("POST"); // 请求方式
				httpsURLConnection.setRequestProperty("Charset", CHARSET); // 设置编码
				httpsURLConnection.setRequestProperty("connection", "keep-alive");
				httpsURLConnection.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);

				dataOutputStream = new DataOutputStream(httpsURLConnection.getOutputStream());
			}
			else {
				httpURLConnection = (HttpURLConnection) url.openConnection();
				httpURLConnection.setReadTimeout(TIME_OUT);
				httpURLConnection.setConnectTimeout(TIME_OUT);
				httpURLConnection.setDoInput(true); // 允许输入流
				httpURLConnection.setDoOutput(true); // 允许输出流
				httpURLConnection.setUseCaches(false); // 不允许使用缓存
				httpURLConnection.setRequestMethod("POST"); // 请求方式
				httpURLConnection.setRequestProperty("Charset", CHARSET); // 设置编码
				httpURLConnection.setRequestProperty("connection", "keep-alive");
				httpURLConnection.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);

				dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
			}

			// 参数不为空，包装参数并上传
			if (params != null) {
				StringBuilder textEntity = new StringBuilder();
				for (Map.Entry<String, String> entry : params.entrySet()) {
					textEntity.append(STAR_TLINE);
					textEntity.append(String.format("Content-Disposition: form-data; name=\"%s\"%s%s", entry.getKey(), LINE_END, LINE_END));
					textEntity.append(StringHelper.nullValue(entry.getValue(), ""));
					textEntity.append(LINE_END);
				}
				dataOutputStream.write(textEntity.toString().getBytes());
			}

			// 当文件不为空，把文件包装并且上传
			if (formFiles != null) {
				for (FormFile formFile : formFiles) {
					StringBuffer fileEntity = new StringBuffer();
					fileEntity.append(STAR_TLINE);
					fileEntity.append(String.format("Content-Disposition: form-data; name=\"%s\"; filename=\"%s\"%s", formFile.getParamName(),
							formFile.getFileName(), LINE_END));
					fileEntity.append("Content-Type: " + formFile.getContentType() + "; charset=" + CHARSET + LINE_END);
					fileEntity.append(LINE_END);
					dataOutputStream.write(fileEntity.toString().getBytes());

					if (formFile.getInputStream() != null) {
						byte[] bytes = new byte[1024];
						int length = 0;
						while ((length = formFile.getInputStream().read(bytes)) != -1) {
							dataOutputStream.write(bytes, 0, length);
						}
						formFile.getInputStream().close();
					}
					else {
						dataOutputStream.write(formFile.getData(), 0, formFile.getData().length);
					}
					dataOutputStream.write(LINE_END.getBytes());
				}

				dataOutputStream.write(END_LINE.getBytes());
				dataOutputStream.flush();
				dataOutputStream.close();

				int responseCode = 0;
				if (targetURL.substring(0, 5).equalsIgnoreCase("https"))
					responseCode = httpsURLConnection.getResponseCode();
				else
					responseCode = httpURLConnection.getResponseCode();

				if (responseCode == 200) {
					InputStream returnInputStream;
					if (targetURL.substring(0, 5).equalsIgnoreCase("https"))
						returnInputStream = httpsURLConnection.getInputStream();
					else
						returnInputStream = httpURLConnection.getInputStream();

					BufferedReader reader = new BufferedReader(new InputStreamReader(returnInputStream));
					String line = "";
					StringBuffer bulder = new StringBuffer();
					while ((line = reader.readLine()) != null) {
						bulder.append(line);
					}
					reader.close();

					result = bulder.toString();
				}
			}
		}
		catch (KeyManagementException e) {
			LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()), "KeyManagementException:" + e.getMessage(),
					true);
		}
		catch (NoSuchAlgorithmException e) {
			LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()), "NoSuchAlgorithmException:" + e.getMessage(),
					true);
		}
		catch (MalformedURLException e) {
			LogHelper
					.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()), "MalformedURLException:" + e.getMessage(), true);
		}
		catch (IOException e) {
			LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()), "IOException:" + e.getMessage(), true);
		}

		return result;
	}

	/**
	 * 单文件上传
	 * 
	 * @param targetURL
	 * @param params
	 * @param formFile
	 * @return
	 */
	public static boolean uploadFileByHttp(String targetURL, Map<String, String> params, FormFile formFile) {
		boolean result = false;

		final String strRandom = "7da2137580612";
		final String BOUNDARY = getSeparator(27) + strRandom; // 边界标识 随机生成
		final String LINE_END = "\r\n";
		final String CONTENT_TYPE = "multipart/form-data"; // 内容类型
		final String STAR_TLINE = getSeparator(2) + BOUNDARY + LINE_END;
		final String END_LINE = getSeparator(2) + BOUNDARY + getSeparator(2) + LINE_END;
		final String CHARSET = ConfigHelper.TextCode.UTF_8;
		final int TIME_OUT = 10 * 60 * 1000;
		InputStream inputStream = null;
		DataOutputStream dataOutputStream = null;
		try {
			HttpsURLConnection httpsURLConnection = null;
			HttpURLConnection httpURLConnection = null;

			URL url = new URL(targetURL);

			if (targetURL.substring(0, 5).equalsIgnoreCase("https")) {
				SSLContext context = SSLContext.getInstance("TLS");
				context.init(null, new TrustManager[] { new TrustAllManager() }, null);
				HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
				HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
					@Override
					public boolean verify(String arg0, SSLSession arg1) {
						return true;
					}
				});

				httpsURLConnection = (HttpsURLConnection) url.openConnection();
				httpsURLConnection.disconnect();

				httpsURLConnection.setReadTimeout(TIME_OUT);
				httpsURLConnection.setConnectTimeout(TIME_OUT);
				httpsURLConnection.setDoInput(true); // 允许输入流
				httpsURLConnection.setDoOutput(true); // 允许输出流
				httpsURLConnection.setUseCaches(false); // 不允许使用缓存
				httpsURLConnection.setRequestMethod("POST"); // 请求方式
				httpsURLConnection.setRequestProperty("Charset", CHARSET); // 设置编码
				httpsURLConnection.setRequestProperty("connection", "keep-alive");
				httpsURLConnection.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);

				httpsURLConnection.connect();

				dataOutputStream = new DataOutputStream(httpsURLConnection.getOutputStream());
			} else {
				httpURLConnection = (HttpURLConnection) url.openConnection();
				httpURLConnection.disconnect();

				httpURLConnection.setReadTimeout(TIME_OUT);
				httpURLConnection.setConnectTimeout(TIME_OUT);
				httpURLConnection.setDoInput(true); // 允许输入流
				httpURLConnection.setDoOutput(true); // 允许输出流
				httpURLConnection.setUseCaches(false); // 不允许使用缓存
				httpURLConnection.setRequestMethod("POST"); // 请求方式
				httpURLConnection.setRequestProperty("Charset", CHARSET); // 设置编码
				httpURLConnection.setRequestProperty("connection", "keep-alive");
				httpURLConnection.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);

				httpURLConnection.connect();

				dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
			}

			// 参数不为空，包装参数并上传
			if (params != null) {
				StringBuilder textEntity = new StringBuilder();
				for (Map.Entry<String, String> entry : params.entrySet()) {
					textEntity.append(STAR_TLINE);
					textEntity.append(String.format("Content-Disposition: form-data; name=\"%s\"%s%s", entry.getKey(),
							LINE_END, LINE_END));
					textEntity.append(entry.getValue());
					textEntity.append(LINE_END);
				}
				dataOutputStream.write(textEntity.toString().getBytes());
			}

			// 当文件不为空，把文件包装并且上传
			if (formFile != null) {
				// 空文件过滤
				if (formFile.getInputStream() != null) {
					StringBuffer fileEntity = new StringBuffer();
					fileEntity.append(STAR_TLINE);
					fileEntity.append(String.format("Content-Disposition: form-data; name=\"%s\"; filename=\"%s\"%s",
							formFile.getParamName(), formFile.getFileName(), LINE_END));
					fileEntity.append("Content-Type: " + formFile.getContentType() + "; charset=" + CHARSET + LINE_END);
					fileEntity.append(LINE_END);
					dataOutputStream.write(fileEntity.toString().getBytes());

					inputStream = formFile.getInputStream();
					byte[] bytes = new byte[1024];
					int length = 0;
					while ((length = inputStream.read(bytes)) != -1) {
						dataOutputStream.write(bytes, 0, length);
					}
				}

				dataOutputStream.write(LINE_END.getBytes());
				dataOutputStream.write(END_LINE.getBytes());

				dataOutputStream.flush();

				int responseCode = 0;
				if (targetURL.substring(0, 5).equalsIgnoreCase("https"))
					responseCode = httpsURLConnection.getResponseCode();
				else
					responseCode = httpURLConnection.getResponseCode();

				if (responseCode == 200) {
					InputStream returnInputStream;
					if (targetURL.substring(0, 5).equalsIgnoreCase("https"))
						returnInputStream = httpsURLConnection.getInputStream();
					else
						returnInputStream = httpURLConnection.getInputStream();
					StringBuffer bulder = new StringBuffer();
					int len;
					while ((len = returnInputStream.read()) != -1) {
						bulder.append((char) len);
					}

					result = true;
				}
			}
		} catch (KeyManagementException e) {
			e.printStackTrace();
			LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
					"KeyManagementException:" + e.getMessage(), true);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
					"NoSuchAlgorithmException:" + e.getMessage(), true);
		} catch (MalformedURLException e) {
			LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
					"MalformedURLException:" + e.getMessage(), true);
		} catch (IOException e) {
			LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
					"IOException:" + e.getMessage(), true);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
							"IOException:" + e.getMessage(), true);
				}
			}
			if (dataOutputStream != null) {
				try {
					dataOutputStream.close();
				} catch (IOException e) {
					LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
							"IOException:" + e.getMessage(), true);
				}
			}
			if (formFile != null) {
				try {
					if (formFile.getInputStream() != null) {
						formFile.getInputStream().close();
					}
				} catch (IOException e) {
					LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
							"IOException:" + e.getMessage(), true);
				}
			}
		}

		return result;
	}

	/**
	 * 上传文件
	 * 
	 * @param targetURL
	 *            上传文件的目标地址
	 * @param formFile
	 *            格式化的文件
	 * @return 返回上传结果：true-成功；false-失败
	 */
	public static boolean uploadFileBySocket(String targetURL, Map<String, String> params, FormFile formFile) {
		boolean result = false;

		result = uploadFileBySocket(targetURL, params, new FormFile[] { formFile });

		return result;
	}

	/**
	 * 上传文件（直接通过HTTP协议提交数据到服务器,实现如下面表单提交功能） <FORM METHOD=POST
	 * ACTION="http://192.168.1.101:8083/upload/servlet/UploadServlet"
	 * enctype="multipart/form-data"> <input type="text" name="name"> <input
	 * type="file" name="zip"/> </FORM>
	 * 
	 * @param targetURL
	 *            上传文件的目标地址
	 *            格式化的文件
	 * @return 返回上传结果：true-成功；false-失败
	 */
	public static boolean uploadFileBySocket(String targetURL, Map<String, String> params, FormFile[] formFiles) {
		boolean result = false;

		final String strRandom = "7da2137580612";
		final String LINE_END = "\r\n";
		// 数据分隔线
		final String BOUNDARY = getSeparator(27) + strRandom;
		// 数据开始、结束标志
		final String startLine = getSeparator(2) + BOUNDARY + LINE_END;
		final String endLine = getSeparator(2) + BOUNDARY + getSeparator(2) + LINE_END;

		// 下面两个for循环都是为了得到数据长度参数，依据表单的类型而定
		// 首先得到文件类型数据的总长度(包括文件分割线)
		int fileTypeDataLength = 0;
		for (FormFile uploadFile : formFiles) {
			StringBuilder fileExplain = new StringBuilder();
			fileExplain.append(startLine);
			fileExplain.append(String.format("Content-Disposition: form-data;name=\"%s\";filename=\"%s\"%s",
					uploadFile.getParamName(), uploadFile.getFileName(), LINE_END));
			fileExplain.append(String.format("Content-Type: %s", uploadFile.getContentType() + LINE_END + LINE_END));
			fileExplain.append(LINE_END);
			fileTypeDataLength += fileExplain.length();
			if (uploadFile.getInputStream() != null)
				fileTypeDataLength += uploadFile.getFile().length();
			else
				fileTypeDataLength += uploadFile.getData().length;
		}
		// 再构造文本类型参数的实体数据
		StringBuilder textEntity = new StringBuilder();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			textEntity.append(startLine);
			textEntity.append(String.format("Content-Disposition: form-data; name=\"%s\"%s%s", entry.getKey(),
					LINE_END, LINE_END));
			textEntity.append(entry.getValue());
			textEntity.append(LINE_END);
		}

		// 计算传输给服务器的实体数据总长度(文本总长度+数据总长度+分隔符)
		int dataLength = fileTypeDataLength + textEntity.toString().getBytes().length + endLine.getBytes().length;

		OutputStream outputStream = null;
		BufferedReader reader = null;
		Socket socket = null;
		try {
			URL url = new URL(targetURL);
			int port = url.getPort() == -1 ? 80 : url.getPort();
			socket = new Socket(InetAddress.getByName(url.getHost()), port);
			outputStream = socket.getOutputStream();
			// 下面完成HTTP请求头的发送
			String requestmethod = String.format("POST %s HTTP/1.1 %s", url.getPath(), LINE_END);
			outputStream.write(requestmethod.getBytes());
			String accept = "Accept: image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*"
					+ LINE_END;
			outputStream.write(accept.getBytes());
			String language = "Accept-Language: zh-CN" + LINE_END;
			outputStream.write(language.getBytes());
			String contenttype = "Content-Type: multipart/form-data; boundary=" + BOUNDARY + LINE_END;
			outputStream.write(contenttype.getBytes());
			String contentlength = "Content-Length: " + dataLength + LINE_END;
			outputStream.write(contentlength.getBytes());
			String alive = "Connection: Keep-Alive" + LINE_END;
			outputStream.write(alive.getBytes());
			String host = String.format("Host: %s:%s%s", url.getHost(), port, LINE_END);
			outputStream.write(host.getBytes());
			// 写完HTTP请求头后根据HTTP协议再写一个回车换行
			outputStream.write(LINE_END.getBytes());
			// 把所有文本类型的实体数据发送出来
			outputStream.write(textEntity.toString().getBytes());

			// 把所有文件类型的实体数据发送出来
			for (FormFile uploadFile : formFiles) {
				StringBuilder fileEntity = new StringBuilder();
				fileEntity.append(startLine);
				fileEntity.append(String.format("Content-Disposition: form-data;name=\"%s\";filename=\"%s\"%s",
						uploadFile.getParamName(), uploadFile.getFileName(), LINE_END));
				fileEntity.append("Content-Type: " + uploadFile.getContentType() + LINE_END + LINE_END);
				outputStream.write(fileEntity.toString().getBytes());

				if (uploadFile.getInputStream() != null) {
					byte[] buffer = new byte[1024];
					int len = 0;
					while ((len = uploadFile.getInputStream().read(buffer, 0, 1024)) != -1) {
						outputStream.write(buffer, 0, len);
					}
					uploadFile.getInputStream().close();
				} else {
					outputStream.write(uploadFile.getData(), 0, uploadFile.getData().length);
				}
				outputStream.write(LINE_END.getBytes());
			}
			outputStream.write(endLine.getBytes());// 发送数据结束标志，表示数据已经结束

			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			// 读取web服务器返回的数据，判断请求码是否为200，如果不是200，代表请求失败
			if (reader.readLine().indexOf("200") == -1)
				result = false;
			else
				result = true;

			outputStream.flush();
		} catch (MalformedURLException e) {
			LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
					"MalformedURLException:" + e.getMessage(), true);
		} catch (IOException e) {
			LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
					"IOException:" + e.getMessage(), true);
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
							"IOException:" + e.getMessage(), true);
				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
							"IOException:" + e.getMessage(), true);

				}
			}
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
							"IOException:" + e.getMessage(), true);
				}
			}

		}

		return result;
	}

	/**
	 * 下载文件
	 * 
	 * @param fileFullPath
	 * @param newFileName
	 * @param strUrl
	 * @param encode
	 * @return
	 */
	public static boolean downloadFile(String fileFullPath, String newFileName, String strUrl, String encode) {
		boolean isSuccess = false;

		InputStream is = null;
		HttpURLConnection con = null;
		OutputStream os = null;
		try {
			// 判断路径是否存在
			File filePath = new File(fileFullPath);
			if (!filePath.exists()) {
				filePath.mkdirs();
			}

			// 判断文件是否存在
			File file = new File(filePath + File.separator + newFileName);
			// 如果目标文件已经存在，则删除。
			if (file.exists()) {
				file.delete();
			}

			// 构造URL
			URL url = new URL(strUrl);
			// 打开连接
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			con.setConnectTimeout(CONNECT_TIME_OUT);
			con.setReadTimeout(READ_TIME_OUT);
			con.connect();

			if (con.getResponseCode() == 200) {
				// 获得文件的长度
//				int contentLength = con.getContentLength();
				// 输入流
				is = con.getInputStream();
				// 1K的数据缓冲
				byte[] bs = new byte[1024];
				// 读取到的数据长度
				int len;
				// 输出的文件流
				os = new FileOutputStream(filePath + File.separator + newFileName);
				// 开始读取
				while ((len = is.read(bs)) != -1) {
					os.write(bs, 0, len);
				}

				isSuccess = true;

			}

		} catch (Exception e) {
			LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
					"IOException:" + e.getMessage(), true);
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (os != null) {
					os.close();
				}
				if (con != null) {
					con.disconnect();
				}
			} catch (IOException e) {
				LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
						"IOCloseException:" + e.getMessage(), true);
			}
		}

		return isSuccess;
	}

	/**
	 * 下载文件
	 * 
	 * @param fileFullPath
	 *            下载地址
	 * @param encode
	 *            指定下载文件内容的编码
	 * @return 返回下载文件的内容
	 */
	public static String downloadFileContent(String fileFullPath, String encode) {
		String result = "";

		InputStream inputStream = null;
		try {
			URL url = new URL(fileFullPath);
			URLConnection urlConnection = url.openConnection();
			urlConnection.connect();
			inputStream = urlConnection.getInputStream();
			result = StringHelper.toString(inputStream, encode);
		} catch (MalformedURLException e) {
			LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
					"MalformedURLException:" + e.getMessage(), true);
		} catch (IOException e) {
			LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
					"IOException:" + e.getMessage(), true);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
							"IOException:" + e.getMessage(), true);
				}
			}
		}

		return result;
	}

	/**
	 * 下载文件
	 * 
	 * @param fileFullPath
	 *            下载地址
	 *            指定下载文件内容的编码
	 * @return 返回下载文件的流
	 */
	public static InputStream downloadFileStream(String fileFullPath) {
		InputStream result = null;

		try {
			URL url = new URL(fileFullPath);
			URLConnection urlConnection = url.openConnection();
			urlConnection.connect();
			InputStream inputStream = urlConnection.getInputStream();

			result = inputStream;
		} catch (MalformedURLException e) {
			LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
					"MalformedURLException:" + e.getMessage(), true);
		} catch (IOException e) {
			LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
					"IOException:" + e.getMessage(), true);
		}

		return result;
	}

	/**
	 * 向手机的存储空间中写入文件
	 * 
	 * @param context
	 *            上下文
	 * @param fileName
	 *            文件的全路径
	 * @param content
	 *            写入的内容
	 */
	public static void writeFile(Context context, String fileName, String content) {
		FileOutputStream out = null;
		try {
			byte[] buffer = content.getBytes();
			out = context.openFileOutput(fileName, Context.MODE_PRIVATE);
			out.write(buffer);
			out.close();
		} catch (Exception e) {
			LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
					"Exception:" + e.getMessage(), true);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
							"Exception:" + e.getMessage(), true);
				}
			}
		}
	}

	/**
	 * 向手机的存储空间中写入文件,模式是mode_private+append 将数据批量写入文件使用
	 * 
	 * @param context
	 *            上下文
	 * @param fileName
	 *            文件的全路径
	 * @param content
	 *            写入的内容
	 */
	public static void writeFileWithModeAppend(Context context, String fileName, String content) {
		FileOutputStream out = null;
		try {
			byte[] buffer = content.getBytes();
			out = context.openFileOutput(fileName, Context.MODE_PRIVATE + Context.MODE_APPEND);
			out.write(buffer);
			out.close();
		} catch (Exception e) {
			LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
					"Exception:" + e.getMessage(), true);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
							"Exception:" + e.getMessage(), true);
				}
			}
		}
	}

	/**
	 * 读取手机存储空间中的文件
	 * 
	 * @param context
	 *            上下文
	 * @param fileName
	 *            文件全路径
	 * @return 返回读取到的内容
	 */
	public static String readFile(Context context, String fileName) {
		String result = "";
		FileInputStream mFileInputStream = null;
		try {
			mFileInputStream = context.openFileInput(fileName);
			int lenght = mFileInputStream.available();
			byte[] buffer = new byte[lenght];
			mFileInputStream.read(buffer);
			result = EncodingUtils.getString(buffer, "UTF-8");
		} catch (Exception e) {
			LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
					"Exception:" + e.getMessage(), true);
		} finally {
			if (mFileInputStream != null) {
				try {
					mFileInputStream.close();
				} catch (IOException e) {
					LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
							"Exception:" + e.getMessage(), true);
				}
			}
		}

		return result;
	}

	/**
	 * 删除手机存储空间中文件
	 * 
	 * @param context
	 *            上下文
	 * @param fileName
	 *            文件全路径
	 * @return 返回删除结果：删除成功-true；删除失败-false
	 */
	public static boolean delFile(Context context, String fileName) {
		boolean result = false;

		try {
			result = context.deleteFile(fileName);
		} catch (Exception e) {
			LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
					"Exception:" + e.getMessage(), true);
		}

		return result;
	}

	/**
	 * 判断SD卡是否存在
	 * 
	 * @return true-存在；false-不存在
	 */
	public static boolean isSDExist() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

	/**
	 * 获取SD卡路径（不带/）
	 * 
	 * @return 返回SD卡路径
	 */
	public static String getSDPath() {
		String sdPath = "";

		File sdDir = null;
		if (isSDExist()) {
			sdDir = Environment.getExternalStorageDirectory();
		}

		if (sdDir != null) {
			sdPath = sdDir.getAbsolutePath();
		}

		return sdPath;
	}

	/**
	 * 在SD卡上创建文件
	 * 
	 * @param fileName
	 *            文件名称
	 * @param dir
	 *            目录名称
	 * @return 返回创建的文件
	 */
	public static File creatFileInSDCard(String fileName, String dir) {
		File file = null;
		try {
			file = new File(getSDPath() + dir + File.separator + fileName);
			file.createNewFile();
		} catch (IOException e) {
			LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
					"IOException:" + e.getMessage(), true);
		}
		return file;
	}

	/**
	 * 在SD卡上创建目录
	 * 
	 * @param dir
	 *            目录名称
	 * @return 返回创建的目录
	 */
	public static File createSDDir(String dir) {
		File dirFile = new File(getSDPath() + File.separator + dir);
		dirFile.mkdirs();
		return dirFile;
	}

	/**
	 * 判断SD卡上的文件夹是否存在
	 * 
	 * @param fileName
	 *            文件名称
	 * @param path
	 *            路径
	 * @return true-存在；false-不存在
	 */
	public static boolean isFileExist(String fileName, String path) {
		File file = new File(getSDPath() + File.separator + path + File.separator + fileName);
		return file.exists();
	}

	/**
	 * 将一个InputStream里面的数据写入SD卡中
	 * 
	 * @param path
	 *            文件路径
	 * @param fileName
	 *            文件名称
	 * @param input
	 *            输入流
	 * @return 返回文件
	 */
	public static File writeSDFromInput(String path, String fileName, InputStream input) {
		File file = null;
		OutputStream output = null;
		try {
			createSDDir(path);
			file = creatFileInSDCard(fileName, path);
			output = new FileOutputStream(file);
			byte buffer[] = new byte[4 * 1024];
			int i;
			while (-1 != (i = input.read(buffer))) {
				output.write(buffer, 0, i);
			}
			output.flush();
		} catch (FileNotFoundException e) {
			LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
					"FileNotFoundException:" + e.getMessage(), true);
		} catch (IOException e) {
			LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
					"IOException:" + e.getMessage(), true);
		} finally {
			try {
				if (output != null) {
					output.close();
				}
			} catch (IOException e) {
				LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
						"IOException:" + e.getMessage(), true);
			}
		}
		return file;
	}

	/**
	 * 将一个InputStream里面的数据写入手机默认路径中
	 * 
	 * @param filename
	 *            文件名称
	 * @param input
	 *            输入流
	 * @param context
	 *            上下文
	 */
	public static void writeSDFromInput(String filename, InputStream input, Context context) {
		FileOutputStream fos = null;
		try {
			fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
			byte buffer[] = new byte[4 * 1024];
			int i;
			while (-1 != (i = input.read(buffer))) {
				fos.write(buffer, 0, i);
			}
			fos.flush();
		} catch (FileNotFoundException e) {
			LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
					"FileNotFoundException:" + e.getMessage(), true);
		} catch (IOException e) {
			LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
					"IOException:" + e.getMessage(), true);
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
						"IOException:" + e.getMessage(), true);
			}
		}
	}

	/**
	 * 将字符串写到文件中并保存文件到SD卡的指定文件夹中
	 * 
	 * @param fileFolder
	 *            指定文件夹
	 * @param fileName
	 *            文件名
	 * @param fileContent
	 *            文件内容
	 */
	@SuppressLint("LongLogTag")
	public static void writeFileToSD(String fileFolder, String fileName, String fileContent) {
		FileWriter fileWriter = null;
		BufferedWriter bufferedWriter = null;
		try {
			File filePath = null;
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				filePath = new File(getSDPath() + File.separator + fileFolder);
				if (!filePath.exists()) {
					filePath.mkdirs();
				}

				File file = new File(filePath, fileName);

				fileWriter = new FileWriter(file.getAbsolutePath(), file.exists());
				bufferedWriter = new BufferedWriter(fileWriter);
				bufferedWriter.write(fileContent);
				bufferedWriter.close();
			}
		} catch (IOException e) {
			Log.e("LogHelper-->writeFileToSD-->", "IOException:" + e.getMessage().toString());

		} catch (Exception e) {
			Log.e("LogHelper-->writeFileToSD-->", "Exception:" + e.getMessage());
		} finally {
			try {
				if (fileWriter != null) {
					fileWriter.close();
				}
				if (bufferedWriter != null) {
					bufferedWriter.close();
				}
			} catch (IOException e) {
				Log.e("LogHelper-->writeFileToSD-->", "Exception:" + e.getMessage());
			}
		}
	}

	/**
	 * 读取SD卡上的txt文件
	 * 
	 * @param fileFullName
	 *            包含文件路径的文件名称
	 * @return 返回文件内容
	 */
	public static String readSDTxt(String fileFullName, String textEncoding) {
		String result = "";
		InputStream inputStream = null;
		try {
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				inputStream = new FileInputStream(new File(getSDPath() + File.separator + fileFullName));
				if (inputStream != null) {
					int length = inputStream.available();
					byte[] buffer = new byte[length];
					inputStream.read(buffer);

					result = EncodingUtils.getString(buffer, textEncoding);
					inputStream.close();
				}
			}
		} catch (FileNotFoundException e) {
			LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
					"FileNotFoundException:" + e.getMessage(), true);
		} catch (IOException e) {
			LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
					"IOException:" + e.getMessage(), true);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
							"IOException:" + e.getMessage(), true);
				}
			}
		}

		return result;
	}

	/**
	 * 删除指定路径下的文件
	 * 
	 * @param path
	 *            指定路径
	 */
	public static void deleteDir(String path) {
		File file = new File(path);
		if (file.exists()) {
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				for (File subFile : files) {
					if (subFile.isDirectory())
						deleteDir(subFile.getPath());
					else
						subFile.delete();
				}
			}
			file.delete();
		}
	}

	/**
	 * 复制单个文件
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/file.txt
	 * @param newPath
	 *            String 复制后路径 如：f:/file.txt
	 * @return boolean
	 */
	public static void copyFile(String oldPath, String newPath) {
		FileInputStream input = null;
		FileOutputStream output = null;
		try {
			File oldFile = new File(oldPath);
			if (oldFile.exists()) {
				input = new FileInputStream(oldPath);

				File folderPath = new File(newPath.substring(0, newPath.lastIndexOf("/")));
				if (!folderPath.exists())
					folderPath.mkdirs();

				output = new FileOutputStream(newPath);

				byte[] buffer = new byte[1024];
				int length = 0;
				while ((length = input.read(buffer)) != -1) {
					output.write(buffer, 0, length);
				}

				output.flush();
			}
		} catch (Exception e) {
			LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
					"Exception:" + e.getMessage(), true);
		} finally {
			try {
				if (input != null) {
					input.close();
				}
				if (output != null) {
					output.close();
				}
			} catch (IOException e) {
				LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
						e.getMessage(), true);
			}
		}
	}

	/**
	 * 将输入流写入到目标文件
	 * 
	 * @param inputStream
	 *            输入流
	 * @param fileFullPath
	 *            目标文件全路径 如：f:/file.txt
	 */
	public static void copyFile(InputStream inputStream, String fileFullPath) {
		OutputStream outputStream = null;
		try {
			if (inputStream != null) {
				File folderPath = new File(fileFullPath.substring(0, fileFullPath.lastIndexOf("/")));
				if (!folderPath.exists())
					folderPath.mkdirs();

				outputStream = new FileOutputStream(fileFullPath);
				byte[] buffer = new byte[1024];
				int length = inputStream.read(buffer);
				while (length > 0) {
					outputStream.write(buffer, 0, length);
					length = inputStream.read(buffer);
				}

				outputStream.flush();
				inputStream.close();
				outputStream.close();
			}
		} catch (IOException e) {
			LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
					e.getMessage(), true);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
							e.getMessage(), true);
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
							e.getMessage(), true);
				}
			}
		}
	}
	
//	public static String getPath(Context context, Uri uri) {
//        if ("content".equalsIgnoreCase(uri.getScheme())) {
//            String[] projection = { MediaStore.Images.Media.DATA };
//            Cursor cursor = null;
//            try {
//                cursor = context.getContentResolver().query(uri, projection,null, null, null);
//                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//                if (cursor.moveToFirst()) {
//                    return cursor.getString(column_index);
//                }
//            } catch (Exception e) {
//            	LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
//						e.getMessage(), false);
//            }
//        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
//            return uri.getPath();
//        }
//        return null;
//    }

	public static InputStream getInputStreamOfAssets(Context context, String fileName) {
		InputStream inputStream = null;

		try {
			inputStream = context.getAssets().open(fileName);
		} catch (IOException e) {
			LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
					e.getMessage(), true);
		}

		return inputStream;
	}
	
	public static String readAllText(InputStream in, String encoding) throws IOException {
		String line = null;
		BufferedReader reader = null;
		StringBuilder sb = new StringBuilder();
		try {
			reader = new BufferedReader(new InputStreamReader(in, encoding));
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString();
		} finally {
			if (reader != null) reader.close();
		}
	}
	
	/**
	 * 获取文件名
	 * @param path
	 * 路径、Url等
	 * @return
	 */
	public static String getFileName(String path) {
		if (StringHelper.isNullOrEmpty(path)) return "";
		String name = new File(path).getName();
		if (StringHelper.isNullOrEmpty(name)) return "";
		int index = name.indexOf('?');
		if (index <= 0) return name;
		return name.substring(0, index);
	}

	/**
	 * 格式化上传文件类
	 * 
	 * @author Aaron.Zhao
	 */
	public static class FormFile {
		/** 上传文件的数据 */
		private byte[] data;
		private InputStream inputStream;
		private File file;
		private String fileName;
		private String paramName;
		/** 内容类型 */
		private String contentType = "application/octet-stream";

		/**
		 * 构造函数
		 * 
		 * @param fileName
		 *            文件名字
		 * @param data
		 *            上传文件的数据数组
		 * @param paramName
		 *            服务端接收文件时文件对应的参数
		 * @param contentType
		 *            上传文件的内容类型（譬如：上传流数据时，内容格式是"application/octet-stream"）
		 */
		public FormFile(String fileName, byte[] data, String paramName, String contentType) {
			this.data = data;
			this.fileName = fileName;
			this.paramName = paramName;
			if (contentType != null)
				this.contentType = contentType;
		}

		/**
		 * 构造函数
		 * 
		 * @param fileName
		 *            文件名字
		 * @param file
		 *            上传的文件（包含文件的路径和文件的名称）
		 * @param parameterName
		 *            服务端接收文件时文件对应的参数
		 * @param contentType
		 *            上传文件的内容类型（譬如：上传流数据时，内容格式是"application/octet-stream"）
		 */
		public FormFile(String fileName, File file, String parameterName, String contentType) {
			this.fileName = fileName;
			this.paramName = parameterName;
			this.file = file;

			if (contentType != null && !contentType.equalsIgnoreCase(""))
				this.contentType = contentType;

			try {
				this.inputStream = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
						"FileNotFoundException:" + e.getMessage(), true);
			}
		}

		/**
		 * 获取文件
		 * 
		 * @return 返回文件
		 */
		public File getFile() {
			return file;
		}

		/**
		 * 获取文件流
		 * 
		 * @return 返回文件流
		 */
		public InputStream getInputStream() {
			return inputStream;
		}

		/**
		 * 获取上传文件数据数组
		 * 
		 * @return 返回上传文件数据数组
		 */
		public byte[] getData() {
			return data;
		}

		/**
		 * 获取文件名称
		 * 
		 * @return 返回文件名称
		 */
		public String getFileName() {
			return fileName;
		}

		/**
		 * s设置文件名称
		 * 
		 * @param fileName
		 *            文件名称
		 */
		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		/**
		 * 获取服务端接收文件时文件对应的参数
		 * 
		 * @return 服务端接收文件时文件对应的参数
		 */
		public String getParamName() {
			return paramName;
		}

		/**
		 * 设置服务端接收文件时文件对应的参数
		 * 
		 * @param paramName
		 *            服务端接收文件时文件对应的参数
		 */
		public void setParamName(String paramName) {
			this.paramName = paramName;
		}

		/**
		 * 获取上传文件的内容类型
		 * 
		 * @return 上传文件的内容类型
		 */
		public String getContentType() {
			return contentType;
		}

		/**
		 * 设置上传文件的内容类型
		 * 
		 * @param contentType
		 *            上传文件的内容类型
		 */
		public void setContentType(String contentType) {
			this.contentType = contentType;
		}
	}

	public static class TrustAllManager implements X509TrustManager {
		@Override
		public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {

		}

		@Override
		public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {

		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	}
}
