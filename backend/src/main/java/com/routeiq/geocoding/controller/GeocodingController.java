package com.routeiq.geocoding.controller;

import com.routeiq.geocoding.dto.LocationDTO;
import com.routeiq.geocoding.service.GeocodingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/geocoding")
public class GeocodingController {

    @Autowired
    private GeocodingService geocodingService;

    @GetMapping("/search")
    public ResponseEntity<List<LocationDTO>> search(@RequestParam("q") String query) {
        return ResponseEntity.ok(geocodingService.search(query));
    }

    @GetMapping("/autocomplete")
    public ResponseEntity<List<LocationDTO>> autocomplete(@RequestParam("q") String query) {
        return ResponseEntity.ok(geocodingService.autocomplete(query));
    }

    @GetMapping("/reverse")
    public ResponseEntity<LocationDTO> reverse(@RequestParam("lat") double lat, @RequestParam("lon") double lon) {
        return ResponseEntity.ok(geocodingService.reverse(lat, lon));
    }
}
