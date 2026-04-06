package com.flightbooking.search.service;

import com.flightbooking.search.dto.FlightSearchResponse;
import com.flightbooking.search.entity.Flight;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

public interface FlightSearchService {

    Page<FlightSearchResponse> searchFlights(String departureAirport, String arrivalAirport, int page, int size);
}
