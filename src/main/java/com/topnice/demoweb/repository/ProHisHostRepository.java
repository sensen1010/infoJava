package com.topnice.demoweb.repository;

import com.topnice.demoweb.entity.ProHisHost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface ProHisHostRepository extends JpaRepository<ProHisHost, Integer> {


}
