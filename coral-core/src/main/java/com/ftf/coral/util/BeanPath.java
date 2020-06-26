package com.ftf.coral.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// $----根节点
// *----所有节点
// []---迭代器标识
// demo: $
// demo: $[*]
// demo: $[0].store
// demo: $.store[*]
// demo: $[0].store\.a
/**
 * @author zhang.baohao
 */
public class BeanPath {

    private static final Logger logger = LoggerFactory.getLogger(BeanPath.class);

    public static final char PERIOD = '.';
    public static final char SINGLE_QUOTE = '\'';

    private static final char OPEN_SQUARE_BRACKET = '[';
    private static final char CLOSE_SQUARE_BRACKET = ']';
    private static final char[] INDEX_CHARS = new char[] { OPEN_SQUARE_BRACKET, PERIOD };

    private final String name;
    private String index;
    private BeanPath childPath;

    private BeanPath(String path, boolean rootPath) {

        path = path.trim();

        int pathLen = path.length();

        int ndx = 0;

        if (StringUtils.startsWithChar(path, SINGLE_QUOTE)) {
            int tempNdx = StringUtils.indexOf(path, SINGLE_QUOTE, 1, pathLen);
            this.name = path.substring(1, tempNdx);

            if (tempNdx + 1 == pathLen) {
                return;
            } else {
                ndx = tempNdx + 1;
            }
        } else {
            int tempNdx = StringUtils.indexOfChars(path, INDEX_CHARS);

            if (tempNdx == -1) {
                this.name = path;
                return;
            } else {
                this.name = path.substring(0, tempNdx);
                ndx = tempNdx;
            }
        }

        if (path.charAt(ndx) == PERIOD) {
            this.childPath = new BeanPath(path.substring(ndx + 1, pathLen), false);
        } else if (path.charAt(ndx) == OPEN_SQUARE_BRACKET) {
            int rndx = StringUtils.indexOf(path, CLOSE_SQUARE_BRACKET);
            if (rndx - ndx > 1) {
                // 设置index
                this.index = path.substring(ndx + 1, rndx);
            }
            if (pathLen - rndx > 2) {
                this.childPath = new BeanPath(path.substring(rndx + 2, pathLen), false);
            }
        }
    }

    public String getFullName() {
        if (index == null) {
            return name;
        } else {
            return name + OPEN_SQUARE_BRACKET + index + CLOSE_SQUARE_BRACKET;
        }
    }

    public BeanPath getChildPath() {
        return this.childPath;
    }

    public static BeanPath compile(String beanPath) {
        return new BeanPath(beanPath, true);
    }

    public List<Object> readTopProperty(Object bean) {

        if (bean == null) {
            return null;
        }

        List<Object> resultList = new ArrayList<>();

        Set<String> propertySet = this.getPropertySet(bean);
        for (String property : propertySet) {

            Object subBean = this.getBeanProperty(bean, property);

            if (index != null) {
                resultList.addAll(this.getArrayProperty(subBean));
            } else {
                resultList.add(subBean);
            }
        }

        return resultList;
    }

    public List<Object> read(Object bean) {

        if (bean == null) {
            return null;
        }

        List<Object> tempList = new ArrayList<>();

        Set<String> propertySet = this.getPropertySet(bean);
        for (String property : propertySet) {
            Object subBean = this.getBeanProperty(bean, property);

            if (subBean == null) {
                continue;
            }

            if (index != null) {
                tempList.addAll(this.getArrayProperty(subBean));
            } else {
                tempList.add(subBean);
            }
        }

        if (childPath == null) {
            return tempList;
        } else {
            List<Object> resultList = new ArrayList<>();
            for (Object tempBean : tempList) {
                List<Object> childList = childPath.read(tempBean);

                if (childList != null && childList.size() > 0) {
                    resultList.addAll(childList);
                }
            }
            return resultList;
        }
    }

    @SuppressWarnings("unchecked")
    private List<Object> getArrayProperty(Object bean) {
        List<Object> subList = new ArrayList<>();

        if (bean.getClass().isArray()) {
            if ("*".equals(index)) {
                int len = Array.getLength(bean);
                for (int i = 0; i < len; i++) {
                    Object obj = Array.get(bean, i);
                    if (obj != null) {
                        subList.add(obj);
                    }
                }
            } else {
                Object obj = Array.get(bean, Integer.valueOf(index));
                if (obj != null) {
                    subList.add(obj);
                }
            }

        } else if (bean instanceof List) {
            List<Object> list = (List<Object>) bean;
            if ("*".equals(index)) {
                subList.addAll(list);
            } else {
                subList.add(list.get(Integer.valueOf(index)));
            }
        }

        return subList;
    }

