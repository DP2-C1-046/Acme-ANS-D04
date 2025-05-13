/*
 * AdministratorBookingRecordListService.java
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

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.Booking;
import acme.entities.bookings.BookingRecord;

@GuiService
public class AdministratorBookingRecordListService extends AbstractGuiService<Administrator, BookingRecord> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorBookingRecordRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Booking booking;

		masterId = super.getRequest().getData("masterId", int.class);
		booking = this.repository.findBookingById(masterId);
		//status = booking != null && (!booking.isDraftMode() || super.getRequest().getPrincipal().hasRealm(booking.getCustomer()));
		status = booking != null;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<BookingRecord> bookingRecords;
		int masterId;

		masterId = super.getRequest().getData("masterId", int.class);
		bookingRecords = this.repository.findBookingRecordsByBookingId(masterId);

		super.getBuffer().addData(bookingRecords);
	}

	@Override
	public void unbind(final BookingRecord bookingRecord) {
		Dataset dataset;

		dataset = super.unbindObject(bookingRecord, "passenger.fullName", "passenger.email", "passenger.passportNumber");
		super.addPayload(dataset, bookingRecord, "passenger.dateOfBirth", "passenger.specialNeeds");

		super.getResponse().addData(dataset);
	}

	@Override
	public void unbind(final Collection<BookingRecord> bookingRecord) {
		int masterId;
		Booking booking;

		masterId = super.getRequest().getData("masterId", int.class);
		booking = this.repository.findBookingById(masterId);

		super.getResponse().addGlobal("masterId", masterId);
	}

}
