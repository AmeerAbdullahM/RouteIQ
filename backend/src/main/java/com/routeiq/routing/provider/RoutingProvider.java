package com.routeiq.routing.provider;

import com.routeiq.routing.dto.RouteAlternativeDTO;
import com.routeiq.routing.dto.RouteRequestDTO;
import java.util.List;

public interface RoutingProvider {
    List<RouteAlternativeDTO> calculateRoutes(RouteRequestDTO request);
}
