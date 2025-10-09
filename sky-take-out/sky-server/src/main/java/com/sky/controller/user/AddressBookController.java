package com.sky.controller.user;

import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/addressBook")
@Slf4j
@Api(tags = "C端-地址簿接口")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    @PostMapping
    @ApiOperation("新增地址簿")
    public Result save(@RequestBody AddressBook addressBook) {
        log.info("新增地址簿:{}", addressBook);
        addressBookService.save(addressBook);
        return Result.success();
    }

    @GetMapping("/list")
    @ApiOperation("查看地址簿")
    public Result<List<AddressBook>> list() {
        List<AddressBook> list = addressBookService.listByUserId();
        return Result.success(list);
    }

    @GetMapping("/default")
    @ApiOperation("获取默认地址")
    public Result<AddressBook> getDefaultAddressBookByUserId() {
        AddressBook addressBook = addressBookService.getDefaultAddressBookByUserId();
        if (addressBook == null) {
            return Result.error("没有查询到默认地址");
        }
        return Result.success(addressBook);
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询地址")
    public Result<AddressBook> getAddressBookById(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getAddressBookById(id);
        return Result.success(addressBook);
    }

    @PutMapping
    @ApiOperation("根据id修改地址")
    public Result updateById(@RequestBody AddressBook addressBook) {
        addressBookService.updateById(addressBook);
        return Result.success();
    }

    @PutMapping("/default")
    @ApiOperation("设置默认地址")
    public Result setDefault(@RequestBody Long id) {
        log.info("设置默认地址:{}", id);
        addressBookService.setDefault(id);
        return Result.success();
    }

    @DeleteMapping
    @ApiOperation("根据id删除地址")
    public Result deleteById(Long id) {
        log.info("删除地址:{}", id);
        addressBookService.deleteById(id);
        return Result.success();
    }
}
