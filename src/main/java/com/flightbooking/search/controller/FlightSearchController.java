package com.flightbooking.search.controller;

import com.flightbooking.search.dto.FlightSearchResponse;
import com.flightbooking.search.service.FlightSearchService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/flights")
@Validated
@Slf4j
public class FlightSearchController {

    private final FlightSearchService flightSearchService;
    @GetMapping("/search")
    public Page<FlightSearchResponse> searchFlights(
            @RequestParam (required = true) @NotBlank String departureAirport,
            @RequestParam (required = true) @NotBlank String arrivalAirport,
            //@RequestParam (required = false)LocalDate date,
            @RequestParam (defaultValue = "0") int page,
            @RequestParam (defaultValue = "10") int size,
            @RequestParam(defaultValue = "price") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
            )
    {
        log.info("Searching flights from {} to {}", departureAirport, arrivalAirport);
        return flightSearchService.searchFlights(departureAirport, arrivalAirport, page, size, sortBy, sortDir);
    }

}
