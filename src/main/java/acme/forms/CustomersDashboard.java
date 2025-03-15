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
import java.util.Map;

import acme.client.components.basis.AbstractForm;
import acme.entities.bookings.TravelClass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomersDashboard extends AbstractForm {

	// Serialisation version --------------------------------------------------

	private static final long			serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	private List<String>				lastFiveDestinations;
	private Double						moneySpentInBookingsLastYear;

	// Their number of bookings grouped by travel class 
	private Map<TravelClass, Integer>	bookingsByTravelClass;

	// Cost of their bookings in the Last Five Years => (L5Y)
	private Integer						countOfBookingsL5Y;
	private Double						averageBookingsCostL5Y;
	private Double						minBookingsCostL5Y;
	private Double						maxBookingsCostL5Y;
	private Double						stdDesvBookingsCostL5Y;

	// Number of passengers in their bookings
	private Integer						countOfPassengers;
	private Double						averagePassengerNumber;
	private Double						minPassengerNumber;
	private Double						maxPassengerNumber;
	private Double						stdPassengerNumber;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
