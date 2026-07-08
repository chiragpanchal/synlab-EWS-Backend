package com.ewsv3.ews.userpref.dto;

public record UserPrefDto(
        Long userPrefId,
        Long userId,
        String tzInternalName,
        String tsarMon,
        String tsarTue,
        String tsarWed,
        String tsarThu,
        String tsarFri,
        String tsarSat,
        String tsarSun,
        Integer tsarHour,
        Integer tsarMinute,
        String tsarNotifyType,
        String time12_24
) {
}
