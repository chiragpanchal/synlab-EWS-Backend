package com.ewsv3.ews.menus.repository;

import com.ewsv3.ews.menus.model.UserTask;
import com.ewsv3.ews.menus.model.UserTaskId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserTaskRepository extends JpaRepository<UserTask, UserTaskId> {

    /**
     * Find all user tasks by user ID (distinct to avoid duplicates)
     */
    // @Query("SELECT DISTINCT ut FROM UserTask ut WHERE ut.userId = :userId ORDER BY ut.groupSeq, ut.seq, ut.taskName")
    // List<UserTask> findByUserId(@Param("userId") Long userId);

    // /**
    //  * Find unique user tasks by user ID (grouped by task ID to avoid duplicates)
    //  */
    // @Query("SELECT ut FROM UserTask ut WHERE ut.userId = :userId AND ut.id IN " +
    //        "(SELECT MIN(ut2.id) FROM UserTask ut2 WHERE ut2.userId = :userId GROUP BY ut2.taskId) " +
    //        "ORDER BY ut.groupSeq, ut.seq, ut.taskName")
    // List<UserTask> findUniqueTasksByUserId(@Param("userId") Long userId);

    // /**
    //  * Find all user tasks by user ID and enterprise ID
    //  */
    // List<UserTask> findByUserIdAndEnterpriseId(Long userId, Long enterpriseId);

    // /**
    //  * Find all user tasks by user ID and employee ID
    //  */
    // List<UserTask> findByUserIdAndEmployeeId(Long userId, Long employeeId);

    // /**
    //  * Find all user tasks by user ID with specific permissions
    //  */
    // @Query("SELECT ut FROM UserTask ut WHERE ut.userId = :userId AND ut.canView = 'Y'")
    // List<UserTask> findViewableTasksByUserId(@Param("userId") Long userId);

    // /**
    //  * Find all user tasks by user ID that can be created
    //  */
    // @Query("SELECT ut FROM UserTask ut WHERE ut.userId = :userId AND ut.canCreate = 'Y'")
    // List<UserTask> findCreatableTasksByUserId(@Param("userId") Long userId);

    // /**
    //  * Find all user tasks by user ID that can be edited
    //  */
    // @Query("SELECT ut FROM UserTask ut WHERE ut.userId = :userId AND ut.canEdit = 'Y'")
    // List<UserTask> findEditableTasksByUserId(@Param("userId") Long userId);

    // /**
    //  * Find all user tasks by user ID that can be deleted
    //  */
    // @Query("SELECT ut FROM UserTask ut WHERE ut.userId = :userId AND ut.canDelete = 'Y'")
    // List<UserTask> findDeletableTasksByUserId(@Param("userId") Long userId);

    // /**
    //  * Find user tasks grouped by group name for a specific user
    //  */
    // @Query("SELECT ut FROM UserTask ut WHERE ut.userId = :userId ORDER BY ut.groupSeq, ut.taskName")
    // List<UserTask> findByUserIdOrderByGroupSeqAndTaskName(@Param("userId") Long userId);

    // /**
    //  * Find user tasks by group name and user ID
    //  */
    // @Query("SELECT ut FROM UserTask ut WHERE ut.userId = :userId AND ut.groupName = :groupName ORDER BY ut.taskName")
    // List<UserTask> findByUserIdAndGroupName(@Param("userId") Long userId, @Param("groupName") String groupName);

    /**
     * Find all user tasks by user ID where user has at least CREATE or VIEW permissions
     */
    @Query("SELECT ut FROM UserTask ut WHERE ut.id.userId = :userId AND (ut.canCreate = 'Y' OR ut.canView = 'Y') ORDER BY ut.groupSeq, ut.id.seq, ut.taskName")
    List<UserTask> findAccessibleTasksByUserId(@Param("userId") Long userId);
}
