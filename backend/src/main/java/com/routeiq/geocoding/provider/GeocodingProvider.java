package com.routeiq.geocoding.provider;

import com.routeiq.geocoding.dto.LocationDTO;
import java.util.List;

public interface GeocodingProvider {
    List<LocationDTO> search(String query);
    LocationDTO reverse(double latitude, double longitude);
    List<LocationDTO> autocomplete(String query);
}
