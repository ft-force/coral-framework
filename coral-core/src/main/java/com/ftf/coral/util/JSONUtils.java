package com.ftf.coral.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class JSONUtils {

    private static final Logger logger = LoggerFactory.getLogger(JSONUtils.class);

    private static final ObjectMapper mapper = new ObjectMapper();
    static {
        // 空对象不要抛异常
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // 设置有属性不能映射成PO时不报错
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // mapper.registerModule(new ProtobufModule());
        // Include.NON_NULL 属性为NULL 不序列化
        mapper.setSerializationInclusion(Include.NON_NULL);
    }

    public static Object[] formJSONData(InputStream in, Type[] types) {

        if (in == null) {
            return null;
        }

        try {
            Object[] arrayList = new Object[types.length];

            JsonNode topNode = mapper.readTree(in);
            if (topNode == null) {
                return arrayList;
            }

            if (topNode instanceof ArrayNode) {
                if (topNode.size() == 0) {
                    return arrayList;
                }
                for (int i = 0; i < topNode.size() && i < types.length; i++) {
                    Type type = types[i];
                    JsonNode jsonNode = topNode.get(i);
                    arrayList[i] = readValue(type, jsonNode);
                }
            } else {
                Type type = types[0];
                arrayList[0] = readValue(type, topNode);
            }

            return arrayList;
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private static Object readValue(Type type, JsonNode jsonNode)
        throws JsonParseException, JsonMappingException, IOException {

        Object value = null;

        if (type instanceof ParameterizedType) {
            ParameterizedType t = (ParameterizedType)type;
            if (t.getRawType().getTypeName().equalsIgnoreCase(List.class.getName())) {
                value = mapper.readValue(jsonNode.traverse(), new TypeReference<List>() {
                    @Override
                    public Type getType() {
                        return type;
                    }
                });
            } else if (t.getRawType().getTypeName().equalsIgnoreCase(Map.class.getName())) {
                value = mapper.readValue(jsonNode.traverse(), new TypeReference<Map>() {
                    @Override
                    public Type getType() {
                        return type;
                    }
                });
            }
        } else {
            value = mapper.readValue(jsonNode.traverse(), new TypeReference<Object>() {
                @Override
                public Type getType() {
                    return type;
                }
            });
        }

        return value;
    }

    public static String toJSON(Object object) {

        if (object == null) {
            return null;
        }

        try {
            String json = mapper.writeValueAsString(object);
            return json;
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public static <T> ArrayList<T> formJSONArray(String json, Class<T> T) {

        if (json == null || json.length() == 0) {
            return null;
        }

        try {
            ArrayList<T> arrayList = new ArrayList<>();

            ArrayNode node = (ArrayNode)mapper.readTree(json);
            if (node == null || node.size() == 0) {
                return arrayList;
            } else {
                for (int i = 0; i < node.size(); i++) {
                    JsonNode aNode = node.get(i);
                    T info = mapper.convertValue(aNode, T);
                    arrayList.add(info);
                }
                return arrayList;
            }
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public static byte[] toJSONData(Object object) {

        if (object == null) {
            return null;
        }

        try {
            byte[] json = mapper.writeValueAsBytes(object);
            return json;
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public static <T> T formJSONData(InputStream in, Class<T> T) {

        if (in == null) {
            return null;
        }

        try {
            return mapper.readValue(in, T);
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public static <T> T formJSONData(byte[] json, int offset, int length, Class<T> T) {

        if (json == null || json.length == 0) {
            return null;
        }

        try {
            return mapper.readValue(json, offset, length, T);
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public static <T> T formJSON(String json, Class<T> T) {

        if (json == null || json.length() == 0) {
            return null;
        }

        try {
            return mapper.readValue(json, T);
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public static <T> T toBean(Object object, Class<T> beanClass) {

        return formJSON(toJSON(object), beanClass);
    }
}
