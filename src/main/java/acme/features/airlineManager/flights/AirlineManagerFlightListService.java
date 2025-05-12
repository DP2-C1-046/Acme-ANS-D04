
package acme.features.airlineManager.flights;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flights.Flight;
import acme.entities.legs.Leg;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerFlightListService extends AbstractGuiService<AirlineManager, Flight> {

	@Autowired
	private AirlineManagerFlightRepository repository;

	// AbstractGuiService interfaced -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Flight> flights;
		int airlineManagerId;

		airlineManagerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		flights = this.repository.findAllFlightsByManagerId(airlineManagerId);

		super.getBuffer().addData(flights);

	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset;
		Collection<Leg> legs;
		legs = this.repository.findLegsByFlightId(flight.getId());
		dataset = super.unbindObject(flight, "tag", "needsSelfTransfer", "cost", "description", "draftMode");
		if (!legs.isEmpty()) {
			dataset.put("originCity", flight.getOriginCity());
			dataset.put("destinationCity", flight.getDestinationCity());
			dataset.put("scheduledDeparture", flight.getScheduledDeparture());
			dataset.put("scheduledArrival", flight.getScheduledArrival());
		} else {
			dataset.put("originCity", null);
			dataset.put("destinationCity", null);
			dataset.put("scheduledDeparture", null);
			dataset.put("scheduledArrival", null);
		}

		super.getResponse().addData(dataset);

	}

}
