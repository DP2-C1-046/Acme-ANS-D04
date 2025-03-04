
package acme.entities.flights;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Transient;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
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
	private String				tag;

	private boolean				needsSelfTransfer;

	private Money				cost;

	private String				description;

	// Derived attributes -----------------------------------------------------


	@Transient
		public getScheduledDeparture() {
			return null;
		}

	@Transient
		public getScheduledArrival() {
			return null;
		}

	@Transient
		public getOriginCity() {
			return null;
		}

	@Transient
		public getDestinationCity() {
			return null;
		}

	@Transient 
	public getNumberOfLayovers() {
		return null;
	}

	// Relationships ----------------------------------------------------------

	// One to One AirlineManager??
}
