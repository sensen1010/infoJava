package com.topnice.demoweb.repository;

import com.topnice.demoweb.entity.ClientUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface ClientUpdateRepository extends JpaRepository<ClientUpdate, Integer> {

    List<ClientUpdate> findAllByApkMd5(String apkMd5);


    @Query(value = "select * from client_update order by id DESC limit 1", nativeQuery = true)
    ClientUpdate findLastOne();

}
