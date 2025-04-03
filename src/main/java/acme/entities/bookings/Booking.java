/*
 * Booking.java
 *
 * Copyright (C) 2012-2025 G3-C1.046.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.entities.bookings;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.entities.flights.Flight;
import acme.realms.Customer;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(indexes = {
	@Index(columnList = "id")
})
public class Booking extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString(pattern = "^[A-Z0-9]{6,8}$")
	@Column(unique = true)
	private String				locatorCode;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				purchaseMoment;

	@Mandatory
	@Valid
	@Automapped
	private TravelClass			travelClass;

	@Mandatory
	@ValidMoney(min = 0, max = 1000000)
	@Automapped
	private Money				price;

	// Interpreto como 4 últimos dígitos de la tarjeta de crédito, consultar
	@Optional
	@ValidString(pattern = "^[0-9]{4}$")
	@Automapped
	private String				lastCardNibble;

	@Mandatory
	// @Valid por defecto
	@Automapped
	private boolean				draftMode;

	// Derived attributes -----------------------------------------------------


	@Transient
	public boolean isPublished() {
		boolean result;

		result = !this.draftMode;

		return result;
	}

	// Relationships ----------------------------------------------------------


	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Customer	customer;

	@Mandatory
	@Valid
	@ManyToOne
	private Flight		flight;

}
