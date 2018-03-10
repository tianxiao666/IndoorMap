package com.iscreate.mobile.service;

import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GsonService {
	/**
	 * convert a object to a JSON string
	 * 
	 * @param src
	 *            the Object which you want to convert to a JSON string
	 */
	public static String gsonObjectToJson(Object src) {
		Gson gson = new Gson();
		return (gson.toJson(src));
	}

	/**
	 * convert a JSON string to a List<String> object
	 * 
	 * @param jsonstr
	 *            a JSON string which you want to convert
	 */
	public static List<String> gsonGetListString(String jsonstr)
			throws Exception {
		try {
			Gson gson = new Gson();
			return (gson.fromJson(jsonstr, new TypeToken<List<String>>() {
			}.getType()));
		} catch (Exception e) {
			throw (new Exception("不是List<String>的Json字符串,Gson解析出错!"));
		}
	}

	/**
	 * convert a JSON string to a HashMap<String, String> object
	 * 
	 * @param jsonstr
	 *            a JSON string which you want to convert
	 */
	public static HashMap<String, String> gsonGetHashMap(String jsonstr)
			throws Exception {
		try {
			Gson gson = new Gson();
			return (gson.fromJson(jsonstr,
					new TypeToken<HashMap<String, String>>() {
					}.getType()));
		} catch (Exception e) {
			throw (new Exception("不是HashMap的Json字符串,Gson解析出错!"));
		}
	}

	/**
	 * convert a JSON string to a HashMap<String, HashMap<String, String>>
	 * object
	 * 
	 * @param jsonstr
	 *            a JSON string which you want to convert
	 */
	public static HashMap<String, HashMap<String, String>> gsonGetHashMapHashMap(
			String jsonstr) throws Exception {
		try {
			Gson gson = new Gson();
			return (gson.fromJson(jsonstr,
					new TypeToken<HashMap<String, HashMap<String, String>>>() {
					}.getType()));
		} catch (Exception e) {
			throw (new Exception("不是HashMapHashMap的Json字符串,Gson解析出错!"));
		}
	}

	/**
	 * convert a JSON string to a List<HashMap<String, String>> object
	 * 
	 * @param jsonstr
	 *            a JSON string which you want to convert
	 */
	public static List<HashMap<String, String>> gsonGetListHashMap(
			String jsonstr) throws Exception {
		try {
			Gson gson = new Gson();
			return (gson.fromJson(jsonstr,
					new TypeToken<List<HashMap<String, String>>>() {
					}.getType()));
		} catch (Exception e) {
			throw (new Exception("不是ListHashMap的Json字符串,Gson解析出错!"));
		}
	}

	/**
	 * convert a JSON string to a HashMap<String, List<HashMap<String, String>>>
	 * object
	 * 
	 * @param jsonstr
	 *            a JSON string which you want to convert
	 */
	public static HashMap<String, List<HashMap<String, String>>> gsonGetHashMapListHashMap(
			String jsonstr) throws Exception {
		try {
			Gson gson = new Gson();
			return (gson
					.fromJson(
							jsonstr,
							new TypeToken<HashMap<String, List<HashMap<String, String>>>>() {
							}.getType()));
		} catch (Exception e) {
			throw (new Exception("不是HashMapListHashMap的Json字符串,Gson解析出错!"));
		}
	}

	/**
	 * convert a JSON string to a List<HashMap<String, List<HashMap<String,
	 * String>>>> object
	 * 
	 * @param jsonstr
	 *            a JSON string which you want to convert
	 */
	public static List<HashMap<String, List<HashMap<String, String>>>> gsonGetListHashMapListHashMap(
			String jsonstr) throws Exception {
		try {
			Gson gson = new Gson();
			return (gson
					.fromJson(
							jsonstr,
							new TypeToken<List<HashMap<String, List<HashMap<String, String>>>>>() {
							}.getType()));
		} catch (Exception e) {
			throw (new Exception("不是HashMapListHashMap的Json字符串,Gson解析出错!"));
		}
	}
}