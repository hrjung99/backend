package swyp.swyp6_team7.location.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import swyp.swyp6_team7.location.service.LocationAutocompleteService;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/autocomplete")
public class LocationController {
    private final LocationAutocompleteService locationAutocompleteService;

    public LocationController(LocationAutocompleteService locationAutocompleteService) {
        this.locationAutocompleteService = locationAutocompleteService;
    }

    @GetMapping
    public ResponseEntity<Map<String, List<String>>> getAutocompleteSuggestions(@RequestParam(value = "location", required = false) String location) {
        if (location == null || location.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("suggestions", Collections.emptyList()));
        }

        List<String> suggestions = locationAutocompleteService.getAutocompleteSuggestions(location);

        Map<String, List<String>> response = new HashMap<>();
        response.put("suggestions", suggestions);
        return ResponseEntity.ok(response);
    }
}
