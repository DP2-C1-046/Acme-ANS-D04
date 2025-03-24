
package acme.entities.tasks;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.realms.Technicians;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Tasks extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Relationships ----------------------------------------------------------

	@Mandatory
	@ManyToOne(optional = false)
	@Valid
	private Technicians			technicians;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Enumerated(EnumType.STRING)
	@Automapped
	private TasksType			tasksType;

	@Mandatory
	@ValidString(min = 0, max = 255)
	@Automapped
	private String				description;

	@Mandatory
	@ValidNumber(min = 0, max = 10)
	@Automapped
	private Integer				priority;

	@Mandatory
	@ValidNumber(min = 0, max = 5000)
	@Automapped
	private Integer				estimatedDuration;

}
