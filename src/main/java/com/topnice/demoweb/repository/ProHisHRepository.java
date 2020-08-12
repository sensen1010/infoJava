package com.topnice.demoweb.repository;

import com.topnice.demoweb.entity.ProHisH;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProHisHRepository extends JpaRepository<ProHisH, Integer> {

    List<ProHisH> findAllByProHisId(String proHisId);

}
