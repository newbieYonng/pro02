package com.atguigu.myspringmvc;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

@WebServlet("*.do")
public class DispatcherServlet extends ViewBaseServlet {

    private Map<String,Object> beanMap = new HashMap<>();

    public DispatcherServlet() {}

    public void init() throws ServletException {
        super.init();
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("applicationContext.xml");
            //1.创建DocumentBuilderFactory
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            //2.创建DocumentBuilder对象
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder() ;
            //3.创建Document对象
            Document document = documentBuilder.parse(inputStream);
            //获取所有的bean节点
            NodeList beanNodeList = document.getElementsByTagName("bean");
            for (int i = 0; i < beanNodeList.getLength(); i++) {
                Node beanNode = beanNodeList.item(i);
                if (beanNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element beanElement = (Element) beanNode;
                    String beanID = beanElement.getAttribute("id");
                    String className = beanElement.getAttribute("class");
                    Class controllerBeanClass = Class.forName(className);
                    Object beanobj = controllerBeanClass.newInstance();
                    beanMap.put(beanID, beanobj);
                }
            }
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        //获取@WebServlet("*.do")中*具体是哪个操作
        String servletPath = request.getServletPath();
        int index = servletPath.indexOf(".do");
        servletPath = servletPath.substring(1, index);

        Object controllerBeanObj = beanMap.get(servletPath);

        String operate = request.getParameter("operate");
        if (operate == null || "".equals(operate))
            operate = "index";

        Method[] methods = controllerBeanObj.getClass().getDeclaredMethods();
        for (Method method : methods) {
            String methodName = method.getName();
            if (operate.equals(methodName)) {
                //1.统一获取请求参数
                //1-1.获取当前方法的参数，返回参数数组
                Parameter[] parameters = method.getParameters();
                //1-2.parameterValues 用来承载参数的值
                Object[] parameterValues = new Object[parameters.length];
                for (int i = 0; i < parameters.length; i++) {
                    Parameter parameter = parameters[i];
                    String parameterName = parameter.getName();
                    if ("request".equals(parameterName)) {
                        parameterValues[i] = request;
                    } else if ("response".equals(parameterName)) {
                        parameterValues[i] = response;
                    } else if ("session".equals(parameterName)) {
                        parameterValues[i] = request.getSession();
                    } else {
                        //从请求中获取参数值
                        String requestParameter = request.getParameter(parameterName);
                        String typeName = parameter.getType().getName();

                        Object parameterObj = requestParameter;

                        if (parameterObj != null) {
                            if ("java.lang.Integer".equals(typeName)) {
                                parameterObj = Integer.parseInt(requestParameter);
                            }
                        }

                        parameterValues[i] = parameterObj;
                    }
                }

                //2.controller组件中的方法调用
                //获取私有方法
                method.setAccessible(true);
                Object returnObj = null;
                try {
                    returnObj = method.invoke(controllerBeanObj, parameterValues);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

                //3.视图处理
                String methodReturnStr = returnObj.toString();
                if (methodReturnStr.startsWith("redirect:")) {
                    String redirectStr = methodReturnStr.substring("redirect:".length());
                    response.sendRedirect(redirectStr);
                } else {
                    super.processTemplate(methodReturnStr, request, response);
                }
            }
        }
    }

}
