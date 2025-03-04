
package acme.entities.flights;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(indexes = {
	@Index(columnList = "id")
})
public class Leg extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	// Preguntar: El IATA Code no deberia ser derivada???
	@Mandatory
	@ValidString(pattern = "^[A-Z]{3}\\d{4}$")
	@Column(unique = true)
	private String				flightNumber;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				scheduledDeparture;

	@Mandatory
	@ValidMoment //poner past = true?? aunque no tendría sentido
	@Temporal(TemporalType.TIMESTAMP)
	private Date				scheduledArrival;

	@Mandatory
	@ValidNumber(min = 0, integer = 2, fraction = 0)
	@Automapped
	private Double				duration;

	@Mandatory
	@Valid
	@Automapped
	private LegStatus			legStatus;

	// Relationships ----------------------------------------------------------

	// TODO: Descomentar cuando tengamos Airport
	//	@Mandatory
	//	@Valid
	//	@ManyToOne(optional=false)
	//	private Airport departureAirport;

	//	@Mandatory
	//	@Valid
	//	@ManyToOne(optional=false)
	//	private Airport arrivalAirport;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Flight				flight;

}
