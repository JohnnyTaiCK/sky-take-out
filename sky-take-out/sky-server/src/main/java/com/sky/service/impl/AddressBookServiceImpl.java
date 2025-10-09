package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.exception.AddressBookBusinessException;
import com.sky.mapper.AddressBookMapper;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressBookServiceImpl implements AddressBookService {

    @Autowired
    private AddressBookMapper addressBookMapper;

    @Override
    public void save(AddressBook addressBook) {
        if (addressBook == null || addressBook.getDetail().isEmpty()) {
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }

        Long userId = BaseContext.getCurrentId();
        addressBook.setUserId(1L);
        if (addressBook.getIsDefault() == null) {
            addressBook.setIsDefault(0);
        }
        addressBookMapper.insert(addressBook);
    }

    @Override
    public List<AddressBook> listByUserId() {
        Long userId = BaseContext.getCurrentId();

        AddressBook addressBook = AddressBook.builder()
                .userId(1L)
                .build();

        return addressBookMapper.listByUserId(addressBook);
    }

    @Override
    public AddressBook getDefaultAddressBookByUserId() {
        Long userId = BaseContext.getCurrentId();

        AddressBook addressBook = AddressBook.builder()
                .userId(1L)
                .isDefault(1)
                .build();

        List<AddressBook> list = addressBookMapper.listByUserId(addressBook);

        if (list != null && list.size() > 0) {
            addressBook = list.get(0);
        }else {
            addressBook = null;
        }

        return addressBook;
    }

    @Override
    public AddressBook getAddressBookById(Long id) {
        Long userId = BaseContext.getCurrentId();

        AddressBook addressBook = AddressBook.builder()
                .userId(1L)
                .id(id)
                .build();

        List<AddressBook> list = addressBookMapper.listByUserId(addressBook);

        if (list != null && list.size() > 0) {
            addressBook = list.get(0);
        }else {
            addressBook = null;
        }

        return addressBook;
    }

    @Override
    public void updateById(AddressBook addressBook) {
        Long userId = BaseContext.getCurrentId();
        addressBook.setUserId(1L);
        addressBookMapper.update(addressBook);
    }

    @Override
    public void setDefault(Long id) {
        Long userId = BaseContext.getCurrentId();
        AddressBook addressBook = AddressBook.builder()
                .userId(1L)
                .isDefault(0)
                .build();
        //1.先将所有地址改为非默认
        addressBookMapper.update(addressBook);

        //2.再将指定地址改为默认
        addressBook.setId(id);
        addressBook.setIsDefault(1);
        addressBookMapper.update(addressBook);
    }

    @Override
    public void deleteById(Long id) {
        Long userId = BaseContext.getCurrentId();
        addressBookMapper.deleteById(id);
    }
}
