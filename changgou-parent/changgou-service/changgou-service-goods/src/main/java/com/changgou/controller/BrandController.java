package com.changgou.controller;


import com.changgou.goods.pojo.Brand;
import com.changgou.service.BrandService;
import com.github.pagehelper.PageInfo;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    /**
     * 条件分页查询实现
     * @param page
     * @param size
     * @return
     */
    @PostMapping("/search/{page}/{size}")
    public Result<PageInfo<Brand>> findPage(@RequestBody Brand brand,
                                            @PathVariable("page")Integer page,
                                            @PathVariable("size")Integer size){
        PageInfo<Brand> pageInfo = this.brandService.findPage(brand,page, size);
        return new Result<PageInfo<Brand>>(true,StatusCode.OK,"分页搜索查询",pageInfo);
    }

    /**
     * 分页查询实现
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/search/{page}/{size}")
    public Result<PageInfo<Brand>> findPage(@PathVariable("page")Integer page,
                                    @PathVariable("size")Integer size){
        int i = 1/0;
        PageInfo<Brand> pageInfo = this.brandService.findPage(page, size);
        return new Result<PageInfo<Brand>>(true,StatusCode.OK,"分页搜索查询",pageInfo);
    }

    /**
     * 条件查询
     * @param brand
     * @return
     */
    @PostMapping("search")
    public Result<List<Brand>> findList(@RequestBody Brand brand){
        List<Brand> brands = this.brandService.findList(brand);
        return new Result<List<Brand>>(true,StatusCode.OK,"条件搜索查询",brands);
    }


    @DeleteMapping("{id}")
    public Result delete(@PathVariable("id") Integer id){
        this.brandService.delete(id);
        return new Result(true,StatusCode.OK,"根据id删除品牌成功");
    }

    /**
     *
     * @param id
     * @param brand
     * @return
     */
    @PutMapping("{id}")
    public Result update(@PathVariable("id")Integer id,@RequestBody Brand brand){
        brand.setId(id);
        this.brandService.update(brand);
        return new Result(true,StatusCode.OK,"根据id修改品牌成功");

    }

    @PostMapping
    public Result add(@RequestBody Brand brand){
        this.brandService.add(brand);
        return new Result(true,StatusCode.OK,"增加品牌成功");
    }

    /**
     * 根据主键id查询
     * @param id
     */
    @GetMapping("{id}")
    public Result<Brand> findById(@PathVariable("id") Integer id){
        Brand brand = this.brandService.findById(id);
        return new Result<Brand>(true,StatusCode.OK,"根据Id查询brand成功",brand);
    }

    /**
     * 查询所有品牌
     */
    @GetMapping
    public Result<List<Brand>> findAll(){
        //查询所有品牌
        List<Brand> brands = this.brandService.findAll();

        return new Result<List<Brand>>(true,StatusCode.OK,"查询品牌集合成功！",brands);
    }
}
