package com.topnice.demoweb.repository;


import com.topnice.demoweb.entity.FileUrl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface FileUrlRepository extends JpaRepository<FileUrl, Integer> {

    FileUrl findAllByFileMd5(String imgMd5);

    Page<FileUrl> findAllByFileNameContainingAndStateContaining(String enterId, String imgName, String state, Pageable pageable);

    List<FileUrl> findAllByStateContaining(String state);

    FileUrl findAllByFileUrlId(String FileId);

    Page<FileUrl> findAllByFileTypeId(String fileTypeId, Pageable pageable);
}
