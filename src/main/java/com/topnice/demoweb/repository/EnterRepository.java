package com.topnice.demoweb.repository;


import com.topnice.demoweb.entity.Enterprise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface EnterRepository extends JpaRepository<Enterprise, Integer> {

    Enterprise findAllByEnterNameContaining(String enterName);

    Page<Enterprise> findAllByStateContainingAndEnterNameContaining(String state, String enterName, Pageable pageable);

    Enterprise findAllByEnterId(String enterId);

    List<Enterprise> findAllByStateContaining(String state);

}
