package org.yujoo.commom.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;

public class CommonUtil {

	private final static Logger log = Logger.getLogger(CommonUtil.class);

	/**
	 * 解析http header 中的cookie
	 * 
	 * @param req
	 * @return
	 */
	public static Map<String, String> parseHeaderCookie(HttpServletRequest req) {
		String cookieString = req.getHeader("cookie");
		if (cookieString == null || cookieString.isEmpty()) {
			cookieString = req.getHeader("cookied");
		}
		log.debug("cookie names string:"
				+ JSON.toJSONString(req.getHeaderNames()));

		if (cookieString != null && !cookieString.isEmpty()) {
			String[] cookieArray = cookieString.split(";");
			Map<String, String> cookieRes = new HashMap<String, String>();
			for (int i = 0; i < cookieArray.length; i++) {
				String cookieItem = cookieArray[i].trim();
				String[] cookieItemKV = cookieItem.split("=");
				if (cookieItemKV.length == 2) {
					cookieRes.put(cookieItemKV[0].trim(),
							cookieItemKV[1].trim());
				}
			}
			return cookieRes;
		} else {
			return null;
		}

	}

	public static int getNowTimeStamp() {
		return (int) (System.currentTimeMillis() / 1000);
	}
	
	public static Object pickValue(Object... args){
		Object now = null;
		for(int i=0;i<args.length;i++){
			if(args[i]!=null){
				if(args[i] instanceof String){
					now = args[i];
					String nowString = (String)now;
					if(!nowString.isEmpty()){
						return nowString;
					}
				}else if(args[i] instanceof Integer){
					now = args[i];
					int nowInt =(Integer) now;
					if(nowInt!=0){
						return nowInt;
					}
				}
			}
		}
		return now;
	}
	
	public static int parseInt(Object param){
		if(param==null){
			return 0;
		}
		try {
			return Integer.parseInt(param.toString());	
		} catch (Exception e) {
			return 0;
		}
	}
}
