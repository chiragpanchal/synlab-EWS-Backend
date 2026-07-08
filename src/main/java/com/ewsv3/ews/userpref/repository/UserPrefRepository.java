package com.ewsv3.ews.userpref.repository;

import com.ewsv3.ews.userpref.entity.UserPref;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserPrefRepository extends JpaRepository<UserPref, Long> {

    Optional<UserPref> findByUserId(Long userId);

    List<UserPref> findAllByUserId(Long userId);

    boolean existsByUserId(Long userId);

    @Query("SELECT up FROM UserPref up WHERE up.userId = :userId")
    Optional<UserPref> getUserPrefByUserId(@Param("userId") Long userId);
}
