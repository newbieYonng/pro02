package com.atguigu.fruit.dao.impl;

import com.atguigu.fruit.dao.FruitDAO;
import com.atguigu.fruit.dao.base.BaseDAO;
import com.atguigu.fruit.pojo.Fruit;

import java.util.List;

public class FruitDAOImpl extends BaseDAO<Fruit> implements FruitDAO {
    @Override
    public List<Fruit> getFruitList() {
        return super.executeQuery("select * from t_fruit");
    }

    @Override
    public List<Fruit> getFruitListWithPageNo(Integer pageNo) {
        return super.executeQuery("select * from t_fruit limit ?, 3", (pageNo - 1) * 3);
    }

    @Override
    public List<Fruit> getFruitListWithRegx(String keyword, Integer pageNo) {
        return super.executeQuery("select * from t_fruit where fname like ? limit ?, 3", "%" + keyword + "%", (pageNo - 1) * 3);
    }

    @Override
    public int getFruitPageCount() {
        return ((Long) super.executeComplexQuery("select count(*) from t_fruit")[0]).intValue();
    }

    @Override
    public int getFruitPageCountWithRegx(String keyword) {
        return ((Long) super.executeComplexQuery("select count(*) from t_fruit where fname like ?", "%" + keyword + "%")[0]).intValue();
    }

    @Override
    public boolean addFruit(Fruit fruit) {
        String sql = "insert into t_fruit values(0,?,?,?,?)";
        int count = super.executeUpdate(sql, fruit.getFname(), fruit.getPrice(), fruit.getFcount(), fruit.getRemark());
        //insert语句返回的是自增列的值，而不是影响行数
        //System.out.println(count);
        return count > 0;
    }

    @Override
    public boolean updateFruit(Fruit fruit) {
        String sql = "updat t_fruit set fname = ?, price = ?, fcount = ?, remark = ? where fid = ? ";
        return super.executeUpdate(sql, fruit.getFname(), fruit.getPrice(), fruit.getFcount(), fruit.getRemark(), fruit.getFid()) > 0;
    }

    @Override
    public Fruit getFruitByFname(String fname) {
        return super.load("select * from t_fruit where fname like ? ", fname);
    }

    @Override
    public boolean delFruit(String fname) {
        String sql = "delete from t_fruit where fname like ? ";
        return super.executeUpdate(sql, fname) > 0;
    }

    @Override
    public Fruit getFruitByID(Integer fid) {
        return super.load("select * from t_fruit where fid = ? ", fid);
    }

    @Override
    public boolean delFruitByID(Integer fid) {
        String sql = "delete from t_fruit where fid = ?";
        return super.executeUpdate(sql, fid) > 0;
    }
}