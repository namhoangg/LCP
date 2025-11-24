package com.lcp.dashboard.service;

import com.lcp.dashboard.dto.DashboardAdminResponse;
import com.lcp.dashboard.dto.DashboardClientResponse;

public interface DashboardService {
    /**
     * Get the client dashboard data.
     *
     * @return DashboardClientResponse
     */
    DashboardClientResponse getClientDashboard();

    /**
     * Get the admin dashboard data.
     *
     * @return DashboardAdminResponse
     */
    DashboardAdminResponse getAdminDashboard();
}
