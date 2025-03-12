/*
 * CustomersDashboard.java
 *
 * Copyright (C) 2025 Rafael David Caro Medina
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.forms;

import java.util.List;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomersDashboard extends AbstractForm {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	List<String>				lastFiveDestinations;
	Double						moneySpentInBookingsLastYear;

	Integer						economyBookingsNumber;
	Integer						businessBookingsNumber;

	// Cost of their bookings in the Last Five Years => (L5Y)
	Integer						countOfBookingsL5Y;
	Double						averageBookingsCostL5Y;
	Double						minBookingsCostL5Y;
	Double						maxBookingsCostL5Y;
	Double						stdDesvBookingsCostL5Y;

	// Number of passengers in their bookings
	Integer						countOfPassengers;
	Double						averagePassengerNumber;
	Double						minPassengerNumber;
	Double						maxPassengerNumber;
	Double						stdPassengerNumber;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
