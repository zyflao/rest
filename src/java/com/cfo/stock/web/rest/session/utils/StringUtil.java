
package com.cfo.stock.web.rest.session.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.codec.binary.Base64;


public class StringUtil {
	public static String appendString(String str1, String str2) {
		StringBuilder sb = new StringBuilder(200);
		sb.append(str1);
		sb.append("-");
		sb.append(str2);
		return sb.toString();
	}
	
	public static String composeString(String... strings){
		StringBuilder sb = new StringBuilder(200);
		for(String str:strings){
			sb.append(str);
		}
		return sb.toString();
	}

	public static String object2String(Object obj)throws Exception{
		return object2String(obj, "UTF-8");
	}
	public static String object2String(Object obj, String charsetName)
			throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(obj);
		oos.flush();
		oos.close();
		bos.close();
		return Base64.encodeBase64String(bos.toByteArray());
	}
	
	public static Object string2Object(String str)throws Exception{
		return string2Object(str, "UTF-8");
	}

	public static Object string2Object(String str, String charsetName)
			throws Exception {
		ByteArrayInputStream bis = new ByteArrayInputStream(Base64.decodeBase64(str));
		ObjectInputStream ois = new ObjectInputStream(bis);
		Object obj = ois.readObject();
		bis.close();
		ois.close(); 
		return obj;
	}
	
	public  static String toLength(String str, int length) {
  	    StringBuffer buff = new StringBuffer();
  	    int index = 0;
  	    char c;
  	    length -= 2;
  	    if(getLength(str) < length){
  	        return str ;
  	    }
  	    while (length > 0) {
  	        c = str.charAt(index);
  	        if (c < 128) {
  	            length--;
  	        } else {
  	            length--;
  	            length--;
  	        }
  	        //if(length >=0)   如果严格要求可以少于但是不能超出长度 请删除此注释
  	        buff.append(c);
  	        index++;
  	    }
  	    buff.append("...");
  	    return buff.toString();
  	}

	public static int getLength(String str) {
	    int count = 0;
	    for (int i = 0; i < str.length(); i++) {
	
	      //请勿去除 &&!((int)str.charAt(i)<=128) 这个代码 因为在某些时候没有这个代码会报异常
	
	        if (String.valueOf(str.charAt(i)).matches("[^x00-xff]*")&&!((int)str.charAt(i)<=128)) {  
	            count += 2;
	        } else {
	            count++;
	        }
	    }
	    return count;
	}
	
}
