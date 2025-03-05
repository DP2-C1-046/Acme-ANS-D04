
package acme.entities.flights;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Transient;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(indexes = {
	@Index(columnList = "id")
})
public class Flight extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString(min = 1, max = 50)
	@Automapped
	private String				tag;

	@Mandatory
	// - Boolean no necesita valid
	@Automapped
	private boolean				needsSelfTransfer;

	@Mandatory
	@ValidMoney
	@Automapped
	private Money				cost;

	@Optional
	@ValidString(min = 1, max = 255)
	@Automapped
	private String				description;

	// Derived attributes -----------------------------------------------------


	@Transient
	public Date getScheduledDeparture() {
		return null;
	}

	@Transient
	public Date getScheduledArrival() {
		return null;
	}

	@Transient
	public String getOriginCity() {
		return null;
	}

	@Transient
	public String getDestinationCity() {
		return null;
	}

	@Transient
	public Integer getNumberOfLayovers() {
		return null;
	}

	// Relationships ----------------------------------------------------------

	//	// One to One AirlineManager?? Si se descomenta, falla
	//	@Mandatory
	//	@Valid
	//	@OneToOne(optional = false)
	//	private AirlineManager airlineManager;

}
