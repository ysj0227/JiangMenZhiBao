package com.winsafe.jiangmenzhibao.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JSONHelper {
	public static JSONObject getJSONObjectInstance() {
		return new JSONObject();
	}

	public static void put(JSONObject objet, String key, Object value) {
		if (!objet.has(key))
			try {
				objet.put(key, value);
			} catch (JSONException e) {
				LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
						e.getMessage(), true);
			}
	}

	public static JSONArray getJSONArrayInstance() {
		return new JSONArray();
	}

	public static void put(JSONArray array, JSONObject object) {
		array.put(object);
	}

	/**
	 * 根据字符串获取JSON对象
	 * 
	 * @param json
	 * @return
	 */
	public static JSONObject getJSONObject(String json) {
		JSONObject object = null;

		try {
			object = new JSONObject(json);
		} catch (JSONException e) {
			LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
					e.getMessage(), true);
		}

		return object;
	}
	
	public static Object getObject(JSONObject jsonObj, String key) {
		if (jsonObj == null) return null;
		if (StringHelper.isNullOrEmpty(key)) return null;
		try {
			return jsonObj.get(key);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据JSON数组和数组索引获取JSON对象
	 * 
	 * @param array
	 * @param index
	 * @return
	 */
	public static JSONObject getJSONObject(JSONArray array, int index) {
		JSONObject object = null;

		try {
			if (array != null && array.length() > 0)
				object = array.getJSONObject(index);
		} catch (JSONException e) {
			LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
					e.getMessage(), true);
		}

		return object;
	}

	public static JSONArray getJSONArray(String jsonArray) {
		JSONArray array = null;

		try {
			if (jsonArray != null)
				array = new JSONArray(jsonArray);
		} catch (JSONException e) {
			LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
					e.getMessage(), true);
		}

		return array;
	}

	/**
	 * 根据关键字从JSON对象中获取数组
	 * 
	 * @param object
	 * @param key
	 * @return
	 */
	public static JSONArray getJSONArray(JSONObject object, String key) {
		if (object == null) return null;
		if (StringHelper.isNullOrEmpty(key)) return null;
		
		JSONArray array = null;

		try {
//			if (object != null && !TextUtils.isEmpty(getString(object, key)))
//				array = object.getJSONArray(key);
			Object obj = object.get(key);
			if (obj instanceof JSONArray) {
				array = (JSONArray) obj;
			}
		} catch (JSONException e) {
			LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
					e.getMessage(), true);
		}

		return array;
	}

	public static String getString(JSONObject object, String key) {
		String str = "";

		try {
			if (object == null) {
				return str;
			}
			if (object.has(key)) {
				Object a = object.get(key);
				if (a != null && a != JSONObject.NULL) {
					str = a.toString();
				}
//				str = object.getString(key);
			}
		} catch (JSONException e) {
			LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
					e.getMessage(), true);
		} catch (Exception e) {
			LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
					e.getMessage(), true);
		}

		return str;
	}
	
	public static Double getDouble(JSONObject jsonObj, String key) {
		Object obj = getObject(jsonObj, key);
		if (obj == null || obj == JSONObject.NULL) return null;
		return NumberHelper.toDouble(obj);
	}
	
	public static double getDouble(JSONObject jsonObj, String key, double defaultValue) {
		Object obj = getObject(jsonObj, key);
		if (obj == null || obj == JSONObject.NULL) return defaultValue;
		return NumberHelper.toDouble(obj, defaultValue);
	}

	public static int getInt(JSONObject object, String key) {
		int i = -1;

		try {
			if (object != null)
				i = object.getInt(key);
		} catch (JSONException e) {
			LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
					e.getMessage(), true);
		}

		return i;
	}

	/**
	 * 将lList<Map<String, Object>>数据转成json
	 * 
	 * @param mapList
	 * @return
	 */
	public static String transform(List<Map<String, Object>> mapList) {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if (mapList != null && mapList.size() > 0) {
			for (Map<String, Object> map : mapList) {
				json.append("{");
				Set<String> keySet = map.keySet();
				for (Object keyName : keySet) {
					json.append("\"" + keyName + "\"").append(":").append("\"" + map.get(keyName) + "\"").append(",");
				}
				json.setCharAt(json.length() - 1, '}');
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}
		return json.toString();
	}

	/**
	 * 将lList<Map<String, Object>>数据中含check：true的数据转成json 专门为获取checkBox选中的数据制定的
	 * 
	 * @param mapList
	 * @return
	 */
	public static String getCheckTrueData(List<Map<String, Object>> mapList) {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if (mapList != null && mapList.size() > 0) {
			for (Map<String, Object> map : mapList) {
				String isCheck = map.get("check") == null ? "" : map.get("check").toString();
				if (isCheck.equals("") || !isCheck.equals("true")) {
					continue;
				}
				json.append("{");
				Set<String> keySet = map.keySet();
				for (Object keyName : keySet) {
					json.append("\"" + keyName + "\"").append(":").append("\"" + map.get(keyName) + "\"").append(",");
				}
				json.setCharAt(json.length() - 1, '}');
				json.append(",");
			}
			if (json.charAt(json.length() - 1) != '[') {
				json.setCharAt(json.length() - 1, ']');
			} else {
				json.append("]");
			}
		} else {
			json.append("]");
		}
		return json.toString();
	}
	

	public static String getData(List<Map<String, Object>> mapList) {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if (mapList != null && mapList.size() > 0) {
			for (Map<String, Object> map : mapList) {
				json.append("{");
				Set<String> keySet = map.keySet();
				for (Object keyName : keySet) {
					json.append("\"" + keyName + "\"").append(":").append("\"" + map.get(keyName) + "\"").append(",");
				}
				json.setCharAt(json.length() - 1, '}');
				json.append(",");
			}
			if (json.charAt(json.length() - 1) != '[') {
				json.setCharAt(json.length() - 1, ']');
			} else {
				json.append("]");
			}
		} else {
			json.append("]");
		}
		return json.toString();
	}

	/**
	 * 将jsonObject数据装配到List<Map<String, Object>>()中
	 * 没考虑jsonObject中存在jsonObject或jsonArray的情况
	 * 
	 * @param mapList
	 * @param jsonObject
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<Map<String, Object>> assembleJsonObject(List<Map<String, Object>> mapList, JSONObject jsonObject) {

		try {
			// 遍历jsonObject
			Map map = new HashMap<String, Object>();
			for (Iterator iterator = jsonObject.keys(); iterator.hasNext();) {
				String key = (String) iterator.next();
				map.put(key, jsonObject.get(key));
			}
			mapList.add(map);
		} catch (JSONException e) {
			LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
					e.getMessage(), true);
		}
		return mapList;
	}

}
