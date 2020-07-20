package com.topnice.demoweb.repository;


import com.topnice.demoweb.entity.ImgUrl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface ImgUrlRepository extends JpaRepository<ImgUrl, Integer> {

    ImgUrl findAllByImgMd5(String imgMd5);

    Page<ImgUrl> findAllByImgNameContainingAndStateContainingAndShowTypeContaining(String imgName, String state, String showType, Pageable pageable);

    List<ImgUrl> findAllByShowTypeContainingAndStateContaining(String showType, String state);

    ImgUrl findAllByImgUrlId(String imgUrlId);

    //Page<ImgUrl> findAll(String fileType, Pageable pageable);
    Page<ImgUrl> findAllByFileTypeContaining(String fileType, Pageable pageable);
}
