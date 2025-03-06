
package acme.entities.claims;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Moment;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(indexes = {
	@Index(columnList = "id")
})
public class TrackingLog extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Claim				claim;

	@Mandatory
	@ValidMoment
	@Automapped
	private Moment				lastUpdate;

	@Mandatory
	@ValidString(min = 1, max = 50)
	@Automapped
	private String				stepUndergoing;

	@Mandatory
	@ValidNumber(min = 0, max = 100, integer = 3, fraction = 2)
	@Automapped
	private Double				resolutionPercentage;

	//¿No es derivada del claim o al revés?

	@Mandatory
	@Automapped
	private boolean				accepted;

	@Optional
	@ValidString
	@Automapped
	private String				resolutionReasonOrCompensation;

}
