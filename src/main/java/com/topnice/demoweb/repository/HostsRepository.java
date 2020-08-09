package com.topnice.demoweb.repository;


import com.topnice.demoweb.entity.Hosts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface HostsRepository extends JpaRepository<Hosts, Integer> {

    //根据主机id查询主机信息
    Hosts findAllByHostId(String hostId);

    //根据主机id+主机状态查询
    Hosts findAllByHostIdAndHostState(String hostId, String hostState);

    //根据链接状态、主机状态分页查询机器
    Page<Hosts> findAllByHostNameContainingAndHostStateContainingAndLinkStateContaining(String hostName, String hostState, String linkState, Pageable pageable);

    Hosts findAllByHostLinkId(String hostLinkId);

    Hosts findAllByEnterIdAndHostLinkId(String enterId,String hostLinkId);

    //根据企业Id、名称分页查询
    Page<Hosts> findByEnterIdAndHostNameContainingAndLinkStateContaining(String enterId,String hostName,String linkState,Pageable pageable);


}
