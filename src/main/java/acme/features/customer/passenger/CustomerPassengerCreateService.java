/*
 * CustomerPassengerCreateService.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.customer.passenger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.passengers.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerCreateService extends AbstractGuiService<Customer, Passenger> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerPassengerRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		// Se autoriza siempre, crea el pasajero asociado al Customer logado
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Passenger passenger;

		passenger = new Passenger();
		passenger.setDraftMode(true);

		super.getBuffer().addData(passenger);
	}

	@Override
	public void bind(final Passenger passenger) {

		Customer customer;

		customer = (Customer) super.getRequest().getPrincipal().getActiveRealm();

		super.bindObject(passenger, "fullName", "email", "passportNumber", //
			"dateOfBirth", "specialNeeds");
		passenger.setCustomer(customer);
	}

	@Override
	public void validate(final Passenger passenger) {

		// Custom validation 1: validate passportNumber must be unique in DB
		boolean uniquePassportNumber;

		Passenger existingPassenger;

		existingPassenger = this.repository.findPassengerByPassportNumber(passenger.getPassportNumber());
		uniquePassportNumber = existingPassenger == null || existingPassenger.equals(passenger);

		super.state(uniquePassportNumber, "passportNumber", "acme.validation.passenger.duplicated-passportNumber.message");

	}

	@Override
	public void perform(final Passenger passenger) {
		this.repository.save(passenger);
	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset;

		dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", //
			"dateOfBirth", "specialNeeds", "draftMode");

		super.getResponse().addData(dataset);
	}

}
