
package acme.entities.maintenaceRecords;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Moment;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.entities.aircrafts.Aircraft;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MaintenanceRecords extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long			serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------
	@Mandatory
	@ManyToOne
	@Automapped
	private Aircraft					aircraft;

	@Mandatory
	@ValidMoment
	@Automapped
	private Moment						moment;

	@Mandatory
	@Valid
	@Automapped
	private MaintenanceRecordsStatus	status;

	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	private Date						inspectionDueDate;

	@Mandatory
	@ValidNumber
	@Automapped
	private Integer						estimatedCost;

	@Optional
	@ValidString
	@Automapped
	private String						notes;

}
