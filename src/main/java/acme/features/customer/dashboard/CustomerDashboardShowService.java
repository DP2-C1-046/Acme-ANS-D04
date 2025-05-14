/*
 * CustomerDashboardShowService.java
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
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.TravelClass;
import acme.entities.flights.Flight;
import acme.forms.CustomerDashboard;
import acme.realms.Customer;

@GuiService
public class CustomerDashboardShowService extends AbstractGuiService<Customer, CustomerDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerDashboardRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		CustomerDashboard dashboard;
		int customerId;
		PageRequest page;

		Collection<Flight> lastFiveFlights;
		String lastFiveDestinations;

		Double moneySpentInBookingsLastYear;
		Integer bookingsInTravelClassECONOMY;
		Integer bookingsInTravelClassBUSINESS;

		// Data in the last 5 years
		Integer countOfBookingsL5Y;
		Double averageBookingsCostL5Y;
		Double minBookingsCostL5Y;
		Double maxBookingsCostL5Y;
		Double stdDesvBookingsCostL5Y;

		// Number of passengers in their bookings
		Integer countOfPassengers;
		Double averagePassengerNumber;
		Double minPassengerNumber;
		Double maxPassengerNumber;
		Double stdPassengerNumber;

		customerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		// The last five destinations
		page = PageRequest.of(0, 5);
		lastFiveFlights = this.repository.lastFiveDestinations(customerId, page);

		lastFiveDestinations = lastFiveFlights.stream().map(Flight::getDestinationCity).collect(Collectors.joining(", "));

		// Money spent in bookings last year
		moneySpentInBookingsLastYear = this.repository.moneySpentInBookingsLastYear(customerId);

		// Number of bookings by TravelClass
		bookingsInTravelClassECONOMY = this.repository.bookingsInTravelClass(customerId, TravelClass.ECONOMY);

		bookingsInTravelClassBUSINESS = this.repository.bookingsInTravelClass(customerId, TravelClass.BUSINESS);

		// Cost of their bookings in the Last Five Years => (L5Y)
		countOfBookingsL5Y = this.repository.countOfBookingsL5Y(customerId);
		averageBookingsCostL5Y = this.repository.averageBookingsCostL5Y(customerId);
		minBookingsCostL5Y = this.repository.minBookingsCostL5Y(customerId);
		maxBookingsCostL5Y = this.repository.maxBookingsCostL5Y(customerId);
		stdDesvBookingsCostL5Y = this.repository.stdDesvBookingsCostL5Y(customerId);

		// Number of passengers in their bookings
		countOfPassengers = this.repository.countOfPassengersByCustomerId(customerId);
		averagePassengerNumber = this.repository.averagePassengerNumber(customerId);
		minPassengerNumber = this.repository.minPassengerNumber(customerId);
		maxPassengerNumber = this.repository.maxPassengerNumber(customerId);

		dashboard = new CustomerDashboard();

		dashboard.setLastFiveDestinations(lastFiveDestinations);

		dashboard.setMoneySpentInBookingsLastYear(moneySpentInBookingsLastYear);

		dashboard.setBookingsInTravelClassECONOMY(bookingsInTravelClassECONOMY);
		dashboard.setBookingsInTravelClassBUSINESS(bookingsInTravelClassBUSINESS);

		dashboard.setCountOfBookingsL5Y(countOfBookingsL5Y);
		dashboard.setAverageBookingsCostL5Y(averageBookingsCostL5Y);
		dashboard.setMinBookingsCostL5Y(minBookingsCostL5Y);
		dashboard.setMaxBookingsCostL5Y(maxBookingsCostL5Y);
		dashboard.setStdDesvBookingsCostL5Y(stdDesvBookingsCostL5Y);

		dashboard.setCountOfPassengers(countOfPassengers);
		dashboard.setAveragePassengerNumber(averagePassengerNumber);
		dashboard.setMinPassengerNumber(minPassengerNumber);
		dashboard.setMaxPassengerNumber(maxPassengerNumber);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final CustomerDashboard dashboard) {
		Dataset dataset;

		dataset = super.unbindObject(dashboard, "lastFiveDestinations", "moneySpentInBookingsLastYear", // 
			"bookingsInTravelClassECONOMY", "bookingsInTravelClassBUSINESS", //
			"countOfBookingsL5Y", "averageBookingsCostL5Y", "minBookingsCostL5Y", "maxBookingsCostL5Y", "stdDesvBookingsCostL5Y", //
			"countOfPassengers", "averagePassengerNumber", "minPassengerNumber", "maxPassengerNumber");

		super.getResponse().addData(dataset);
	}

}
