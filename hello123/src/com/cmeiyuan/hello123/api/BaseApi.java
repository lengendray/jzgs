package com.cmeiyuan.hello123.api;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public abstract class BaseApi<T> extends AsyncHttpResponseHandler {

	protected static final String TAG = "BaseApi";

	private Type mType;
	private Gson mGson = new Gson();
	private AsyncHttpClient mAsyncHttpClient = new AsyncHttpClient();
	private AsyncCallBack<T> mAsyncCallBack;

	public BaseApi() {
		mAsyncHttpClient.setTimeout(30000);
		// mAsyncHttpClient.setConnectTimeout(30000);
		// mAsyncHttpClient.setResponseTimeout(30000);
		mAsyncHttpClient.setMaxRetriesAndTimeout(3, 30000);
        mAsyncHttpClient.addHeader("Cache-control", "private");
        mAsyncHttpClient.addHeader("Pragma", "private");
	}

	public AsyncCallBack<T> getAsyncCallBack() {
		return mAsyncCallBack;
	}

	public void setAsyncCallBack(AsyncCallBack<T> mAsyncCallBack) {
		this.mAsyncCallBack = mAsyncCallBack;
	}

    public void get() {
        get(null);
    }

	public void get(Map<String, String> param) {
		String url = getUrl();
		if (param != null && !param.isEmpty()) {
			if (!url.endsWith("?")) {
				url += "?";
			}
			Set<String> keySet = param.keySet();
			int i = 0;
			for (String key : keySet) {
				url += key;
				url += "=";
				url += param.get(key);
				if (i < param.size() - 1) {
					url += "&";
				}
				i++;
			}
		}
		mAsyncHttpClient.get(url, this);
	}

	protected abstract String getUrl();

	protected Type getType() {
		if (mType == null) {
			// 获取类型
			Class<?> cls = getClass();
			// 获取带泛型的父类
			Type type = cls.getGenericSuperclass();
			// 转换成参数化类型
			ParameterizedType p = (ParameterizedType) type;
			Type[] types = p.getActualTypeArguments();
			mType = types[0];
		}
		return mType;
	}

	@Override
	public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
		if (mAsyncCallBack != null) {
			Error error = null;
			T t = null;
			try {
				String json = new String(arg2);
				t = mGson.fromJson(json, getType());
			} catch (Exception e) {
				error = new Error();
				error.code = 1001;
				error.message = "解析数据出错";
				error.detail = e.toString();
			}

			if (error == null) {
				mAsyncCallBack.onSuccess(t);
			} else {
				mAsyncCallBack.onFailed(error);
			}
		}
	}

	@Override
	public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
		if (mAsyncCallBack != null) {
			try {
				String result = new String(arg2);
				Error error = new Error();
				error.code = arg0;
				error.message = result;
				error.detail = arg3.toString();
				mAsyncCallBack.onFailed(error);
			} catch (Exception e) {
				Error error = new Error();
				error.code = 1001;
				error.message = "解析数据出错";
				error.detail = e.toString();
				mAsyncCallBack.onFailed(error);
			}
		}
	}

	public interface AsyncCallBack<T> {
		public void onSuccess(T t);

		public void onFailed(Error error);
	}

	public static class Error {
		int code;
		String message;
		String detail;

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public String getDetail() {
			return detail;
		}

		public void setDetail(String detail) {
			this.detail = detail;
		}

	}
}
