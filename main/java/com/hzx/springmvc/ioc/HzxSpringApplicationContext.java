package com.hzx.springmvc.ioc;

import com.hzx.springmvc.annotation.Controller;
import com.hzx.springmvc.utils.SAXParser;
import org.apache.commons.lang3.StringUtils;
import org.omg.CORBA.PRIVATE_MEMBER;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Jools He
 * @version 1.0
 * @date 2024/4/19 17:52
 * @description: TODO
 */
public class HzxSpringApplicationContext {

    private List<String> classFullNames = new ArrayList<>();
    private SAXParser saxParser;    //XML 文件解析工具

    private String configLocation;

    public static final String TARGET_PATH =
            "D://study//study_projects//spring_mvc_review//my_springmvc3//target//classes";

    private ConcurrentHashMap<String, Object> singletonObjects = new ConcurrentHashMap<>();

    public HzxSpringApplicationContext(String configLocation) {
        saxParser = new SAXParser();
        this.configLocation = configLocation;
    }

    public void init(String contextPath) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        String scanPackages = this.saxParser.getScanPackages(this.configLocation);
        //如果需要扫描的包有多个;按照 , 进行分割
        String[] packages = scanPackages.split(",");
        for (String aPackage : packages) {
            scanPackagesGetClassNames(TARGET_PATH, aPackage);
        }
        System.out.println("!!---- 获取全类名完成 ----!!");
        System.out.println(classFullNames);
        executeCreateBeanInstance();
        System.out.println("!!---- 实例化 Bean 对象完成 ----!!");
        System.out.println(singletonObjects);
    }

    //扫描所有全类名，使用反射判断是否含有注解;
    //使用反射实例化对象注入集合内
    private void executeCreateBeanInstance() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        if (this.classFullNames.isEmpty()) return;
        for (String classFullName : classFullNames) {
            Class<?> cls = Class.forName(classFullName);
            Object beanInstance;
            Controller annotation;
            String beanId = StringUtils.uncapitalize(cls.getSimpleName());
            if (cls.isAnnotationPresent(Controller.class)) {    //实例化注入 Controller 组件对象
                beanInstance = cls.newInstance();
                annotation = cls.getAnnotation(Controller.class);
                if (!"".equals(annotation.value())) {
                    beanId = annotation.value();
                }
                singletonObjects.put(beanId, beanInstance);
            }
        }
    }

    /**
     * 扫描目标目录下的所有 Class 类
     *
     * @param target    项目资源的存放目录
     * @param packageName   待扫描的包
     */
    private void scanPackagesGetClassNames(String target, String packageName) {
        System.out.println("扫描包:" + packageName);
        if (packageName.isEmpty()) return;
        //得到包的 URL-path
        StringBuilder sb = new StringBuilder();
        sb.append(target.replaceAll("\\.", "/"))
                .append("/")
                .append(packageName.replaceAll("\\.", "/"));
        String realPath = sb.toString();
        System.out.println("该包的资源路径:" + realPath);
        sb = new StringBuilder();   //刷新StringBuilder

        File file = new File(realPath);
        //遍历目录下的所有文件
        File[] files = file.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                //如何是一个目录，继续扫描子目录
                StringBuilder newPackage = sb.append(packageName).append(".").append(f.getName());
                sb = new StringBuilder();   //刷新 StringBuilder
                scanPackagesGetClassNames(target, newPackage.toString());
            } else if (f.getName().endsWith(".class")) {
                //全类名 = 包名 + 类名首字母小写
                sb.append(packageName).
                        append(".").
                        append(f.getName().replace(".class", ""));
                //仅处理 class 文件
                classFullNames.add(sb.toString());
                sb = new StringBuilder();   //刷新StringBuilder
            }
        }
    }

    public List<String> getClassFullNames() {
        return classFullNames;
    }

    public ConcurrentHashMap<String, Object> getSingletonObjects() {
        return singletonObjects;
    }
}
