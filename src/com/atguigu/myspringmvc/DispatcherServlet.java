package com.atguigu.myspringmvc;

import com.atguigu.fruit.io.BeanFactory;
import com.atguigu.fruit.io.impl.ClassPathXmlApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@WebServlet("*.do")
public class DispatcherServlet extends ViewBaseServlet {

    private BeanFactory beanFactory;

    public DispatcherServlet() {}

    public void init() throws ServletException {
        super.init();
        beanFactory = new ClassPathXmlApplicationContext();
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        //获取@WebServlet("*.do")中*具体是哪个操作
        String servletPath = request.getServletPath();
        int index = servletPath.indexOf(".do");
        servletPath = servletPath.substring(1, index);

        Object controllerBeanObj = beanFactory.getBean(servletPath);

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
