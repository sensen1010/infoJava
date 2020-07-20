package com.topnice.demoweb.repository;

import com.topnice.demoweb.entity.InfoLayout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface InfoLayoutRepository extends JpaRepository<InfoLayout, Integer> {

    List<InfoLayout> findAllByType(String type);

    InfoLayout findAllByUuid(String uuid);

}
