/*
 * CustomerDashboardRepository.java
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

package acme.features.customer.dashboard;

import java.util.Collection;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.bookings.TravelClass;
import acme.entities.flights.Flight;

@Repository
public interface CustomerDashboardRepository extends AbstractRepository {

	// Dashboard

	// The last five destinations 
	//@Query("select b.flight from Customer c, Booking b, Leg l where b.customer.id=c.id and b.flight.id=l.flight.id and c.id=:customerId order by l.scheduledArrival desc")
	//@Query("select b.flight from Customer c, Booking b where b.customer.id=c.id and c.id=:customerId order by b.purchaseMoment desc")
	@Query("select b.flight from Booking b where b.customer.id=:customerId and b.draftMode=false order by b.purchaseMoment desc")
	Collection<Flight> lastFiveDestinations(int customerId, PageRequest pageResuest);

	// 	The money spent in bookings during the last year ( 3 años de momento, no hay sample data para 1 año)
	@Query("select sum(b.price.amount) from Booking b where b.customer.id=:customerId and b.draftMode=false and function('datediff', now(), b.purchaseMoment)<=1095")
	Double moneySpentInBookingsLastYear(int customerId);

	// Their number of bookings by travel class.
	@Query("select count(b) from Booking b where b.customer.id=:customerId and b.draftMode=false and b.travelClass=:travelClass")
	Integer bookingsInTravelClass(int customerId, TravelClass travelClass);

	//@Query("select b.travelClass, count(b) from Booking b where b.customer.id=:customerId group by b.travelClass")
	//Map<TravelClass, Integer> bookingsByTravelClass(int customerId);

	// Count, average, minimum, maximum, and standard deviation of the cost of their bookings in the last five years
	@Query("select count(b.price.amount) from Booking b where b.customer.id=:customerId and b.draftMode=false and function('datediff', now(), b.purchaseMoment)<=1825")
	Integer countOfBookingsL5Y(int customerId);

	@Query("select avg(b.price.amount) from Booking b where b.customer.id=:customerId and b.draftMode=false and function('datediff', now(), b.purchaseMoment)<=1825")
	Double averageBookingsCostL5Y(int customerId);

	@Query("select min(b.price.amount) from Booking b where b.customer.id=:customerId and b.draftMode=false and function('datediff', now(), b.purchaseMoment)<=1825")
	Double minBookingsCostL5Y(int customerId);

	@Query("select max(b.price.amount) from Booking b where b.customer.id=:customerId and b.draftMode=false and function('datediff', now(), b.purchaseMoment)<=1825")
	Double maxBookingsCostL5Y(int customerId);

	@Query("select stddev(b.price.amount) from Booking b where b.customer.id=:customerId and b.draftMode=false and function('datediff', now(), b.purchaseMoment)<=1825")
	Double stdDesvBookingsCostL5Y(int customerId);

	// Number of passengers in their bookings
	@Query("select count(p) from Booking b, BookingRecord br, Passenger p where b.id=br.booking.id and br.passenger.id=p.id and br.booking.draftMode=false and b.customer.id=:customerId")
	Integer countOfPassengersByCustomerId(int customerId);

	@Query("select avg(select count(br.passenger.id) from BookingRecord br where br.booking.id=b.id group by br.booking.id) from Booking b where b.draftMode=false and b.customer.id=:customerId")
	Double averagePassengerNumber(int customerId);

	@Query("select min(select count(br.passenger.id) from BookingRecord br where br.booking.id=b.id group by br.booking.id) from Booking b where b.draftMode=false and b.customer.id=:customerId")
	Double minPassengerNumber(int customerId);

	@Query("select max(select count(br.passenger.id) from BookingRecord br where br.booking.id=b.id group by br.booking.id) from Booking b where b.draftMode=false and b.customer.id=:customerId")
	Double maxPassengerNumber(int customerId);

	// Hay que comentarlas porque da error ¿al existir valores a 0?, el "group by" elimina las reservas con 0 pasajeros y aún así
	// da el error
	//@Query("select stddev(select count(br.passenger.id) from BookingRecord br where br.booking.id=b.id group by br.booking.id) from Booking b where b.customer.id=customerId")
	//Double stdPassengerNumber(int customerId);

}
