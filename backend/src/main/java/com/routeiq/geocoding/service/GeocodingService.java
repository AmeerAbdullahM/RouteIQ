package com.routeiq.geocoding.service;

import com.routeiq.geocoding.dto.LocationDTO;
import com.routeiq.geocoding.provider.GeocodingProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GeocodingService {
    @Autowired
    private GeocodingProvider geocodingProvider;

    public List<LocationDTO> search(String query) {
        if (query == null || query.trim().length() < 3) {
            throw new IllegalArgumentException("Search query must be at least 3 characters");
        }
        return geocodingProvider.search(query);
    }

    public LocationDTO reverse(double lat, double lon) {
        if (lat < -90 || lat > 90 || lon < -180 || lon > 180) {
            throw new IllegalArgumentException("Invalid coordinates");
        }
        return geocodingProvider.reverse(lat, lon);
    }

    public List<LocationDTO> autocomplete(String query) {
        if (query == null || query.trim().length() < 2) {
            return java.util.Collections.emptyList();
        }
        return geocodingProvider.autocomplete(query.trim());
    }
}
