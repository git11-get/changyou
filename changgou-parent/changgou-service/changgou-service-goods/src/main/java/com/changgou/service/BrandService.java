package com.changgou.service;

import com.changgou.goods.pojo.Brand;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface BrandService {

    /**
     * 分页+条件搜索
     * @param brand 搜索条件
     * @param page 当前页
     * @param size 每页显示的条数
     * @return
     */
    PageInfo<Brand> findPage(Brand brand,Integer page,Integer size);

    /**
     * 分页
     * @param page 当前页
     * @param size 每页显示的条数
     * @return
     */
    PageInfo<Brand> findPage(Integer page,Integer size);

    /**
     * 根据品牌信息多条件搜索
     * @param brand
     * @return
     */
    List<Brand> findList(Brand brand);

    /**
     * 根据id删除
     * @param id
     */
    void delete(Integer id );

    /**
     * 根据id修改品牌数据
     * @param brand
     */
    void update(Brand brand);

    /**
     * 增加品牌
     * @param brand
     */
    void add(Brand brand);

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    Brand findById(Integer id);

    /**
     * 查询所有
     * @return
     */
    List<Brand> findAll();
}
