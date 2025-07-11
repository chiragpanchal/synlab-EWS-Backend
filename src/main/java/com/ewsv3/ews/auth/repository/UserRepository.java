package com.ewsv3.ews.auth.repository;

import com.ewsv3.ews.auth.dto.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    @Query("SELECT u FROM User u WHERE u.userName = :username")
    Optional<User> findByUserName(@Param("username") String username);
    
    @Query("SELECT u FROM User u WHERE u.userName = :username AND u.userId = :userId")
    Optional<User> findByUserNameAndUserId(@Param("username") String username, @Param("userId") Long userId);
    
    boolean existsByUserName(String username);
}
