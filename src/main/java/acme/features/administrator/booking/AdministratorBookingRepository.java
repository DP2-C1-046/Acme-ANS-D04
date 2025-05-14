/*
 * AdministratorBookingRepository.java
 *
 * Copyright (C) 2025 Rafael David Caro Medina
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 * 
 */

package acme.features.administrator.booking;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.bookings.Booking;
import acme.entities.flights.Flight;

@Repository
public interface AdministratorBookingRepository extends AbstractRepository {

	@Query("select b from Booking b where b.draftMode = false")
	Collection<Booking> findBookings();

	@Query("select b from Booking b where b.id = :id")
	Booking findBookingById(int id);

	@Query("select f from Flight f")
	Collection<Flight> findAllFlights();

	// Ahora mismo no se puede ejecutar, no existe draftMode para Flight en esta rama
	//@Query("select f from Flight f where f.draftMode=false and f.getScheduledDeparture > :currentMoment")
	//Collection<Flight> findAvailablesFlights(Date currentMoment);

}
