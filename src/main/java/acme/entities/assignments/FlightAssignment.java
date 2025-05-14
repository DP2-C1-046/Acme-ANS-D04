
package acme.entities.assignments;

import java.util.Date;

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
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.entities.legs.Leg;
import acme.realms.FlightCrewMember;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(indexes = {
	@Index(columnList = "id")
})
public class FlightAssignment extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

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

	@Mandatory
	@Valid
	@Automapped
	private Boolean				draftMode;

	//Relationships ----------------------------------------------------
	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private FlightCrewMember	flightCrewMember;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Leg					leg;


	@Override
	public String toString() {
		return String.format(" %s - %s (%s)", this.leg.getFlight().getAirline().getName(), this.leg.getDepartureAirport().getName(), this.flightCrewDuty);
	}

}
