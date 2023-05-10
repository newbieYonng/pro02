package com.atguigu.controllers;

import com.atguigu.fruit.dao.impl.FruitDAOImpl;
import com.atguigu.fruit.pojo.Fruit;
import com.atguigu.servlets.ViewBaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*@WebServlet("/edit.do")*/
public class EtidServlet extends ViewBaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fidStr = request.getParameter("fid");
        FruitDAOImpl fruitDAO = new FruitDAOImpl();
        Fruit fruit = fruitDAO.getFruitByID(Integer.parseInt(fidStr));
        request.setAttribute("fruit", fruit);

        //response.sendRedirect("/edit.html");
        super.processTemplate("edit", request, response);
    }
}
