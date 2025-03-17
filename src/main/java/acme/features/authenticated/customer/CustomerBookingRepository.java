/*
 * CustomerBookingRepository.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.authenticated.customer;

import java.util.Collection;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.bookings.Booking;
import acme.entities.bookings.TravelClass;
import acme.entities.passengers.Passenger;
import acme.realms.Customer;

@Repository
public interface CustomerBookingRepository extends AbstractRepository {

	@Query("select b from Booking b where b.id = :id")
	Booking findBookingById(int id);

	@Query("select b from Booking b where b.locatorCode = :locatorCode")
	Booking findBookingByLocatorCode(String locatorCode);

	@Query("select c from Customer c where c.id = :id")
	Customer findCustomerById(int id);

	@Query("select b from Booking b where b.customer.id = :customerId")
	Collection<Booking> findBookingsByCustomerId(int customerId);

	@Query("select b from Booking b where b.draftMode = false")
	Collection<Booking> findBookingsByAvailability();

	@Query("select p from Passenger p, BookingRecord br where p.id=br.passenger.id and br.booking.id = :bookingId")
	Collection<Passenger> findPassengersByBookingId(int bookingId);

	// Dashboard

	// The last five destinations ( no sé de momento cómo sacar los 5 últimos, no hay Top 5 en JPQL )
	@Query("select distinct l.arrivalAirport.name from Customer c, Booking b, Leg l where b.customer.id=c.id and b.flight.id=l.flight.id and c.id=:customerId order by l.scheduledArrival desc")
	Collection<String> lastFiveDestinations(int customerId);

	// 	The money spent in bookings during the last year ( 3 años de momento, no hay sample data para 1 año)
	@Query("select sum(b.price.amount) from Booking b where b.customer.id=:customerId and (year(now())-year(b.purchaseMoment))<=3")
	Double moneySpentInBookingsLastYear(int customerId);

	// Their number of bookings grouped by travel class.
	@Query("select b.travelClass, count(b) from Booking b where b.customer.id=:customerId group by b.travelClass")
	Map<TravelClass, Integer> bookingsByTravelClass(int customerId);

	// Count, average, minimum, maximum, and standard deviation of the cost of their bookings in the last five years
	@Query("select count(b.price.amount) from Booking b where b.customer.id=:customerId and ( year(now()) - year(b.purchaseMoment) ) <= 5")
	Integer countOfBookingsL5Y(int customerId);

	@Query("select avg(b.price.amount) from Booking b where b.customer.id=:customerId and ( year(now()) - year(b.purchaseMoment) ) <= 5")
	Double averageBookingsCostL5Y(int customerId);

	@Query("select min(b.price.amount) from Booking b where b.customer.id=:customerId and ( year(now()) - year(b.purchaseMoment) ) <= 5")
	Double minBookingsCostL5Y(int customerId);

	@Query("select max(b.price.amount) from Booking b where b.customer.id=:customerId and ( year(now()) - year(b.purchaseMoment) ) <= 5")
	Double maxBookingsCostL5Y(int customerId);

	// Hay que comentarlas porque da error al existir valores a 0
	//@Query("select stdev(b.price.amount) from Booking b where b.customer.id=:customerId and ( year(now()) - year(b.purchaseMoment) ) <= 5")
	//Double stdDesvBookingsCostL5Y(int customerId);

	// Number of passengers in their bookings
	@Query("select count(p) from Booking b, BookingRecord br, Passenger p where b.id=br.booking.id and br.passenger.id=p.id and b.customer.id=:customerId")
	Integer countOfPassengers(int customerId);

	@Query("select avg(select count(br.passenger.id) from BookingRecord br where br.booking.id=b.id group by br.booking.id) from Booking b where b.customer.id=customerId")
	Double averagePassengerNumber(int customerId);

	@Query("select min(select count(br.passenger.id) from BookingRecord br where br.booking.id=b.id group by br.booking.id) from Booking b where b.customer.id=customerId")
	Double minPassengerNumber(int customerId);

	@Query("select max(select count(br.passenger.id) from BookingRecord br where br.booking.id=b.id group by br.booking.id) from Booking b where b.customer.id=customerId")
	Double maxPassengerNumber(int customerId);

	// Hay que comentarlas porque da error al existir valores a 0
	//@Query("select stdev(select count(br.passenger.id) from BookingRecord br where br.booking.id=b.id group by br.booking.id) from Booking b where b.customer.id=customerId")
	//Double stdPassengerNumber(int customerId);

}
