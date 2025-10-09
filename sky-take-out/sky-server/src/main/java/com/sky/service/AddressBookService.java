package com.sky.service;

import com.sky.entity.AddressBook;

import java.util.List;

public interface AddressBookService {

    void save(AddressBook addressBook);

    List<AddressBook> listByUserId();

    AddressBook getDefaultAddressBookByUserId();

    AddressBook getAddressBookById(Long id);

    void updateById(AddressBook addressBook);

    void setDefault(Long id);

    void deleteById(Long id);
}
