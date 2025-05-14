/*
 * CustomerBookingRecordListService.java
 *
 * Copyright (C) 2025 Rafael David Caro Medina
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.customer.passenger;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.passengers.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerListService extends AbstractGuiService<Customer, Passenger> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerPassengerRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {

		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Passenger> passengers;
		int customerId;

		customerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		if (super.getRequest().getData().isEmpty())
			// Retrieve only passenger in his/her bookings
			passengers = this.repository.findPassengersByCustomerId(customerId);
		else
			// Retrieve ALL the customer passengers
			passengers = this.repository.findAllPassengersByCustomerId(customerId);

		super.getBuffer().addData(passengers);
	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset;

		dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "draftMode");
		super.addPayload(dataset, passenger, "dateOfBirth", "specialNeeds");

		super.getResponse().addData(dataset);
	}

	@Override
	public void unbind(final Collection<Passenger> passengers) {

		int customerId;
		Customer customer;

		final boolean showCreate;

		customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		customer = this.repository.findCustomerById(customerId);

		showCreate = super.getRequest().getPrincipal().hasRealm(customer);

		super.getResponse().addGlobal("masterId", customerId);
		super.getResponse().addGlobal("showCreate", showCreate);
	}

}
