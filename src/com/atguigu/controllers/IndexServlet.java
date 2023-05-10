package com.atguigu.controllers;

import com.atguigu.fruit.dao.impl.FruitDAOImpl;
import com.atguigu.fruit.pojo.Fruit;
import com.atguigu.servlets.ViewBaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/*@WebServlet("/index")*/
public class IndexServlet extends ViewBaseServlet {

    private FruitDAOImpl fruitDAO = new FruitDAOImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);

        /*request.setCharacterEncoding("UTF-8");
        String keyword = request.getParameter("keyword");

        int pageNo = 1;
        String pageNoStr = request.getParameter("pageNo");
        if (pageNoStr != null && !"".equals(pageNoStr)) {
            pageNo = Integer.parseInt(pageNoStr);
        }

        List<Fruit> fruitList = fruitDAO.getFruitListWithRegx(keyword, pageNo);
        int fruitCount = fruitDAO.getFruitPageCountWithRegx(keyword);

        int pageCount = (fruitCount + 3 - 1) / 3;

        HttpSession session = request.getSession();
        session.setAttribute("pageNo", pageNo);
        session.setAttribute("pageCount", pageCount);
        session.setAttribute("fruitList", fruitList);
        session.setAttribute("keyword", keyword);

        super.processTemplate("index", request, response);*/
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
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




        /*HttpSession session = request.getSession();

        int pageNo = 1;
        String pageNoStr = request.getParameter("pageNo");
        if (pageNoStr != null && !"".equals(pageNoStr)) {
            pageNo = Integer.parseInt(pageNoStr);
        }

        String keyword = null ;
        Object keywordObj = session.getAttribute("keyword");
        if(keywordObj!=null){
            keyword = keywordObj.toString() ;
        }else{
            keyword = "" ;
        }

        *//*List<Fruit> fruitList = fruitDAO.getFruitListWithPageNo(pageNo);
        int fruitCount = fruitDAO.getFruitPageCount();*//*
        List<Fruit> fruitList = fruitDAO.getFruitListWithRegx(keyword, pageNo);
        int fruitCount = fruitDAO.getFruitPageCountWithRegx(keyword);

        int pageCount = (fruitCount + 3 - 1) / 3;

        session.setAttribute("pageNo", pageNo);
        session.setAttribute("pageCount", pageCount);
        session.setAttribute("fruitList", fruitList);

        super.processTemplate("index", request, response);*/
    }
}
