package com.example.migrate.commons;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Javax Xml Bind Parser
 *
 * @author yinghuihong
 * @date 2019/4/19
 */
public class XmlBindParser {

    private static final Logger logger = LoggerFactory.getLogger(XmlBindParser.class);

    private static Map<List<Class<?>>, JAXBContext> jaxbContextMap = new ConcurrentHashMap<>();

    private static JAXBContext getJAXBContext(Class<?>... classes) throws JAXBException {
        List<Class<?>> classList = new ArrayList<>(Arrays.asList(classes));
        if (!jaxbContextMap.containsKey(classList)) {
            logger.info("XmlBindParser getJAXBContext()，没有命中缓存，新建缓存对象:{}", classes[0].getName());
            JAXBContext jaxbContext = JAXBContext.newInstance(classes);
            jaxbContextMap.put(classList, jaxbContext);
            return jaxbContext;
        }
        return jaxbContextMap.get(classList);
    }

    public static <T> T toObj(Class<T> clazz, String xml) {
        long startTimeForMeasure = System.currentTimeMillis();
        try {
            JAXBContext context = getJAXBContext(clazz);
            InputStream buf = new ByteArrayInputStream(xml.getBytes("UTF-8"));
            T t = (T) context.createUnmarshaller().unmarshal(buf);
            logger.info("XmlBindParser toObj()，处理时长:{}", System.currentTimeMillis() - startTimeForMeasure);
            return t;
        } catch (Exception e) {
            logger.error("XmlBindParser toObj()异常，",e);
        }
        return null;
    }

    public static <T> String toXml(T obj) {
        long startTimeForMeasure = System.currentTimeMillis();
        try {
            StringWriter write = new StringWriter();
            JAXBContext context = getJAXBContext(obj.getClass());
            context.createMarshaller().marshal(obj, write);
            String xml = write.getBuffer().toString();
            logger.info("XmlBindParser toXml()，处理时长:{}", System.currentTimeMillis() - startTimeForMeasure);
            return xml;
        } catch (JAXBException e) {
            logger.error("XmlBindParser toXml()异常，",e);
        }
        return "";
    }
}





