package com.atguigu.servlets;

import com.atguigu.fruit.dao.FruitDAO;
import com.atguigu.fruit.dao.impl.FruitDAOImpl;
import com.atguigu.fruit.pojo.Fruit;
import com.atguigu.myspringmvc.ViewBaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

@WebServlet("/fruit.do")
public class FruitServlet extends ViewBaseServlet {

    private FruitDAOImpl fruitDAO = new FruitDAOImpl();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String operate = request.getParameter("operate");
        if (operate == null || "".equals(operate))
            operate = "index";

        Method[] methods = this.getClass().getDeclaredMethods();
        for (Method method : methods) {
            String methodName = method.getName();
            if (operate.equals(methodName)) {
                try {
                    method.invoke(this, request, response);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        /**
         * 用反射优化
         * switch (operate) {
            case "index":
                index(request, response);
                break;
            case "add":
                add(request, response);
                break;
            case "delete":
                delete(request, response);
                break;
            case "edit":
                edit(request, response);
                break;
            case "update":
                update(request, response);
                break;
            default:
                throw new RuntimeException("error operate!!!");
        }*/
    }

    private void index(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        int pageNo = 1;
        String keyword = null;

        //如果oper!=null 说明通过表单的查询按钮点击过来的
        //如果oper是空的，说明不是通过表单的查询按钮点击过来的
        String oper = request.getParameter("oper");

        if (oper != null && !"".equals(oper) && "search".equals(oper)) {
            pageNo = 1;
            keyword = request.getParameter("keyword");
            if (keyword == null || "".equals(keyword))
                keyword = "";
            session.setAttribute("keyword", keyword);
        } else {
            String pageNoStr = request.getParameter("pageNo");
            if (pageNoStr != null && !"".equals(pageNoStr)) {
                pageNo = Integer.parseInt(pageNoStr);
            }
            Object keywordObj = session.getAttribute("keyword");
            if (keywordObj != null)
                keyword = keywordObj.toString();
            else
                keyword = "";
        }

        session.setAttribute("pageNo", pageNo);

        List<Fruit> fruitList = fruitDAO.getFruitListWithRegx(keyword, pageNo);
        int fruitCount = fruitDAO.getFruitPageCountWithRegx(keyword);

        int pageCount = (fruitCount + 3 - 1) / 3;

        session.setAttribute("pageCount", pageCount);
        session.setAttribute("fruitList", fruitList);

        super.processTemplate("index", request, response);
    }

    private void add(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String fname = request.getParameter("fname");
        String priceStr = request.getParameter("price");
        Integer price = Integer.parseInt(priceStr);
        String fcountStr = request.getParameter("fcount");
        Integer fcount = Integer.parseInt(fcountStr);
        String remark = request.getParameter("remark");

        FruitDAO fruitDAO = new FruitDAOImpl();
        boolean flag = fruitDAO.addFruit(new Fruit(0 , fname , price , fcount , remark));

        response.sendRedirect("fruit.do");
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int fid = Integer.parseInt(request.getParameter("fid"));

        fruitDAO.delFruitByID(fid);

        response.sendRedirect("fruit.do");
    }

    private void edit(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String fidStr = request.getParameter("fid");
        FruitDAOImpl fruitDAO = new FruitDAOImpl();
        Fruit fruit = fruitDAO.getFruitByID(Integer.parseInt(fidStr));
        request.setAttribute("fruit", fruit);

        //response.sendRedirect("/edit.html");
        super.processTemplate("edit", request, response);
    }

    private void update(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int fid = Integer.parseInt(request.getParameter("fid"));
        String fname = request.getParameter("fname");
        int price = Integer.parseInt(request.getParameter("price"));
        int fcount = Integer.parseInt(request.getParameter("fcount"));
        String remark = request.getParameter("remark");

        Fruit fruit = new Fruit(fid, fname, price, fcount, remark);

        fruitDAO.updateFruit(fruit);

        response.sendRedirect("fruit.do");
    }

}
