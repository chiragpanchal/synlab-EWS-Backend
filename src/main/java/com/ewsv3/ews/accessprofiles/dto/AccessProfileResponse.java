package com.ewsv3.ews.accessprofiles.dto;

import java.util.List;

public class AccessProfileResponse {

    AccessProfiles accessProfiles;
    List<UserProfileAssoc> userProfileAssocList;
    List<AccessProfileLines> accessProfileLinesList;

    public AccessProfileResponse() {
    }

    public AccessProfileResponse(AccessProfiles accessProfiles, List<UserProfileAssoc> userProfileAssocList, List<AccessProfileLines> accessProfileLinesList) {
        this.accessProfiles = accessProfiles;
        this.userProfileAssocList = userProfileAssocList;
        this.accessProfileLinesList = accessProfileLinesList;
    }

    public AccessProfiles getAccessProfiles() {
        return accessProfiles;
    }

    public void setAccessProfiles(AccessProfiles accessProfiles) {
        this.accessProfiles = accessProfiles;
    }

    public List<UserProfileAssoc> getUserProfileAssocList() {
        return userProfileAssocList;
    }

    public void setUserProfileAssocList(List<UserProfileAssoc> userProfileAssocList) {
        this.userProfileAssocList = userProfileAssocList;
    }

    public List<AccessProfileLines> getAccessProfileLinesList() {
        return accessProfileLinesList;
    }

    public void setAccessProfileLinesList(List<AccessProfileLines> accessProfileLinesList) {
        this.accessProfileLinesList = accessProfileLinesList;
    }
}
