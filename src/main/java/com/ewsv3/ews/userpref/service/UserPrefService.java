package com.ewsv3.ews.userpref.service;

import com.ewsv3.ews.auth.dto.UserPrincipal;
import com.ewsv3.ews.commons.dto.DMLResponseDto;
import com.ewsv3.ews.userpref.dto.UserPrefDto;
import com.ewsv3.ews.userpref.dto.UserPrefRequestDto;
import com.ewsv3.ews.userpref.entity.UserPref;
import com.ewsv3.ews.userpref.repository.UserPrefRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserPrefService {

    private final UserPrefRepository userPrefRepository;

    public UserPrefService(UserPrefRepository userPrefRepository) {
        this.userPrefRepository = userPrefRepository;
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            return userPrincipal.getUserId();
        }
        throw new RuntimeException("User not authenticated or invalid token");
    }

    public Optional<UserPrefDto> getUserPreferenceById(Long id) {
        return userPrefRepository.findById(id)
                .map(this::convertToDto);
    }

    public Optional<UserPrefDto> getUserPreferenceForCurrentUser() {
        Long currentUserId = getCurrentUserId();
        return userPrefRepository.findByUserId(currentUserId)
                .map(this::convertToDto);
    }

    @Transactional
    private UserPref saveUserPref(UserPref userPref) {
        return userPrefRepository.save(userPref);
    }

    public DMLResponseDto createUserPreference(UserPrefRequestDto request) {
        try {
            Long currentUserId = getCurrentUserId();
            Date now = new Date();

            UserPref userPref = new UserPref();
            userPref.setUserId(currentUserId);
            userPref.setTzInternalName(request.tzInternalName());
            userPref.setTsarMon(request.tsarMon() != null ? request.tsarMon() : "N");
            userPref.setTsarTue(request.tsarTue() != null ? request.tsarTue() : "N");
            userPref.setTsarWed(request.tsarWed() != null ? request.tsarWed() : "N");
            userPref.setTsarThu(request.tsarThu() != null ? request.tsarThu() : "N");
            userPref.setTsarFri(request.tsarFri() != null ? request.tsarFri() : "N");
            userPref.setTsarSat(request.tsarSat() != null ? request.tsarSat() : "N");
            userPref.setTsarSun(request.tsarSun() != null ? request.tsarSun() : "N");
            userPref.setTsarHour(request.tsarHour());
            userPref.setTsarMinute(request.tsarMinute());
            userPref.setTsarNotifyType(request.tsarNotifyType());
            userPref.setTime12_24(request.time12_24());
            userPref.setCreatedBy(currentUserId);
            userPref.setCreatedOn(now);
            userPref.setLastUpdatedBy(currentUserId);
            userPref.setLastUpdateDate(now);

            UserPref savedUserPref = saveUserPref(userPref);
            return new DMLResponseDto("Success", "User preference created successfully.");
        } catch (Exception e) {
            return new DMLResponseDto("Error", "Error creating user preference: " + e.getMessage());
        }
    }

    @Transactional
    private UserPref updateUserPrefRecord(UserPref userPref) {
        return userPrefRepository.save(userPref);
    }

    public DMLResponseDto updateUserPreference(Long id, UserPrefRequestDto request) {
        try {
            Optional<UserPref> optionalUserPref = userPrefRepository.findById(id);
            if (optionalUserPref.isEmpty()) {
                return new DMLResponseDto("Error", "User preference not found");
            }

            Long currentUserId = getCurrentUserId();
            Date now = new Date();
            UserPref userPref = optionalUserPref.get();

            userPref.setUserId(currentUserId);
            userPref.setTzInternalName(request.tzInternalName());
            userPref.setTsarMon(request.tsarMon() != null ? request.tsarMon() : "N");
            userPref.setTsarTue(request.tsarTue() != null ? request.tsarTue() : "N");
            userPref.setTsarWed(request.tsarWed() != null ? request.tsarWed() : "N");
            userPref.setTsarThu(request.tsarThu() != null ? request.tsarThu() : "N");
            userPref.setTsarFri(request.tsarFri() != null ? request.tsarFri() : "N");
            userPref.setTsarSat(request.tsarSat() != null ? request.tsarSat() : "N");
            userPref.setTsarSun(request.tsarSun() != null ? request.tsarSun() : "N");
            userPref.setTsarHour(request.tsarHour());
            userPref.setTsarMinute(request.tsarMinute());
            userPref.setTsarNotifyType(request.tsarNotifyType());
            userPref.setTime12_24(request.time12_24());
            userPref.setLastUpdatedBy(currentUserId);
            userPref.setLastUpdateDate(now);

            updateUserPrefRecord(userPref);
            return new DMLResponseDto("Success", "User preference updated successfully");
        } catch (Exception e) {
            return new DMLResponseDto("Error", "Error updating user preference: " + e.getMessage());
        }
    }

    @Transactional
    private void deleteUserPrefRecord(Long id) {
        userPrefRepository.deleteById(id);
    }

    public DMLResponseDto deleteUserPreference(Long id) {
        try {
            if (!userPrefRepository.existsById(id)) {
                return new DMLResponseDto("Error", "User preference not found");
            }

            deleteUserPrefRecord(id);
            return new DMLResponseDto("Success", "User preference deleted successfully");
        } catch (Exception e) {
            return new DMLResponseDto("Error", "Error deleting user preference: " + e.getMessage());
        }
    }

    private UserPrefDto convertToDto(UserPref userPref) {
        return new UserPrefDto(
                userPref.getUserPrefId(),
                userPref.getUserId(),
                userPref.getTzInternalName(),
                userPref.getTsarMon(),
                userPref.getTsarTue(),
                userPref.getTsarWed(),
                userPref.getTsarThu(),
                userPref.getTsarFri(),
                userPref.getTsarSat(),
                userPref.getTsarSun(),
                userPref.getTsarHour(),
                userPref.getTsarMinute(),
                userPref.getTsarNotifyType(),
                userPref.getTime12_24()
        );
    }
}
