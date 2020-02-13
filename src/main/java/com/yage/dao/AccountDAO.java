package com.yage.dao;


import com.yage.domain.Account;

import java.util.List;

/**
 * @Author LOL_toulan
 * @Message
 */

public interface AccountDAO {

    /**
     * 查询所有
     * @return
     */
    List<Account> findAllAccount();

    /**
     * 通过id查询所有
     *
     * @param id
     * @return
     */
    Account findAccountById(int id);


    /**
     * 保存数据
     * @param account
     */
    void saveAccount(Account account);

    /**
     * 修改
     * @param account
     */
    void updateAccount(Account account);

    /**
     * 通过id删除
     * @param id
     */
    void deleteById(int id);

    /**
     * 通过名称查询账户
     * @param name
     * @return
     */
    Account findByName(String name);

}
