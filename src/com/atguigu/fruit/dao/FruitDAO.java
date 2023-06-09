package com.atguigu.fruit.dao;

import com.atguigu.fruit.pojo.Fruit;

import java.util.List;

public interface FruitDAO {
    //查询库存列表
    List<Fruit> getFruitList();

    //分页查询
    List<Fruit> getFruitListWithPageNo(Integer pageNo);

    //模糊查询
    List<Fruit> getFruitListWithRegx(String keyword, Integer pageNo);

    //获取页数
    int getFruitPageCount();

    //模糊查询获取页数
    int getFruitPageCountWithRegx(String keyword);

    //新增库存
    boolean addFruit(Fruit fruit);

    //修改库存
    boolean updateFruit(Fruit fruit);

    //根据名称查询特定库存
    Fruit getFruitByFname(String fname);

    //删除特定库存记录
    boolean delFruit(String fname);

    //根据ID查询特定库存
    Fruit getFruitByID(Integer fid);

    //根据ID删记录
    boolean delFruitByID(Integer fid);
}
