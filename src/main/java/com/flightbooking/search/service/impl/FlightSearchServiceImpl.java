package com.flightbooking.search.service.impl;

import com.flightbooking.search.dto.FlightSearchResponse;
import com.flightbooking.search.entity.Flight;
import com.flightbooking.search.repository.FlightSearchRepository;
import com.flightbooking.search.service.FlightSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class FlightSearchServiceImpl implements FlightSearchService{

    private final FlightSearchRepository flightSearchRepository;

    public Page<FlightSearchResponse> searchFlights(String departureAirport, String arrivalAirport, int page, int size){
        //1. validation for input parameters can be added here

        if(departureAirport.equalsIgnoreCase(arrivalAirport)){
            throw new IllegalArgumentException("Departure airport and arrival airport cannot be same");
        }

        //2. Pageable object creation
        Pageable pageable = PageRequest.of(page, size);

        //3. fetching data from repository
        Page<Flight> flights = flightSearchRepository.findByDepartureAirportAndArrivalAirport(departureAirport, arrivalAirport, pageable);

        //4. mapping Flight entity to FlightSearchResponse DTO

        return flights.map(flight -> FlightSearchResponse.builder()
                .flightNumber(flight.getFlightNumber())
                .airline(flight.getAirline())
                .departureAirport(flight.getDepartureAirport())
                .arrivalAirport(flight.getArrivalAirport())
                .departureTime(flight.getDepartureTime())
                .arrivalTime(flight.getArrivalTime())
                .price(flight.getPrice())
                .build()
        );
    }
}
