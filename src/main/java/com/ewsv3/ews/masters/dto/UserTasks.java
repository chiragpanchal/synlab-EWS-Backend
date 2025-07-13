package com.ewsv3.ews.masters.dto;

public record UserTasks(
//        String groupName,
//        Integer groupSeq,
//        Integer menuId,
//        String menuName,
//        String menuCode,
//        String canCreate,
//        Integer seq,
//        String icon
        String groupName,
        Integer groupSeq,
        Integer taskId,
        String taskName,
        String taskCode,
        String canCreate,
        String canEdit,
        String canDelete,
        String canView,
        Long userId,
        Integer seq,
        Integer enterpriseId,
        Long employeeId
) {
}
