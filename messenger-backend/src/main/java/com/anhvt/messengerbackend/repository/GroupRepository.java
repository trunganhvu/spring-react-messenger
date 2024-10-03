package com.anhvt.messengerbackend.repository;

import com.anhvt.messengerbackend.entity.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Comment class
 *
 * @author trunganhvu
 * @date 9/22/2024
 */
@Repository
public interface GroupRepository extends JpaRepository<GroupEntity, Integer> {

    @Query(value = "SELECT g.id FROM chat_group g WHERE g.url = :url", nativeQuery = true)
    int findGroupIdByUrl(@Param(value = "url") String url);

    @Query(value = "SELECT g.name FROM chat_group g WHERE g.url = :url", nativeQuery = true)
    String getGroupEntitiesBy(@Param(value = "url") String url);

    @Query(value = "SELECT * FROM chat_group g WHERE g.url = :url", nativeQuery = true)
    GroupEntity getGroupByUrl(@Param(value = "url") String url);

    @Query(value = "SELECT g.url FROM chat_group g WHERE g.id = :id", nativeQuery = true)
    String getGroupUrlById(@Param(value = "id") Integer id);
}
