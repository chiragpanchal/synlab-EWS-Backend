package com.ewsv3.ews.rosters.dto.rosters.payload.pivot;


import com.ewsv3.ews.rosters.dto.rosters.RosterLines;

import java.util.List;

public class PersonRosterSqlResp {
    private List<RosterLines> rosterLines;
    private String kpiString;

    public PersonRosterSqlResp() {
    }

    public PersonRosterSqlResp(List<RosterLines> rosterLines, String kpiString) {
        this.rosterLines = rosterLines;
        this.kpiString = kpiString;
    }

    public List<RosterLines> getRosterLines() {
        return rosterLines;
    }

    public void setRosterLines(List<RosterLines> rosterLines) {
        this.rosterLines = rosterLines;
    }

    public String getKpiString() {
        return kpiString;
    }

    public void setKpiString(String kpiString) {
        this.kpiString = kpiString;
    }

    @Override
    public String toString() {
        return "PersonRosterSqlResp{" +
                "rosterLines=" + rosterLines +
                ", kpiString='" + kpiString + '\'' +
                '}';
    }
}
