/*
 * AdministratorBookingRecordRepository.java
 *
 * Copyright (C) 2025 Rafael David Caro Medina
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.administrator.bookingRecord;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.bookings.Booking;
import acme.entities.bookings.BookingRecord;
import acme.entities.passengers.Passenger;

@Repository
public interface AdministratorBookingRecordRepository extends AbstractRepository {

	@Query("select b from Booking b where b.id = :id")
	Booking findBookingById(int id);

	@Query("select br from BookingRecord br where br.booking.id = :bookingId")
	Collection<BookingRecord> findBookingRecordsByBookingId(int bookingId);

	@Query("select br from BookingRecord br where br.id = :bookingRecordId")
	BookingRecord findBookingRecordById(int bookingRecordId);

	@Query("select p from Passenger p, BookingRecord br where p.id=br.passenger.id and br.id = :bookingRecordId")
	Collection<Passenger> findPassengersByBookingRecordId(int bookingRecordId);
}
