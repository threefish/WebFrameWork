package com.sgaop.web.frame.server.util;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

public class ParameterConverter {
    private static final Logger log = Logger.getRootLogger();

    public static <T> T bulid(Class<T> cls, String prefix, Map<String, ?> requestParameterMap) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException, ParseException {
        Object obj = cls.newInstance();
        Field[] fieldArray = cls.getDeclaredFields();
        for (int i = 0; i < fieldArray.length; i++) {
            Field field = fieldArray[i];
            Class fieldType = field.getType();
            String fieldName = field.getName();
            String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            Object paramObj = requestParameterMap.get(prefix + "." + fieldName);
            if (paramObj == null) {
                continue;
            }
            Method method = null;
            Class classDef = obj.getClass();
            if (fieldType.equals(String.class)) {
                method = classDef.getMethod(methodName, String.class);
            } else if (fieldType.equals(String[].class)) {
                method = classDef.getMethod(methodName, String[].class);
            } else if (fieldType.equals(int[].class)) {
                method = classDef.getMethod(methodName, int[].class);
            } else if (fieldType.equals(int.class)) {
                method = classDef.getMethod(methodName, int.class);
            } else if (fieldType.equals(double.class)) {
                method = classDef.getMethod(methodName, double.class);
            } else if (fieldType.equals(long.class)) {
                method = classDef.getMethod(methodName, long.class);
            } else if (fieldType.equals(float.class)) {
                method = classDef.getMethod(methodName, float.class);
            } else if (fieldType.equals(boolean.class)) {
                method = classDef.getMethod(methodName, boolean.class);
            } else if (fieldType.equals(Date.class)) {
                method = classDef.getMethod(methodName, Date.class);
            } else if (fieldType.equals(java.sql.Date.class)) {
                method = classDef.getMethod(methodName, java.sql.Date.class);
            } else if (fieldType.equals(Timestamp.class)) {
                method = classDef.getMethod(methodName, Timestamp.class);
            }
            Object objevalue = ClassTool.ParamCast(fieldType, paramObj);
            if (objevalue != null) {
                method.invoke(obj, new Object[]{objevalue});
            }
        }
        return (T) obj;
    }

    /**
     * 将url get参数转MAP
     *
     * @param url
     * @return
     */
    public static Map<String, String> urlToMap(String url) {
        url = url.substring(url.indexOf("?") + 1);
        String[] temp = url.split("&");
        Map<String, String> map = new HashMap<String, String>();
        for (String s : temp) {
            String[] stemp = s.split("=");
            if (stemp.length == 2) {
                map.put("" + stemp[0], stemp[1]);
            } else {
                map.put("" + stemp[0], "");
            }
        }
        return map;
    }


    /**
     * 转成正常map
     *
     * @param request
     * @return
     */
    public static Map<String, Object[]> bulidMultipartMap(HttpServletRequest request) throws Exception {
        Map<String, Object[]> req = new HashMap<String, Object[]>();
        if (ServletFileUpload.isMultipartContent(request)) {
            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            List items = upload.parseRequest(request);
            for (Iterator i = items.iterator(); i.hasNext(); ) {
                FileItem fileItem = (FileItem) i.next();
                // 如果该FileItem不是表单域
                if (!fileItem.isFormField()) {
                    String name = fileItem.getFieldName();
                    req.put(name,new Object[]{IoTool.writeFile(fileItem.getInputStream(),fileItem.getName())} );
                } else {
                    String name = fileItem.getFieldName();
                    String value = IoTool.InputStreamTOString(fileItem.getInputStream());
                    req.put(name, new Object[]{value});
                }
            }
        }
        return req;
    }


    /**
     * 转成正常map
     *
     * @param request
     * @return
     */
    public static Map<String, String> bulid(HttpServletRequest request) {
        Map<String, String[]> req = request.getParameterMap();
        Map<String, String> map = new HashMap<String, String>();
        for (Iterator iter = req.entrySet().iterator(); iter.hasNext(); ) {
            Map.Entry element = (Map.Entry) iter.next();
            String key = (String) element.getKey();
            Object value = element.getValue();
            if (value instanceof String[]) {
                String[] values = (String[]) value;
                if (values.length == 1) {
                    map.put(key, values[0]);
                } else {
                    String temp = "";
                    for (String str : values) {
                        if (temp.equals(""))
                            temp = str;
                        temp += "," + str;
                    }
                }
            } else {
                map.put(key, value.toString());
            }
        }
        return map;
    }

}
