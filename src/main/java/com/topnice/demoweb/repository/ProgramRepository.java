package com.topnice.demoweb.repository;

import com.topnice.demoweb.entity.Program;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface ProgramRepository extends JpaRepository<Program, Integer> {



    Page<Program> findAllByNameContaining(String name, Pageable pageable);

    Page<Program> findAllByEnterIdAndNameContaining(String enterId, String name, Pageable pageable);

    Program findAllByProId(String proId);

    Program findAllByProIdAndEnterId(String proId, String enterId);




}
