package com.routeiq.geocoding.provider;

import com.routeiq.geocoding.dto.LocationDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class NominatimGeocodingProvider implements GeocodingProvider {
    
    @Value("${routeiq.providers.geocoding.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String USER_AGENT = "RouteIQ-Dev/1.0";

    @Override
    public List<LocationDTO> search(String query) {
        String url = UriComponentsBuilder.fromUriString(baseUrl)
                .path("/search")
                .queryParam("q", query)
                .queryParam("format", "json")
                .queryParam("limit", 5)
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", USER_AGENT);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, new ParameterizedTypeReference<List<Map<String, Object>>>() {});
            
            List<LocationDTO> results = new ArrayList<>();
            if (response.getBody() != null) {
                for (Map<String, Object> item : response.getBody()) {
                    LocationDTO loc = new LocationDTO();
                    loc.setName((String) item.get("display_name"));
                    loc.setLatitude(Double.parseDouble((String) item.get("lat")));
                    loc.setLongitude(Double.parseDouble((String) item.get("lon")));
                    results.add(loc);
                }
            }
            return results;
        } catch (Exception e) {
            throw new RuntimeException("Geocoding failed: " + e.getMessage());
        }
    }

    @Override
    public LocationDTO reverse(double latitude, double longitude) {
        String url = UriComponentsBuilder.fromUriString(baseUrl)
                .path("/reverse")
                .queryParam("lat", latitude)
                .queryParam("lon", longitude)
                .queryParam("format", "json")
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", USER_AGENT);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, new ParameterizedTypeReference<Map<String, Object>>() {});
            
            Map<String, Object> body = response.getBody();
            if (body != null && body.containsKey("display_name")) {
                LocationDTO loc = new LocationDTO();
                loc.setName((String) body.get("display_name"));
                loc.setLatitude(Double.parseDouble((String) body.get("lat")));
                loc.setLongitude(Double.parseDouble((String) body.get("lon")));
                return loc;
            }
            throw new RuntimeException("Location not found");
        } catch (Exception e) {
            throw new RuntimeException("Reverse geocoding failed: " + e.getMessage());
        }
    }

    @Override
    public List<LocationDTO> autocomplete(String query) {
        if (query == null || query.trim().isEmpty()) {
            return Collections.emptyList();
        }

        // 1. Try Photon autocomplete first (fast, typo-tolerant OSM-based prefix search)
        try {
            String photonUrl = "https://photon.komoot.io/api/?q=" + UriUtils.encode(query.trim(), StandardCharsets.UTF_8) + "&limit=6";
            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", USER_AGENT);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    photonUrl, HttpMethod.GET, entity, new ParameterizedTypeReference<Map<String, Object>>() {});

            Map<String, Object> body = response.getBody();
            if (body != null && body.containsKey("features")) {
                List<Map<String, Object>> features = (List<Map<String, Object>>) body.get("features");
                List<LocationDTO> results = new ArrayList<>();
                for (Map<String, Object> feature : features) {
                    Map<String, Object> props = (Map<String, Object>) feature.get("properties");
                    Map<String, Object> geom = (Map<String, Object>) feature.get("geometry");
                    if (props != null && geom != null) {
                        String name = (String) props.get("name");
                        String city = (String) props.get("city");
                        String state = (String) props.get("state");
                        String country = (String) props.get("country");

                        String mainTitle = name != null ? name : (city != null ? city : country);
                        if (mainTitle == null) continue;

                        StringBuilder detailsBuilder = new StringBuilder();
                        if (city != null && !city.equalsIgnoreCase(mainTitle)) {
                            detailsBuilder.append(city);
                        }
                        if (state != null && !state.equalsIgnoreCase(mainTitle)) {
                            if (detailsBuilder.length() > 0) detailsBuilder.append(", ");
                            detailsBuilder.append(state);
                        }
                        if (country != null && !country.equalsIgnoreCase(mainTitle)) {
                            if (detailsBuilder.length() > 0) detailsBuilder.append(", ");
                            detailsBuilder.append(country);
                        }
                        String details = detailsBuilder.toString();
                        String fullName = details.isEmpty() ? mainTitle : mainTitle + ", " + details;

                        List<Double> coords = (List<Double>) geom.get("coordinates");
                        if (coords != null && coords.size() >= 2) {
                            double lon = ((Number) coords.get(0)).doubleValue();
                            double lat = ((Number) coords.get(1)).doubleValue();
                            results.add(new LocationDTO(fullName, lat, lon, mainTitle, details));
                        }
                    }
                }
                if (!results.isEmpty()) {
                    return results;
                }
            }
        } catch (Exception ignored) {
            // Fallback to Nominatim search
        }

        // 2. Fallback to Nominatim search
        try {
            List<LocationDTO> fallback = search(query);
            for (LocationDTO loc : fallback) {
                if (loc.getDisplayName() == null || loc.getDisplayName().isEmpty()) {
                    String[] parts = loc.getName().split(",", 2);
                    loc.setDisplayName(parts[0].trim());
                    if (parts.length > 1) {
                        loc.setDetails(parts[1].trim());
                    }
                }
            }
            return fallback;
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
