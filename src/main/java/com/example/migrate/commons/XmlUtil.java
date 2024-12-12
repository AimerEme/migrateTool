package com.example.migrate.commons;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Set;

public class XmlUtil {

	public static final String DOM_ENCODEING_GBK = "GBK";
	public static final String DOM_ENCODEING_UTF8 = "UTF-8";

	public static final XStream xstream_GBK = new XStream(new DomDriver(DOM_ENCODEING_GBK, new NoNameCoder()));
	public static final XStream xstream_UTF8 = new XStream(new DomDriver(DOM_ENCODEING_UTF8, new NoNameCoder()));

	public static final XStream xstream_GBK_PAY = new XStream(new DomDriver(DOM_ENCODEING_GBK, new NoNameCoder()));
	public static final XStream xstream_GBK_Refund = new XStream(new DomDriver(DOM_ENCODEING_GBK, new NoNameCoder()));

	public static final XStream xstream_GBK_PAY_NOTIFY = new XStream(new DomDriver(DOM_ENCODEING_GBK, new NoNameCoder()));
	public static final XStream xstream_UTF8_PAY_NOTIFY = new XStream(new DomDriver(DOM_ENCODEING_UTF8, new NoNameCoder()));

	public static final XStream xstream_GBK_PAY_QUERY = new XStream(new DomDriver(DOM_ENCODEING_GBK, new NoNameCoder()));
	public static final XStream xstream_UTF8_PAY_QUERY = new XStream(new DomDriver(DOM_ENCODEING_UTF8, new NoNameCoder()));

	public static final XStream xstream_GBK_PAY_QUERY_RESP = new XStream(new DomDriver(DOM_ENCODEING_GBK, new NoNameCoder()));
	public static final XStream xstream_UTF8_PAY_QUERY_RESP = new XStream(new DomDriver(DOM_ENCODEING_UTF8, new NoNameCoder()));

	public static final XStream xstream_GBK_PAYMENT_OBJECT = new XStream(new DomDriver(DOM_ENCODEING_GBK, new NoNameCoder()));
	public static final XStream xstream_UTF8_PAYMENT_OBJECT = new XStream(new DomDriver(DOM_ENCODEING_UTF8, new NoNameCoder()));

	public static final XStream xstream_GBK_PAYMENT_XML = new XStream(new DomDriver(DOM_ENCODEING_GBK, new NoNameCoder()));
	public static final XStream xstream_UTF8_PAYMENT_XML = new XStream(new DomDriver(DOM_ENCODEING_UTF8, new NoNameCoder()));

	public static final XStream xstream_GBK_PAYMENT_XML2 = new XStream(new DomDriver(DOM_ENCODEING_GBK, new NoNameCoder()));
	public static final XStream xstream_UTF8_PAYMENT_XML2 = new XStream(new DomDriver(DOM_ENCODEING_UTF8, new NoNameCoder()));

	public static final XStream xstream_GBK_PAY_NOTIFY_RESP = new XStream(new DomDriver(DOM_ENCODEING_GBK, new NoNameCoder()));
	public static final XStream xstream_UTF8_PAY_NOTIFY_RESP = new XStream(new DomDriver(DOM_ENCODEING_UTF8, new NoNameCoder()));

	public static final XStream xstream_GBK_REFUND = new XStream(new DomDriver(DOM_ENCODEING_GBK, new NoNameCoder()));
	public static final XStream xstream_UTF8_REFUND = new XStream(new DomDriver(DOM_ENCODEING_UTF8, new NoNameCoder()));

	public static final XStream xstream_GBK_REFUND_RESP = new XStream(new DomDriver(DOM_ENCODEING_GBK, new NoNameCoder()));
	public static final XStream xstream_UTF8_REFUND_RESP = new XStream(new DomDriver(DOM_ENCODEING_UTF8, new NoNameCoder()));

	public static final XStream xstream_GBK_ONLINE_BASE = new XStream(new DomDriver(DOM_ENCODEING_GBK, new NoNameCoder()));
	public static final XStream xstream_UTF8_ONLINE_BASE = new XStream(new DomDriver(DOM_ENCODEING_UTF8, new NoNameCoder()));

	public static final XStream xstream_GBK_REFUND_XML = new XStream(new DomDriver(DOM_ENCODEING_GBK, new NoNameCoder()));
	public static final XStream xstream_UTF8_REFUND_XML = new XStream(new DomDriver(DOM_ENCODEING_UTF8, new NoNameCoder()));

