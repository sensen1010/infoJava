package com.topnice.demoweb.repository;

import com.topnice.demoweb.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {

    Users findAllByUserNameAndPow(String userName, String pow);

    Users findAllByUserId(String userId);

    //根据企业id、账号名称（模糊）查询用户
    Page<Users> findAllByEnterIdAndNameContainingAndState(String enterId, String name, String state, Pageable pageable);

    //根据企业id（模糊）、账号名称（模糊）查询用户  超级管理员接口
    Page<Users> findAllByEnterIdContainingAndNameContainingAndState(String enterId, String name, String state, Pageable pageable);

    Users findAllByName(String name);

    Users findAllByUserName(String userName);

    Users findAllByUserNameAndEnterId(String userName, String enterId);

    Users findAllByUserIdAndEnterId(String userId, String enterId);


}
