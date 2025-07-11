package com.ewsv3.ews.menus.service;

import com.ewsv3.ews.menus.model.UserTask;
import com.ewsv3.ews.menus.repository.UserTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserTaskService {

    @Autowired
    private UserTaskRepository userTaskRepository;

    /**
     * Get all accessible tasks for a specific user
     * With proper composite key, duplicates should be handled at entity level
     */
    public List<UserTask> getUserTasks(Long userId) {
        System.out.println("getUserTasks userId: " + userId);
        List<UserTask> allTasks = userTaskRepository.findAccessibleTasksByUserId(userId);
        System.out.println("Total tasks fetched from DB: " + allTasks.size());
        
        // Return all tasks - composite key should ensure uniqueness
        return allTasks;
    }

    /**
     * Get all tasks for a specific user and enterprise
     */
    // public List<UserTask> getUserTasksByEnterprise(Long userId, Long enterpriseId) {
    //     return userTaskRepository.findByUserIdAndEnterpriseId(userId, enterpriseId);
    // }

    // /**
    //  * Get all tasks for a specific user and employee
    //  */
    // public List<UserTask> getUserTasksByEmployee(Long userId, Long employeeId) {
    //     return userTaskRepository.findByUserIdAndEmployeeId(userId, employeeId);
    // }

    // /**
    //  * Get only viewable tasks for a user
    //  */
    // public List<UserTask> getViewableUserTasks(Long userId) {
    //     return userTaskRepository.findViewableTasksByUserId(userId);
    // }

    // /**
    //  * Get tasks that user can create
    //  */
    // public List<UserTask> getCreatableUserTasks(Long userId) {
    //     return userTaskRepository.findCreatableTasksByUserId(userId);
    // }

    // /**
    //  * Get tasks that user can edit
    //  */
    // public List<UserTask> getEditableUserTasks(Long userId) {
    //     return userTaskRepository.findEditableTasksByUserId(userId);
    // }

    // /**
    //  * Get tasks that user can delete
    //  */
    // public List<UserTask> getDeletableUserTasks(Long userId) {
    //     return userTaskRepository.findDeletableTasksByUserId(userId);
    // }

    // /**
    //  * Get user tasks ordered by group sequence and task name
    //  */
    // public List<UserTask> getUserTasksOrdered(Long userId) {
    //     return userTaskRepository.findByUserIdOrderByGroupSeqAndTaskName(userId);
    // }

    // /**
    //  * Get user tasks grouped by group name
    //  */
    // public Map<String, List<UserTask>> getUserTasksGroupedByGroup(Long userId) {
    //     List<UserTask> tasks = getUserTasksOrdered(userId);
    //     return tasks.stream()
    //             .collect(Collectors.groupingBy(UserTask::getGroupName));
    // }

    // /**
    //  * Get user tasks by specific group
    //  */
    // public List<UserTask> getUserTasksByGroup(Long userId, String groupName) {
    //     return userTaskRepository.findByUserIdAndGroupName(userId, groupName);
    // }

    /**
     * Check if user has specific permission for a task
     */
    public boolean hasPermission(Long userId, Long taskId, String permission) {
        List<UserTask> userTasks = getUserTasks(userId);
        return userTasks.stream()
                .filter(task -> task.getTaskId().equals(taskId))
                .anyMatch(task -> {
                    switch (permission.toLowerCase()) {
                        case "view":
                            return "Y".equals(task.getCanView());
                        case "create":
                            return "Y".equals(task.getCanCreate());
                        case "edit":
                            return "Y".equals(task.getCanEdit());
                        case "delete":
                            return "Y".equals(task.getCanDelete());
                        default:
                            return false;
                    }
                });
    }

    /**
     * Get menu structure for user
     */
    // public Map<String, Object> getUserMenuStructure(Long userId) {
    //     Map<String, List<UserTask>> groupedTasks = getUserTasksGroupedByGroup(userId);
        
    //     return groupedTasks.entrySet().stream()
    //             .collect(Collectors.toMap(
    //                 Map.Entry::getKey,
    //                 entry -> entry.getValue().stream()
    //                         .map(task -> {
    //                             Map<String, Object> taskInfo = Map.of(
    //                                 "taskId", task.getTaskId(),
    //                                 "taskName", task.getTaskName(),
    //                                 "taskCode", task.getTaskCode(),
    //                                 "permissions", Map.of(
    //                                     "canView", "Y".equals(task.getCanView()),
    //                                     "canCreate", "Y".equals(task.getCanCreate()),
    //                                     "canEdit", "Y".equals(task.getCanEdit()),
    //                                     "canDelete", "Y".equals(task.getCanDelete())
    //                                 )
    //                             );
    //                             return taskInfo;
    //                         })
    //                         .collect(Collectors.toList())
    //             ));
    // }

    /**
     * Get raw tasks for debugging (no duplicate removal)
     */
    public List<UserTask> getUserTasksRaw(Long userId) {
        return userTaskRepository.findAccessibleTasksByUserId(userId);
    }
}
