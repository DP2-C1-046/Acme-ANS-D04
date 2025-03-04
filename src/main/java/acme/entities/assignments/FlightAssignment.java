
package acme.entities.assignments;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.realms.FlightCrewMember;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class FlightAssignment extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	//	Attributes -----------------------------------------------------
	//	 flight assignment represents the allocation of a flight crew member 
	//	 to a specific leg of a flight.  Each assignment specifies the flight crew's duty 
	//	 in that leg ("PILOT", "CO-PILOT", "LEAD ATTENDANT", "CABIN ATTENDANT"), 
	//	 the moment of the last update (in the past), 
	//	 the current status of the assignment ("CONFIRMED", "PENDING", or "CANCELLED"), 
	//	 and some remarks (up to 255 characters), if necessary.
	@Mandatory
	@Automapped
	private FlightCrewDuty		flightCrewDuty;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				lastUpdate;

	@Mandatory
	@Automapped
	private AssignmentStatus	assignmentStatus;

	@Optional
	@ValidString
	@Automapped
	private String				remarks;

	//Relationships ----------------------------------------------------
	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private FlightCrewMember	flightCrewMember;

}