	public static final XStream xstream_GBK_REFUND_QUERY = new XStream(new DomDriver(DOM_ENCODEING_GBK, new NoNameCoder()));
	public static final XStream xstream_UTF8_REFUND_QUERY = new XStream(new DomDriver(DOM_ENCODEING_UTF8, new NoNameCoder()));

	public static final XStream xstream_GBK_REFUND_QUERY_RESULT = new XStream(new DomDriver(DOM_ENCODEING_GBK, new NoNameCoder()));
	public static final XStream xstream_UTF8_REFUND_QUERY_RESULT = new XStream(new DomDriver(DOM_ENCODEING_UTF8, new NoNameCoder()));

	public static <T> T xml2Object(String str, Class<T> clazz) {
		XStream xstream = new XStream(new DomDriver("UTF-8", new NoNameCoder()));
		xstream.autodetectAnnotations(true);
		xstream.processAnnotations(clazz);
		//缺元素是否报错
		xstream.ignoreUnknownElements();
		T obj = null;
		try {
			obj = clazz.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("无法实例化" + clazz.getName());
		}
		xstream.fromXML(str, obj);
		return obj;
	}

	public static String object2XML(Object obj) {
		XStream xstream = new XStream(new DomDriver("UTF-8", new NoNameCoder()));
		xstream.autodetectAnnotations(true);
		return xstream.toXML(obj);
	}

	@SuppressWarnings("rawtypes")
	public static String object2XML(Object obj, Class clazz) {
		XStream xstream = new XStream(new DomDriver("UTF-8", new NoNameCoder()));
		xstream.autodetectAnnotations(true);
		return xstream.toXML(obj);
	}
	/**
	 * 将对象转换为xml
	 * @param obj 将对象转换为xml
	 * @param encoding 编码
	 * @param classAlins 类的别名
	 * @return
	 */
	public static String object2XML(Object obj,String encoding, Map<String, Class> classAlins) {
		XStream xStream = null;
		if(encoding.equalsIgnoreCase(XmlUtil.DOM_ENCODEING_GBK)){
			xStream = XmlUtil.xstream_GBK;
		}else if (encoding.equalsIgnoreCase(XmlUtil.DOM_ENCODEING_UTF8)){
			xStream = XmlUtil.xstream_UTF8;
		}else{
			xStream = new XStream(new DomDriver(encoding, new NoNameCoder()));
		}
		xStream.autodetectAnnotations(true);
		Set<String> keys = classAlins.keySet();
		for (String key : keys) {
			xStream.alias("key", classAlins.get(key));
		}
		return xStream.toXML(obj);
	}
	public static String object2XML(Object obj,String encoding,String rootAlins) {
		XStream xStream = null;
		if(encoding.equalsIgnoreCase(XmlUtil.DOM_ENCODEING_GBK)){
			xStream = XmlUtil.xstream_GBK;
		}else if (encoding.equalsIgnoreCase(XmlUtil.DOM_ENCODEING_UTF8)){
			xStream = XmlUtil.xstream_UTF8;
		}else{
			xStream = new XStream(new DomDriver(encoding, new NoNameCoder()));
		}
		xStream.autodetectAnnotations(true);
		xStream.alias(rootAlins, obj.getClass());
		return xStream.toXML(obj);
	}

	public static String object2XML(Object obj,String encoding) {
		XStream xStream = null;
		if(encoding.equalsIgnoreCase(XmlUtil.DOM_ENCODEING_GBK)){
			xStream = XmlUtil.xstream_GBK;
		}else if (encoding.equalsIgnoreCase(XmlUtil.DOM_ENCODEING_UTF8)){
			xStream = XmlUtil.xstream_UTF8;
		}else{
			xStream = new XStream(new DomDriver(encoding, new NoNameCoder()));
		}
		xStream.autodetectAnnotations(true);
		return xStream.toXML(obj);
	}

	public static String replaceXmlElementValue(String xmlContent, Map<String, String> map){
		if(!StringUtils.hasText(xmlContent)){
			return xmlContent;
		}
		//定义${开头 ，}结尾的占位符
		PropertyPlaceholderHelper propertyPlaceholderHelper = new PropertyPlaceholderHelper("${", "}");
		//调用替换
		return propertyPlaceholderHelper.replacePlaceholders(xmlContent, map::get);
	}
}
