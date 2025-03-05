
package acme.entities.tasks;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.entities.maintenaceRecords.MaintenanceRecords;
import acme.entities.technicians.Technicians;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Tasks extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------
	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Technicians			technician;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private MaintenanceRecords	maintenanceRecords;

	@Mandatory
	@Automapped
	private TasksType			tasksType;

	@Mandatory
	@ValidString
	@Automapped
	private String				description;

	@Mandatory
	@ValidNumber(min = 0, max = 10, integer = 2, fraction = 2)
	@Automapped
	private Integer				priority;

	@Mandatory
	@ValidNumber
	@Automapped
	private Integer				estimatedDuration;

}
