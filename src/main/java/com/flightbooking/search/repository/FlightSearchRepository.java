package com.flightbooking.search.repository;

import com.flightbooking.search.entity.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightSearchRepository extends JpaRepository<Flight, Long> {
    Page<Flight> findByDepartureAirportAndArrivalAirport(String departureAirport, String arrivalAirport, Pageable pageable);
}
