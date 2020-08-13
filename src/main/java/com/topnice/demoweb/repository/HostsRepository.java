package com.topnice.demoweb.repository;


import com.topnice.demoweb.entity.Hosts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface HostsRepository extends JpaRepository<Hosts, Integer> {

    //根据主机id查询主机信息
    Hosts findAllByHostId(String hostId);

    //根据主机id+主机状态查询
    Hosts findAllByHostIdAndHostState(String hostId, String hostState);

    //根据链接状态、主机状态分页查询机器
    Page<Hosts> findAllByHostNameContainingAndHostStateContainingAndLinkStateContaining(String enterId, String hostName, String linkState, Pageable pageable);

    Page<Hosts> findAllByEnterIdContainingAndHostNameContainingAndLinkStateContaining(String enterId, String hostName, String linkState, Pageable pageable);

    Hosts findAllByHostLinkId(String hostLinkId);

    Hosts findAllByEnterIdAndHostLinkId(String enterId,String hostLinkId);

    //根据企业Id、名称分页查询
    Page<Hosts> findByEnterIdAndHostNameContainingAndLinkStateContaining(String enterId,String hostName,String linkState,Pageable pageable);
    //根据企业id、连接id查询
    List<Hosts> findAllByEnterIdAndLinkStateAndHostState(String enterId, String linkState, String hostState);

    @Modifying
    @Query(value ="update Hosts set link_state='1' WHERE link_state='0'",nativeQuery = true)
    void updateAllHostLinkState();

}
