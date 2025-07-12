package com.ewsv3.ews.timecard.dto;

import java.util.List;

public final class TimecardObject {
    private TimecardSummary timecardSummary;
    private List<TimecardLine> timecardLines;

    public TimecardObject() {
    }

    public TimecardObject(TimecardSummary timecardSummary, List<TimecardLine> timecardLines) {
        this.timecardSummary = timecardSummary;
        this.timecardLines = timecardLines;
    }

    public TimecardSummary getTimecardSummary() {
        return timecardSummary;
    }

    public void setTimecardSummary(TimecardSummary timecardSummary) {
        this.timecardSummary = timecardSummary;
    }

    public List<TimecardLine> getTimecardLines() {
        return timecardLines;
    }

    public void setTimecardLines(List<TimecardLine> timecardLines) {
        this.timecardLines = timecardLines;
    }
}
