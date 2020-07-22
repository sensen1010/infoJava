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

    Users findAllByUserNameAndPassword(String userName, String password);

    Users findAllByUserId(String userId);

    Page<Users> findAllByEnterIdAndNameContaining(String enterId, String name, Pageable pageable);

    Users findAllByName(String name);

    Users findAllByUserNameAndEnterId(String userName, String enterId);

    Users findAllByUserIdAndEnterId(String userId, String enterId);


}
