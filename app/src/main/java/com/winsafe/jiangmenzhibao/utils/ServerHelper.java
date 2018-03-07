package com.winsafe.jiangmenzhibao.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ServerHelper {
	protected ServerHelper() {}

	/**
	 * 读取输入的流数据
	 * @param inputStream 输入流
	 * @return 返回读取的数据数组
	 */
	protected static byte[] readStream(InputStream inputStream) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length = -1;

		try {
			while ((length = inputStream.read(buffer)) != -1) {
				byteArrayOutputStream.write(buffer, 0, length);
			}
			byteArrayOutputStream.close();
			inputStream.close();
		}
		catch (IOException e) {
			LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()), "Exception:" + e.getMessage(), true);
		}

		return byteArrayOutputStream.toByteArray();
	}
}
