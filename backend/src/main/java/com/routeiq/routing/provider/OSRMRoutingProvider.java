package com.routeiq.routing.provider;

import com.routeiq.routing.dto.RouteAlternativeDTO;
import com.routeiq.routing.dto.RouteRequestDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class OSRMRoutingProvider implements RoutingProvider {

    @Value("${routeiq.providers.routing.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public List<RouteAlternativeDTO> calculateRoutes(RouteRequestDTO request) {
        String coordinates = request.getOrigin().getLongitude() + "," + request.getOrigin().getLatitude() + ";" +
                             request.getDestination().getLongitude() + "," + request.getDestination().getLatitude();

        String profile = "driving";
        if ("WALKING".equalsIgnoreCase(request.getTravelMode())) profile = "foot";
        else if ("CYCLING".equalsIgnoreCase(request.getTravelMode())) profile = "bike";

        String url = UriComponentsBuilder.fromUriString(baseUrl)
                .path("/route/v1/" + profile + "/" + coordinates)
                .queryParam("alternatives", true)
                .queryParam("geometries", "geojson")
                .queryParam("overview", "full")
                .toUriString();

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url, HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Object>>() {});
            
            Map<String, Object> body = response.getBody();
            if (body == null || !"Ok".equals(body.get("code"))) {
                throw new RuntimeException("Routing failed or no route found.");
            }

            List<Map<String, Object>> routes = (List<Map<String, Object>>) body.get("routes");
            List<RouteAlternativeDTO> alternatives = new ArrayList<>();

            for (int i = 0; i < routes.size(); i++) {
                Map<String, Object> r = routes.get(i);
                RouteAlternativeDTO alt = new RouteAlternativeDTO();
                alt.setId(UUID.randomUUID().toString());
                alt.setName("Route " + (i + 1));
                
                double distanceMeters = ((Number) r.get("distance")).doubleValue();
                alt.setDistanceKm(distanceMeters / 1000.0);
                
                long durationSeconds = ((Number) r.get("duration")).longValue();
                
                // Adjust duration based on travel mode (since public OSRM primarily returns car speeds)
                if ("WALKING".equalsIgnoreCase(request.getTravelMode())) {
                    durationSeconds *= 10;
                } else if ("CYCLING".equalsIgnoreCase(request.getTravelMode())) {
                    durationSeconds *= 3;
                }
                
                alt.setBaseDurationSeconds(durationSeconds);
                alt.setAdjustedDurationSeconds(durationSeconds); // Default without traffic
                
                Map<String, Object> geometry = (Map<String, Object>) r.get("geometry");
                List<List<Number>> coords = (List<List<Number>>) geometry.get("coordinates");
                List<double[]> parsedCoords = new ArrayList<>();
                for (List<Number> c : coords) {
                    parsedCoords.add(new double[]{c.get(0).doubleValue(), c.get(1).doubleValue()});
                }
                alt.setGeometryCoordinates(parsedCoords);
                alternatives.add(alt);
            }

            return alternatives;
        } catch (Exception e) {
            throw new RuntimeException("Routing service error: " + e.getMessage());
        }
    }
}
