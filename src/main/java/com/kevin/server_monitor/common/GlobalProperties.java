package com.kevin.server_monitor.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class GlobalProperties {
	
    private static Logger logger = LoggerFactory.getLogger(GlobalProperties.class);
	public static Map<String, String> globalProperties = new HashMap<String, String>();	    
    private static GlobalProperties omniProp = new GlobalProperties();
    
    GlobalProperties() {
        if(omniProp == null) {
        	initProperties();
        }
    }
    
    public static GlobalProperties getInstance() {
    	if(omniProp == null) {
    		omniProp = new GlobalProperties();
    	}
        return omniProp;
    }
    
    public void initProperties() {
        final Map<String, String> props = getPropertyInformation(ServerString.GLOBAL_CONFIG_PROPERTIES);

        globalProperties.putAll(props);
    }
    
    public String getPropertyValue(String key) {
    	String value = globalProperties.get(key);
    	
    	if(value == null) return " ";
    	
    	return value;
    }
    public String findPropertyValue(String ip) {
        String findKey = "";

        // 모든 키를 순회합니다.
        for(String key : globalProperties.keySet()) {
            // 키와 매핑된 값이랑 equals() 메서드에 전달된 값이랑 일치하면 반복문을 종료합니다.
            if(globalProperties.get(key).equals(ip)) { // 키가 null이면 NullPointerException 예외 발생
                findKey = key;
                break;
            }
        }

        return findKey;
    }

   
    public Map<String, String> getPropertyInformation(final String path) {

        final FileIo propFile = new FileIo(path);
        final Map<String, String> props = new HashMap<String, String>();
        try {
            String strKey = "";
            String strValue = "";
            String cnt = null;
            while ((cnt = propFile.readOneLine()) != null) {
                if (cnt != null & cnt.length() > 0) {
                    if (cnt.trim().startsWith("#")) {
                        continue;
                    } else {
                        strKey = cnt.substring(0, cnt.indexOf("="));
                        strValue = cnt.substring(cnt.indexOf("=") + 1, cnt.length());

                       	props.put(strKey, strValue);
                    }
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        logger.info("property ="+props);

        return props;
    }
}
