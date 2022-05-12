package com.meituan.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meituan.reggie.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {

}
