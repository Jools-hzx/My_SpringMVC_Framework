# My_SpringMVC_Framework
A simple framework realizes some parts of simplify SpringMVC and Spring features

# SpringMVC 模拟实现项目
## 简介
本项目旨在手动模拟实现 SpringMVC 框架的核心机制，通过一系列自定义的类和注解，复现 SpringMVC 的基本功能，包括请求分发、依赖注入、参数绑定、视图解析等。

## 功能概述
- **请求分发**：通过自定义的 `DispatcherServlet` 类实现请求的接收和分发。
- **依赖注入**：模拟 Spring 的依赖注入机制，通过自定义注解和容器类实现 Bean 的创建和注入。
- **参数绑定**：支持通过 `@RequestParam` 注解实现请求参数到方法参数的绑定。
- **视图解析**：模拟视图解析机制，支持转发和重定向，以及简单的 JSP 视图渲染。
- **JSON 数据交互**：支持通过 `@ResponseBody` 注解返回 JSON 格式的数据。

## 环境和依赖
- **Java**：1.8 或以上版本
- **Maven**：用于项目的依赖管理和打包

## 项目结构
```
my_springmvc
|-- annotation               # 存放自定义注解
|-- bean                      # 存放 JavaBean 对象
|-- controller                # 存放 Controller 组件类
|-- ioc                       # 存放自定义的容器类
|-- mapper                    # 存放映射类，如 HandlerMapper
|-- service                   # 存放 Service 组件类
|-- servlet                   # 存放原生 Servlet 类
|-- utils                     # 存放工具类，如 SAXParser
|-- resources                 # 存放各类资源配置
|-- webapp                    # 存放 JSP 页面
```

## 构建和运行
1. 使用 Maven 构建工具导入项目。
2. 执行 `mvn clean package` 命令进行编译打包。
3. 将生成的 WAR 文件部署到 Web 服务器中，如 Tomcat。

## 核心组件和注解
- **@Controller**：用于标识 Controller 类。
- **@RequestMapping**：用于映射 HTTP 请求到 Controller 的处理方法。
- **@Service**：用于标识 Service 类。
- **@AutoWired**：用于字段注入。
- **@RequestParam**：用于请求参数绑定。
- **@ResponseBody**：用于标识方法返回值直接作为 JSON 响应。

## 使用说明
1. 配置 `web.xml`，定义 `DispatcherServlet`。
2. 在 `config.xml` 中配置扫描的包和 Bean 定义。
3. 编写 Controller 和 Service 类，并使用自定义注解进行标注。
4. 使用 Postman 或浏览器发起请求，测试项目功能。

## 许可证
本项目遵循 [MIT License](https://opensource.org/licenses/MIT)。

---
