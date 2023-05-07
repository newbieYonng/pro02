package com.atguigu.service.impl;

import com.atguigu.fruit.dao.FruitDAO;
import com.atguigu.fruit.pojo.Fruit;
import com.atguigu.service.FruitService;
import com.atguigu.util.ConnUtil;

import java.util.List;

public class FruitServiceImpl implements FruitService {

    private FruitDAO fruitDAO = null;

    @Override
    public List<Fruit> getFruitList() {
        return fruitDAO.getFruitList();
    }

    @Override
    public List<Fruit> getFruitListWithPageNo(Integer pageNo) {
        return fruitDAO.getFruitListWithPageNo(pageNo);
    }

    @Override
    public List<Fruit> getFruitListWithRegx(String keyword, Integer pageNo) {
        return fruitDAO.getFruitListWithRegx(keyword, pageNo);
    }

    @Override
    public int getFruitPageCount() {
        return fruitDAO.getFruitPageCount();
    }

    @Override
    public int getFruitPageCountWithRegx(String keyword) {
        return fruitDAO.getFruitPageCountWithRegx(keyword);
    }

    @Override
    public void addFruit(Fruit fruit) {
        fruitDAO.addFruit(fruit);
        Fruit fruit1 = fruitDAO.getFruitByID(3);
        fruit1.setFcount(999);
        fruitDAO.updateFruit(fruit1);
    }

    @Override
    public boolean updateFruit(Fruit fruit) {
        return fruitDAO.updateFruit(fruit);
    }

    @Override
    public Fruit getFruitByFname(String fname) {
        return fruitDAO.getFruitByFname(fname);
    }

    @Override
    public boolean delFruit(String fname) {
        return fruitDAO.delFruit(fname);
    }

    @Override
    public Fruit getFruitByID(Integer fid) {
        return fruitDAO.getFruitByID(fid);
    }

    @Override
    public boolean delFruitByID(Integer fid) {
        return fruitDAO.delFruitByID(fid);
    }
}
