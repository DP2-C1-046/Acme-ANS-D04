
package acme.entities.airlines;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;

public class Manager extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString(pattern = "^[A-Z]{2-3}\\d{6}$")
	@Automapped
	private String				identifierNumber;

	@Mandatory
	@ValidNumber(max = 50)  //TODO: Cambiar a derivada? Max < birth-currentYear
	@Automapped
	private Integer				yearsOfExperience;

	@Optional
	@ValidUrl
	@Automapped
	private String				linkToPicture;
}
