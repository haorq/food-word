package com.meiyuan.catering.wx.utils;

import com.meiyuan.catering.core.util.ConvertUtils;
import com.meiyuan.catering.user.vo.user.UserPublicTextMessageVO;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;

/**
 * description：
 *
 * @author yy
 * @version 1.4.0
 * @date 2020/9/10 11:12
 */
@Slf4j
public class WxXmlUtil {

    private WxXmlUtil(){}

    private static final Logger logger = LoggerFactory.getLogger(WxXmlUtil.class);

    /**
     * xml文档解析为对象
     *
     * @param xml   xml文档
     * @param clazz 要转换的类
     * @return 对象
     */
    public static <T> T fromXml(String xml, Class<T> clazz, Class... childClazz) {
        try {
            XStream xmlStream = new XStream(new XppDriver(new XmlFriendlyNameCoder("_-", "_")));
            XStream.setupDefaultSecurity(xmlStream);
            xmlStream.processAnnotations(new Class[]{clazz});
            xmlStream.processAnnotations(childClazz);
            xmlStream.allowTypes(new Class[]{clazz});
            xmlStream.allowTypes(childClazz);
            xmlStream.ignoreUnknownElements();
            Object result = xmlStream.fromXML(xml);
            return ConvertUtils.sourceToTarget(result, clazz);
        }catch (Exception e){
            logger.error("fromXML error ", e);
            return null;
        }
    }


    /**
     * 对象组装成xml
     *
     * @param object 要转换的对象
     * @return xml文档
     */
    public static String toXml(Object object) {
        //转换成XML
        XStream xmlStream = new XStream(new XppDriver(new XmlFriendlyNameCoder("_-", "_")));
        XStream.setupDefaultSecurity(xmlStream);
        xmlStream.processAnnotations(new Class[]{object.getClass()});
        xmlStream.allowTypes(new Class[]{object.getClass()});
        return xmlStream.toXML(object);
    }


    /**
     * describe: 获取xml
     * @author: yy
     * @date: 2020/9/14 14:30
     * @param request
     * @return: {@link String}
     * @version 1.4.0
     **/
    public static String getXml(HttpServletRequest request) {
        SAXReader reader;
        try {
            reader = new SAXReader();
            InputStream ins = request.getInputStream();
            try {
                Document doc = reader.read(ins);
                return doc.asXML();
            }finally {
                ins.close();
            }
        }catch (Exception e){
            logger.error("getXml error ", e);
            return "";
        }
    }

    /**
     * describe: 文本消息对象转xml
     * @author: yy
     * @date: 2020/9/14 14:31
     * @param textMessage
     * @return: {@link String}
     * @version 1.4.0
     **/
    public static String textMsgToxml(UserPublicTextMessageVO textMessage){
        XStream xstream = new XStream();
        xstream.alias("xml", textMessage.getClass());
        return xstream.toXML(textMessage);
    }

}
