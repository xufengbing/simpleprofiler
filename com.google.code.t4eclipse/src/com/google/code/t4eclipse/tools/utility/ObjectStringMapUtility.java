package com.google.code.t4eclipse.tools.utility;
import java.lang.reflect.Array;
import java.util.*;
public class ObjectStringMapUtility {

	public static String getStringDescription(Object result, String info) {
		String start = "******" + info + "******";
		String end = getStrings(start.length(), "*");
		String center = getStrings(start.length(), "*");
		String resturnStr = start + "\n" + center + "\n"
				+ getObjectString(result) + "\n" + center + "\n" + end;
		return resturnStr;
	}

	public static String getObjectString(Object result) {
		if (result == null) {
			return "null";
		} else {
			String start = "Field Value Type:"
					+ result.getClass().getSimpleName() + "\n";

			if (result instanceof Number) {
				return start + ((Number) result).toString();
			}
			if (result instanceof String) {
				return start + result.toString();
			}
			if (result instanceof Character) {
				return start + result.toString();
			}

			// Array need to be solved Separatly
			if (result.getClass().isArray()) {

				if (result.getClass().getComponentType().isPrimitive()) {
					// primitive array
					int length=Array.getLength(result);
					start+="Array lengthe is:"+length;
					for(int i=0;i<length;i++){
						start+="["+i+"]:"+Array.get(result,i)+"\n";
					}
					return start;
				}

				else {
					// non-primitive array
					int length=Array.getLength(result);
					start+="Array lengthe is:"+length+"\n";
					for(int i=0;i<length;i++){
						start+="["+i+"]:"+Array.get(result,i)+"\n";
					}
					return start;
				}
			}
		
			// Collection need to be solved Separatly
			if(result instanceof Collection){
				Collection tmp=(Collection) result;
				start+="Collection Contents:\n";
				int index=0;
				for(Iterator it=tmp.iterator();it.hasNext();){
					Object element =it.next();
					start+="("+(++index)+"):"+element.toString()+"\n";
					
				}
				
			}
			return start + result.toString();
		}

	}

	private static String getStrings(int i, String str) {
		String a = "";
		if (i <= 0)
			return "";
		while ((--i) >= 0) {
			a += str;
		}
		return a;
	}
}
