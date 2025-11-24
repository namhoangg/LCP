package com.lcp.dashboard.controller;

import com.lcp.common.Response;
import com.lcp.dashboard.dto.DashboardAdminResponse;
import com.lcp.dashboard.dto.DashboardClientResponse;
import com.lcp.dashboard.service.DashboardService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;
    @GetMapping(value = "/client")
    public Response<DashboardClientResponse> getClientDashboard() {
        return Response.success(dashboardService.getClientDashboard());
    }

    @GetMapping(value = "/admin")
    public Response<DashboardAdminResponse> getAdminDashboard() {
        return Response.success(dashboardService.getAdminDashboard());
    }
}
