package com.winsafe.jiangmenzhibao.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Set;


public class SharedManager {
	private SharedPreferences mSharedPreferences = null;
	
	/**
	 * 构造函数
	 * @param context 上下文
	 * @param sharedName 文件名字
	 */
	public SharedManager(Context context, String sharedName) {
		if(mSharedPreferences == null) {
			mSharedPreferences = context.getSharedPreferences(sharedName, Context.MODE_PRIVATE);
		}
	}
	
	/**
	 * 保存数据到SharedPreferences
	 * @param key 保存值对应的键值
	 * @param value 保存值
	 * @return 保存成功：true;保存失败：false
	 */
	public boolean saveValueByKey(String key, String value) {
		boolean result = false;
		
		try{
			Editor mEditor = mSharedPreferences.edit();
			mEditor.putString(key, value);
			mEditor.commit();
			
			result = true;
		}
		catch(Exception e) {
			LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()), "Exception:" + e.getMessage(), true);
		}
		
		return result;
	}
	
	@SuppressLint("NewApi")
	public boolean saveValuesByKey(String key, Set<String> values) {
		boolean result = false;
		
		try{
			Editor mEditor = mSharedPreferences.edit();
			mEditor.putStringSet(key, values);
			mEditor.commit();
			
			result = true;
		}
		catch(Exception e) {
			LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()), "Exception:" + e.getMessage(), true);
		}
		
		return result;
	}
	
	/**
	 * 根据键获取其所对应的值
	 * @param pKey 值所对应的的键
	 * @return 返回键所对应的的值
	 */
	public String getValueByKey(String pKey) {
		String result = mSharedPreferences.getString(pKey, "");
		return result;
	}
	
	@SuppressLint("NewApi")
	public Set<String> getValuesByKey(String pKey, Set<String> defaultSet) {
		return mSharedPreferences.getStringSet(pKey, defaultSet);
	}
	
	public boolean remove(String key) {
		boolean result = false;
		
		try{
			Editor mEditor = mSharedPreferences.edit();
			mEditor.remove(key);
			mEditor.commit();
			result = true;
		}
		catch(Exception e) {
			LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()), "Exception:" + e.getMessage(), true);
		}
		
		return result;
	}
}

