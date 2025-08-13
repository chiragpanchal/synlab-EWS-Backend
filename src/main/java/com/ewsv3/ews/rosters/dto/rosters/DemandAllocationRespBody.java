package com.ewsv3.ews.rosters.dto.rosters;

import java.util.List;

public class DemandAllocationRespBody {
    List<DemandAllocationSummary> allocationSummaryList;

    public DemandAllocationRespBody() {
    }

    public List<DemandAllocationSummary> getAllocationSummaryList() {
        return allocationSummaryList;
    }

    public void setAllocationSummaryList(List<DemandAllocationSummary> allocationSummaryList) {
        this.allocationSummaryList = allocationSummaryList;
    }
}
