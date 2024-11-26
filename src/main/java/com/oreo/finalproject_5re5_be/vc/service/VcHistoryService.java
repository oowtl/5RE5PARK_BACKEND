package com.oreo.finalproject_5re5_be.vc.service;

import com.oreo.finalproject_5re5_be.vc.dto.request.VcRequestHistoryRequest;
import com.oreo.finalproject_5re5_be.vc.dto.request.VcResultHistoryRequest;

public interface VcHistoryService {
    /**
     * 1. 요청 이력
     * 2. 결과 이력
     */

    void requestHistorySave(VcRequestHistoryRequest requestHistory);
    void resultHistorySave(VcResultHistoryRequest resultHistory);
}
