package com.atguigu.servlets;

import com.atguigu.fruit.dao.impl.FruitDAOImpl;
import com.atguigu.fruit.pojo.Fruit;
import com.atguigu.myspringmvc.ViewBaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/index")
public class IndexServlet extends ViewBaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int pageNo = 1;
        String pageNoStr = request.getParameter("pageNo");
        if (pageNoStr != null && !"".equals(pageNoStr)) {
            pageNo = Integer.parseInt(pageNoStr);
        }

        FruitDAOImpl fruitDAO = new FruitDAOImpl();
        List<Fruit> fruitList = fruitDAO.getFruitListWithPageNo(pageNo);
        int fruitCount = fruitDAO.getFruitPageCount();

        int pageCount = (fruitCount + 3 - 1) / 3;

        HttpSession session = request.getSession();
        session.setAttribute("pageNo", pageNo);
        session.setAttribute("pageCount", pageCount);
        session.setAttribute("fruitList", fruitList);

        super.processTemplate("index", request, response);
    }
}
