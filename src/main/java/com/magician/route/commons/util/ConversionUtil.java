package com.magician.route.commons.util;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.magician.route.commons.constant.DataType;
import com.magician.route.commons.exception.VerificationException;
import io.magician.application.request.MagicianRequest;
import io.netty.handler.codec.http.multipart.MixedFileUpload;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Transformation class that extracts the parameters from the request and transforms them into an object of the specified type
 */
public class ConversionUtil {

    /**
     * Extracts parameters from the request and converts them to an object of the specified type.
     * @param request
     * @param cls
     * @return
     * @param <T>
     * @throws Exception
     */
    public static <T> T conversion(MagicianRequest request, Class<T> cls) throws Exception {
        return getObject(request, cls);
    }

    /**
     * Extracts the parameters from the request and converts them to an object of the specified type, and checks the parameters, throwing an exception if the check fails
     * @param request
     * @param cls
     * @return
     * @param <T>
     * @throws Exception
     */
    public static <T> T conversionAndVerification(MagicianRequest request, Class<T> cls) throws Exception {

        T obj = getObject(request, cls);

        String result = ParamsCheckUtil.checkParam(request.getUrl(), obj);
        if(StringUtil.isNull(result) == false){
            throw new VerificationException(result);
        }

        return obj;
    }

    /**
     * build parameter object
     * @param request
     * @param cls
     * @return
     * @throws Exception
     */
    private static <T> T getObject(MagicianRequest request, Class<T> cls) throws Exception {
        /* If it is a Json parameter, it is directly converted to a Java object and returned */
        if(ParamTypeUtil.isJSON(request.getContentType())){
            String paramJson = request.getJsonParam();
            if(paramJson == null){
                return null;
            }
            return JSONUtil.toJavaObject(paramJson, cls);
        }

        /* If it is not a Json parameter, then use reflection to handle it */
        Object obj = cls.getDeclaredConstructor().newInstance();
        Field[] fields = cls.getDeclaredFields();
        for(Field f : fields){
            boolean isFinal = Modifier.isFinal(f.getModifiers());
            if(isFinal){
                continue;
            }

            f.setAccessible(true);

            List<String> valList = request.getParams(f.getName());
            if(f.getType().equals(MixedFileUpload.class)){
                MixedFileUpload mixedFileUpload = request.getFile(f.getName());
                if (mixedFileUpload != null){
                    f.set(obj, mixedFileUpload);
                }
            } else if(f.getType().equals(MixedFileUpload[].class)){
                putMarsFileUploads(f,obj, request.getFiles(f.getName()));
            } else if(valList != null && valList.size() > 0){
                putAttr(f,obj,valList);
            }
        }
        return (T)obj;
    }

    /**
     * assign a value to a parameter
     * @param field
     * @param obj
     * @throws Exception
     */
    private static void putMarsFileUploads(Field field, Object obj,List<MixedFileUpload> marsFileUpLoadList) throws Exception{
        if (marsFileUpLoadList == null) {
            return;
        }
        MixedFileUpload[] marsFileUpLoads = new MixedFileUpload[marsFileUpLoadList.size()];

        for(int index = 0; index < marsFileUpLoadList.size(); index++){
            marsFileUpLoads[index] = marsFileUpLoadList.get(0);
        }

        field.set(obj, marsFileUpLoads);
    }

    /**
     * assign a value to a parameter
     * @param field
     * @param obj
     * @param valList
     * @throws Exception
     */
    private static void putAttr(Field field, Object obj, List<String> valList) throws Exception{
        String fieldTypeName = field.getType().getSimpleName().toUpperCase();
        String valStr = valList.get(0);
        if(valStr == null || valStr.equals("")){
            return;
        }
        switch (fieldTypeName){
            case DataType.INT:
            case DataType.INTEGER:
                field.set(obj,Integer.parseInt(valStr));
                break;
            case DataType.BYTE:
                field.set(obj,Byte.parseByte(valStr));
                break;
            case DataType.STRING:
                field.set(obj,valStr);
                break;
            case DataType.CHAR:
            case DataType.CHARACTER:
                field.set(obj,valStr.charAt(0));
                break;
            case DataType.DOUBLE:
                field.set(obj,Double.parseDouble(valStr));
                break;
            case DataType.FLOAT:
                field.set(obj,Float.parseFloat(valStr));
                break;
            case DataType.LONG:
                field.set(obj,Long.parseLong(valStr));
                break;
            case DataType.SHORT:
                field.set(obj,Short.valueOf(valStr));
                break;
            case DataType.BOOLEAN:
                field.set(obj,Boolean.parseBoolean(valStr));
                break;
            case DataType.BIGDECIMAL:
                field.set(obj,new BigDecimal(valStr));
                break;
            case DataType.DATE:
                String fmt = "yyyy-MM-dd HH:mm:ss";
                JsonFormat jsonFormat = field.getAnnotation(JsonFormat.class);
                if(jsonFormat != null && jsonFormat.pattern() != null && !jsonFormat.pattern().equals("")){
                    fmt = jsonFormat.pattern();
                }
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(fmt);
                field.set(obj,simpleDateFormat.parse(valStr));
                break;
            default:
                String[] paramArray = new String[valList.size()];
                paramArray = valList.toArray(paramArray);
                if (field.getType().equals(String[].class)){
                    field.set(obj,paramArray);
                }
                break;
        }
    }
}
