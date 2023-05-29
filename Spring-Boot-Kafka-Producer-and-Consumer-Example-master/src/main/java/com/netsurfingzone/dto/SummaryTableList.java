package com.netsurfingzone.dto;

import java.util.ArrayList;
import java.util.List;

public class SummaryTableList {
    List<SummaryTable> summaryTableList = new ArrayList<SummaryTable>();

    public List<SummaryTable> getSummaryTableList() {
        return summaryTableList;
    }

    public void setSummaryTableList(List<SummaryTable> summaryTableList) {
        this.summaryTableList = summaryTableList;
    }
}
