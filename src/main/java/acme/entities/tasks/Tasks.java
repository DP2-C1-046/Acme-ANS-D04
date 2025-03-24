
package acme.entities.tasks;

import javax.persistence.Entity;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
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
	@ValidNumber(min = 0, max = 5000)
	@Automapped
	private Integer				estimatedDuration;

}
