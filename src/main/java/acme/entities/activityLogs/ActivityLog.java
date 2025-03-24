
package acme.entities.activityLogs;

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
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidActivityLog;
import acme.entities.assignments.FlightAssignment;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@ValidActivityLog
@Table(indexes = {
	@Index(columnList = "id")
})
public class ActivityLog extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	//Attributes --------------------------------------

	//	They are logged by any of the flight crew members assigned to the corresponding leg and after the leg has taken place. 

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				registrationMoment;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				typeOfIndicent;

	@Mandatory
	@ValidString
	@Automapped
	private String				description;

	@Mandatory
	@ValidNumber(min = 0, max = 10)
	@Automapped
	private Integer				severityLevel;

	@Mandatory
	@Valid
	@Automapped
	private Boolean				draftMode;

	//Relationships ---------------------------------
	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private FlightAssignment	flightAssignment;

}
