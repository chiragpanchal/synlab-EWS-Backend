package com.ewsv3.ews.reports.dto.attendanceConsolidation;

import java.util.List;

public class AttendanceConsolidationResp {

    AttendTimecardPersonRespBody personRespBody;
    List<AttendSchViolRespBody> schViolRespBodies;
    List<AttendActualLinesRespBody> actualLinesRespBodies;
    List<AttendLeavesRespBody> leavesRespBodies;
    List<AttendHolidaysRespBody> holidaysRespBodies;

    public AttendanceConsolidationResp() {
    }

    public AttendTimecardPersonRespBody getPersonRespBody() {
        return personRespBody;
    }

    public void setPersonRespBody(AttendTimecardPersonRespBody personRespBody) {
        this.personRespBody = personRespBody;
    }

    public List<AttendSchViolRespBody> getSchViolRespBodies() {
        return schViolRespBodies;
    }

    public void setSchViolRespBodies(List<AttendSchViolRespBody> schViolRespBodies) {
        this.schViolRespBodies = schViolRespBodies;
    }

    public List<AttendActualLinesRespBody> getActualLinesRespBodies() {
        return actualLinesRespBodies;
    }

    public void setActualLinesRespBodies(List<AttendActualLinesRespBody> actualLinesRespBodies) {
        this.actualLinesRespBodies = actualLinesRespBodies;
    }

    public List<AttendLeavesRespBody> getLeavesRespBodies() {
        return leavesRespBodies;
    }

    public void setLeavesRespBodies(List<AttendLeavesRespBody> leavesRespBodies) {
        this.leavesRespBodies = leavesRespBodies;
    }

    public List<AttendHolidaysRespBody> getHolidaysRespBodies() {
        return holidaysRespBodies;
    }

    public void setHolidaysRespBodies(List<AttendHolidaysRespBody> holidaysRespBodies) {
        this.holidaysRespBodies = holidaysRespBodies;
    }
}
