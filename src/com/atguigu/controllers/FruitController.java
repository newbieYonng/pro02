package com.atguigu.controllers;

import com.atguigu.fruit.pojo.Fruit;
import com.atguigu.service.FruitService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class FruitController {

    //private fruitServiceImpl fruitDAO = new fruitServiceImpl();
    private FruitService fruitService = null;

    /*@Override
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
        */

    /**
     * 用反射优化
     * switch (operate) {
     * case "index":
     * index(request, response);
     * break;
     * case "add":
     * add(request, response);
     * break;
     * case "delete":
     * delete(request, response);
     * break;
     * case "edit":
     * edit(request, response);
     * break;
     * case "update":
     * update(request, response);
     * break;
     * default:
     * throw new RuntimeException("error operate!!!");
     * }
     *//*
    }*/
    private String index(HttpServletRequest request, String oper, String keyword, Integer pageNo) throws IOException {
        HttpSession session = request.getSession();

        if (pageNo == null)
            pageNo = 1;

        if (oper != null && !"".equals(oper) && "search".equals(oper)) {
            pageNo = 1;
            if (keyword == null || "".equals(keyword))
                keyword = "";
            session.setAttribute("keyword", keyword);
        } else {
            Object keywordObj = session.getAttribute("keyword");
            if (keywordObj != null)
                keyword = keywordObj.toString();
            else
                keyword = "";
        }

        session.setAttribute("pageNo", pageNo);

        List<Fruit> fruitList = fruitService.getFruitListWithRegx(keyword, pageNo);
        int fruitCount = fruitService.getFruitPageCountWithRegx(keyword);

        int pageCount = (fruitCount + 3 - 1) / 3;

        session.setAttribute("pageCount", pageCount);
        session.setAttribute("fruitList", fruitList);

        return "index";
    }

    private String add(String fname, Integer price, Integer fcount, String remark) throws IOException {
        fruitService.addFruit(new Fruit(0, fname, price, fcount, remark));

        return "redirect:fruit.do";
    }

    private String delete(Integer fid) throws IOException {
        if (fid != null) {
            fruitService.delFruitByID(fid);
            return "redirect:fruit.do";
        }
        return "error";
    }

    private String edit(HttpServletRequest request, Integer fid) throws IOException {
        if (fid != null) {
            Fruit fruit = fruitService.getFruitByID(fid);
            request.setAttribute("fruit", fruit);

            return "edit";
        }
        return "error";
    }

    private String update(Integer fid, String fname, Integer price, Integer fcount, String remark) throws IOException {
        Fruit fruit = new Fruit(fid, fname, price, fcount, remark);
        fruitService.updateFruit(fruit);

        return "redirect:fruit.do";
    }

}
