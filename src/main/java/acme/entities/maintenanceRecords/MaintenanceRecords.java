
package acme.entities.maintenanceRecords;

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
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.entities.aircrafts.Aircraft;
import acme.realms.Technicians;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MaintenanceRecords extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long			serialVersionUID	= 1L;

	// Relationships ----------------------------------------------------------
	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Technicians					technicians;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Aircraft					aircraft;

	// Attributes -------------------------------------------------------------
	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	private Date						moment;

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
