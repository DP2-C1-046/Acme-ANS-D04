
package acme.entities.aircrafts;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.entities.airlines.Airline;
import lombok.Getter;
import lombok.Setter;

// An aircraft is a vehicle designed for air travel that belongs to an airline
// and is used to transport passengers between cities or countries. The system
// must store the following data about them: a model (up to 50 characters),
// a registration number (unique, up to 50 characters), its capacity as a number of passengers,
// its cargo weight (between 2K and 50K kgs), a status, which reports on whether
// the aircraft is in active service or under maintenance, and optional details (up to 255 characters).

@Entity
@Getter
@Setter
@Table(indexes = {
	@Index(columnList = "id")
})
public class Aircraft extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString(min = 1, max = 50)
	@Automapped
	private String				model;

	@Mandatory
	@ValidString(min = 1, max = 50)
	@Column(unique = true)
	private String				registrationNumber;

	@Mandatory
	@ValidNumber(min = 1)
	@Automapped
	private Integer				capacity;

	@Mandatory
	@ValidNumber(min = 2000, max = 50000)
	@Automapped
	private Integer				cargoWeight;

	@Mandatory
	@Automapped
	private AircraftStatus		status;

	@Optional
	@ValidString(min = 0, max = 255)
	@Automapped
	private String				optionalDetails;

	// Relationships ----------------------------------------------------------

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airline				airline;

}
