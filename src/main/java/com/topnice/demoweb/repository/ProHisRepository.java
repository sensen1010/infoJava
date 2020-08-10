package com.topnice.demoweb.repository;

import com.topnice.demoweb.entity.ProHis;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface ProHisRepository extends JpaRepository<ProHis, Integer> {


    Page<ProHis> findAllByNameContaining(String name, Pageable pageable);

    Page<ProHis> findAllByEnterIdAndNameContaining(String enterId,String name,Pageable pageable);



}
