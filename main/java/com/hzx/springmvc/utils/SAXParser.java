package com.hzx.springmvc.utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * @author Jools He
 * @version 1.0
 * @date 2024/4/19 17:42
 * @description: TODO
 */
public class SAXParser {

    private SAXReader saxReader;

    public SAXParser() {
        this.saxReader = new SAXReader();
    }

    public String getScanPackages(String config) {
        if (config.isEmpty()) return "";
        Document document;
        Element rootElement;
        try {
            document = new SAXReader().read(
                    Thread.currentThread()
                            .getContextClassLoader()
                            .getResourceAsStream(config));
            rootElement = document.getRootElement();
            System.out.println("RootElement:" + rootElement.getName());

            Element element = rootElement.element("component-scan");
            String packages = element.attributeValue("base-package");
            System.out.println("待扫描的包信息为:" + packages);
            return packages;
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }
}
