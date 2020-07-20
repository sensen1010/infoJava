package com.topnice.demoweb.repository;


import com.topnice.demoweb.entity.HostsImg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface HostsImgRepository extends JpaRepository<HostsImg, Integer> {

    //根据主机id/图片id+state 模糊查询
    Page<HostsImg> findAllByHostIdContainingAndImgUrlIdContainingAndStateContaining(String hostId, String imgUrl, String state, Pageable pageable);

    //根据主机id 图片id查询记录
    HostsImg findAllByHostIdAndImgUrlId(String hostId, String imgUrlId);

    //根据主机id查询
    HostsImg findAllByHostId(String hostId);

    //根据主机id，状态查询
    List<HostsImg> findAllByHostIdContainingAndStateContaining(String hostId, String state);

    //根据图片uuid查询
    List<HostsImg> findAllByImgUrlId(String imgUrlId);
}
