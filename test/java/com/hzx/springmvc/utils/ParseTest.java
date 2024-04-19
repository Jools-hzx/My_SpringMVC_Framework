package com.hzx.springmvc.utils;

import org.junit.Test;

/**
 * @author Jools He
 * @version 1.0
 * @date 2024/4/19 17:48
 * @description: TODO
 */
public class ParseTest {

    @Test
    public void testScan() {
        SAXParser saxParser = new SAXParser();
        String scanPackages = saxParser.getScanPackages("config.xml");
        System.out.println(scanPackages);
    }
}
