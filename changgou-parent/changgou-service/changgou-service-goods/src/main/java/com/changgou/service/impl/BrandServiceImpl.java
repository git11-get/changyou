package com.changgou.service.impl;

import com.changgou.dao.BrandMapper;
import com.changgou.goods.pojo.Brand;
import com.changgou.service.BrandService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandMapper brandMapper;

    @Override
    public PageInfo<Brand> findPage(Brand brand, Integer page, Integer size) {

        //分页
        PageHelper.startPage(page,size );
        //条件搜索数据
        Example example = createExample(brand);
        List<Brand> brands = this.brandMapper.selectByExample(example);
        //封装PageInfo对象实体
        return new PageInfo<Brand>(brands);
    }

    /**
     * 分页实现 PageHelper.startPage(page, size);分页实现后，后面的查询紧跟集合查询
     * 1. 当前页
     * 2. 每页显示多少条
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<Brand> findPage(Integer page, Integer size) {
        //操作页面的工具类
        PageHelper.startPage(page, size); //使用该分页方法后，会生效，然后执行下面的代码
        //查询集合
        List<Brand> brands = this.brandMapper.selectAll();
        //封装PageInfo对象实体
        return new PageInfo<Brand>(brands);
    }

    /**
     * 多条件搜索
     * @param brand
     * @return
     */
    @Override
    public List<Brand> findList(Brand brand) {
        //调用方法构建条件
        Example example = createExample(brand);
        return this.brandMapper.selectByExample(example);
    }

    /**
     * 条件构建
     * @param brand
     * @return
     */
    public Example createExample(Brand brand) {
        Example example = new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();
        if(brand != null){
            if(!StringUtils.isEmpty(brand.getName())){
                criteria.andLike("name","%"+brand.getName()+"%" );
            }
            if(!StringUtils.isEmpty(brand.getLetter())){
                criteria.andEqualTo("letter",brand.getLetter() );
            }
        }
        return example;
    }

    /**
     * 根据id删除
     * @param id
     */
    @Override
    public void delete(Integer id) {
        this.brandMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Brand brand) {
        this.brandMapper.updateByPrimaryKeySelective(brand);
    }

    /**
     * 增加品牌
     * @param brand
     */
    @Override
    public void add(Brand brand) {

        this.brandMapper.insertSelective(brand);

    }

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    @Override
    public Brand findById(Integer id) {
        return this.brandMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询所有
     * @return
     */
    @Override
    public List<Brand> findAll() {

        return this.brandMapper.selectAll();
    }
}
