package com.routeiq.traffic.provider;

import com.routeiq.routing.dto.RouteAlternativeDTO;
import java.util.List;

public interface TrafficProvider {
    void applyTraffic(List<RouteAlternativeDTO> routes, String trafficMode);
}