    @SuppressWarnings("unchecked")
    private Object setArrayProperty(Object bean, int ndx, Object value) {

        if (bean.getClass().isArray()) {

            Array.set(bean, ndx, value);

        } else if (bean instanceof List) {

            List<Object> list = (List<Object>) bean;
            list.set(ndx, value);
        }

        return bean;
    }

    @SuppressWarnings("unchecked")
    private Object getBeanProperty(Object bean, String property) {
        if (property.equals("@") || property.equals("$")) {
            return bean;
        }
        if (bean instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) bean;
            return map.get(property);
        } else {
            try {
                Method getMethod = bean.getClass()
                                .getMethod("get" + property.substring(0, 1).toUpperCase() + property.substring(1));
                return getMethod.invoke(bean);
            } catch (Exception e) {
                logger.debug("未找到对应getter方法 class={}, property={}", bean.getClass(), property);
            }
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private Object setBeanProperty(Object bean, String property, Object value) {

        if (property.equals("@") || property.equals("$")) {
            return value;
        }

        if (bean instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) bean;
            map.put(property, value);
        } else {
            try {

                ReflectHelper.setValueByFieldName(bean, property, value);

            } catch (Exception e) {
                logger.error("未找到对应setter方法 class={}, property={}", bean.getClass(), property, e);
            }
        }

        return bean;
    }

    public Object process(Object bean, Function<Object, Object> processFunction) {

        if (bean == null) {
            return null;
        }

        Set<String> propertySet = this.getPropertySet(bean);
        for (String property : propertySet) {

            Object subBean = this.getBeanProperty(bean, property);

            if (subBean == null) {
                continue;
            }

            if (childPath == null) {

                if (index != null) {
                    // 加工数组数据
                    subBean = this.processArrayProperty(subBean, processFunction);
                } else {
                    subBean = processFunction.apply(subBean);
                }
            } else {

                if (index != null) {
                    if ("*".equals(index)) {

                        List<Object> valueLst = this.getArrayProperty(subBean);
                        for (int i = 0; i < valueLst.size(); i++) {
                            Object value = this.childPath.process(valueLst.get(i), processFunction);
                            this.setArrayProperty(subBean, i, value);
                        }

                    } else {

                        int ndx = Integer.parseInt(index);

                        List<Object> valueLst = this.getArrayProperty(subBean);

                        Object value = this.childPath.process(valueLst.get(0), processFunction);
                        this.setArrayProperty(subBean, ndx, value);
                    }
                } else {

                    subBean = this.childPath.process(subBean, processFunction);
                }
            }

            bean = this.setBeanProperty(bean, property, subBean);
        }

        return bean;
    }

    @SuppressWarnings("unchecked")
    private Object processArrayProperty(Object bean, Function<Object, Object> processFunction) {
        Object result = bean;

        if (bean.getClass().isArray()) {
            if ("*".equals(index)) {
                int len = Array.getLength(bean);
                for (int i = 0; i < len; i++) {
                    Object obj = Array.get(bean, i);
                    Array.set(bean, i, processFunction.apply(obj));
                }
            } else {
                int i = Integer.valueOf(index);
                Object obj = Array.get(bean, i);
                Array.set(bean, i, processFunction.apply(obj));
            }

        } else if (bean instanceof List) {

            List<Object> list = (List<Object>) bean;
            if ("*".equals(index)) {
                result = list.stream().map(processFunction::apply).collect(Collectors.toList());
            } else {
                int i = Integer.valueOf(index);
                Object obj = list.get(i);
                list.set(i, processFunction.apply(obj));
            }
        }

        return result;
    }

    private Set<String> getPropertySet(Object bean) {

        Set<String> keySet = new HashSet<>();

        switch (this.name) {
            case "*":
                if (bean instanceof Map) {
                    Map<String, Object> map = (Map<String, Object>) bean;
                    keySet = map.keySet();
                } else {
                    // java bean对象，获取所有属性
                    Field[] fields = bean.getClass().getDeclaredFields();
                    for (Field f : fields) {
                        keySet.add(f.getName());
                    }
                }

                break;

            default:
                keySet.add(this.name);
                break;
        }

        return keySet;
    }
}