package library.management.system.common.utils;

import library.management.system.common.entities.HoldRequest;

import java.util.Comparator;

public class HoldReqComparator implements Comparator<HoldRequest> {

    @Override
    public int compare(HoldRequest holdRequest1, HoldRequest holdRequest2) {
        int result = holdRequest1.getRequestDate().isBefore(holdRequest2.getRequestDate()) ? -1 :
                holdRequest1.getRequestDate().isAfter(holdRequest2.getRequestDate()) ? 1 : 0;
        return result;
    }
}
