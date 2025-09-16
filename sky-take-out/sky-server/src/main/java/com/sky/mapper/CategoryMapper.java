package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper {

    void insert(Category category);

    List<Category> list(Integer type);

    Page<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    void update(Category category);

    @Delete("delete from category where id = #{id}")
    void deleteById(Long id);
}
