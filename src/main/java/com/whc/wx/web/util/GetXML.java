package com.whc.wx.web.util;

import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

public class GetXML {
	 /** 
     * xml转换json
     * @param xmlString 
     * @return 
     */  
    public static JSONObject getJSONFromXML(String xmlString){
        System.out.println("-----xmlString------："+xmlString);
    	XMLSerializer xmlSerializer = new XMLSerializer();  
    	JSONObject json = (JSONObject) xmlSerializer.read(xmlString);
        System.out.println("-----json------："+json);
        return json;
    }  
}
