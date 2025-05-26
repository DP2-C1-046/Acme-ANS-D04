
package acme.entities.flights;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
import acme.entities.airlines.Airline;
import acme.entities.airports.Airport;
import acme.entities.legs.Leg;
import acme.entities.legs.LegRepository;
import acme.realms.AirlineManager;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
//@ValidFlight
public class Flight extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString(min = 1, max = 50)
	@Automapped
	private String				tag;

	// ¿Nombre correcto? ¿Deberia ser indication?
	@Mandatory
	// - Boolean no necesita valid
	@Automapped
	private boolean				needsSelfTransfer;

	@Mandatory
	@ValidMoney
	@Automapped
	private Money				cost;

	@Optional
	@ValidString(min = 0, max = 255)
	@Automapped
	private String				description;

	private boolean				draftMode;

	// Derived attributes -----------------------------------------------------


	@Transient
	public Date getScheduledDeparture() {
		LegRepository repository = SpringHelper.getBean(LegRepository.class);
		List<Date> wrapper = repository.findFirstScheduledDeparture(this.getId());

		return wrapper != null && !wrapper.isEmpty() ? wrapper.get(0) : null;
	}

	@Transient
	public Date getScheduledArrival() {
		LegRepository repository = SpringHelper.getBean(LegRepository.class);
		List<Date> wrapper = repository.findLastScheduledArrival(this.getId());

		return wrapper != null && !wrapper.isEmpty() ? wrapper.get(0) : null;
	}

	@Transient
	public String getOriginCity() {
		LegRepository repository = SpringHelper.getBean(LegRepository.class);
		List<Airport> wrapper = repository.findFirstAirport(this.getId());
		return wrapper != null && !wrapper.isEmpty() ? wrapper.get(0).getCity() : null;
	}

	@Transient
	public String getDestinationCity() {
		LegRepository repository = SpringHelper.getBean(LegRepository.class);
		List<Airport> wrapper = repository.findLastAirport(this.getId());
		return wrapper != null && !wrapper.isEmpty() ? wrapper.get(0).getCity() : null;
	}

	@Transient
	public Integer getNumberOfLayovers() {
		LegRepository repository = SpringHelper.getBean(LegRepository.class);
		Integer layovers = repository.numberOfLayovers(this.getId());

		return layovers != null ? layovers : 0;
	}

	@Transient
	public Integer getLayovers() {
		Integer res;
		LegRepository repository;
		List<Leg> wrapper;

		repository = SpringHelper.getBean(LegRepository.class);
		wrapper = repository.findLegsByFlightId(this.getId());
		res = wrapper.size();

		return res;
	}

	@Transient
	public String getDisplayTag() {

		Date scheduledDeparture;
		Date scheduledArrival;

		scheduledDeparture = this.getScheduledDeparture();
		scheduledArrival = this.getScheduledArrival();

		int departureHour = scheduledDeparture != null ? this.getScheduledDeparture().getHours() : 0;
		int departureMinute = scheduledDeparture != null ? this.getScheduledDeparture().getMinutes() : 0;
		int arrivalHour = scheduledArrival != null ? this.getScheduledArrival().getHours() : 0;
		int arrivalMinute = scheduledArrival != null ? this.getScheduledArrival().getMinutes() : 0;

		return String.format("%02d:%02d %s → %02d:%02d %s", departureHour, departureMinute, this.getOriginCity(), arrivalHour, arrivalMinute, this.getDestinationCity());
	}

	// Relationships ----------------------------------------------------------


	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private AirlineManager	airlineManager;

	@Mandatory
	@ManyToOne(optional = false)
	@Valid
	private Airline			airline;

}
