
package acme.features.airlineManager.legs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flights.Flight;
import acme.entities.legs.Leg;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerLegListService extends AbstractGuiService<AirlineManager, Leg> {

	@Autowired
	private AirlineManagerLegRepository repository;


	@Override
	public void authorise() {
		boolean status = true; // Por defecto permitimos listar todos los tramos
		int airlineManagerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Integer flightId = null;
		boolean showCreate = false;

		// Si hay un flightId, validamos que el airlineManager tenga acceso a ese vuelo
		if (!super.getRequest().getData().isEmpty() && super.getRequest().hasData("flightId")) {
			flightId = super.getRequest().getData("flightId", Integer.class);
			if (flightId != null) {
				Flight flight = this.repository.findFlightById(flightId);
				if (flight != null) {
					AirlineManager airlineManager = flight.getAirlineManager();
					// Solo autorizamos si el vuelo pertenece al airlineManager actual
					status = super.getRequest().getPrincipal().hasRealm(airlineManager) && airlineManagerId == airlineManager.getId();
					// Permitir crear solo si el vuelo está en modo borrador
					showCreate = flight.isDraftMode();
				} else
					status = false; // El vuelo no existe
			}
		}

		super.getResponse().addGlobal("showCreate", showCreate);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Leg> legs;

		// Si hay ID de vuelo, cargamos los tramos de ese vuelo
		if (super.getRequest().hasData("flightId")) {
			int flightId = super.getRequest().getData("flightId", int.class);
			legs = this.repository.findLegsByFlightId(flightId);
			super.getResponse().addGlobal("flightId", flightId);
		} else {
			// Si no hay ID de vuelo, cargamos todos los tramos del airlineManager
			int airlineManagerId = super.getRequest().getPrincipal().getActiveRealm().getId();
			legs = this.repository.findLegsByAirlineManagerId(airlineManagerId);
		}

		List<Leg> orderedLegs = new ArrayList<>(legs);
		Collections.sort(orderedLegs, Comparator.comparing(Leg::getScheduledDeparture));

		super.getBuffer().addData(orderedLegs);
	}

	@Override
	public void unbind(final Leg leg) {
		Dataset dataset;

		dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival");
		dataset.put("departureAirport", leg.getDepartureAirport().getIataCode());
		dataset.put("arrivalAirport", leg.getArrivalAirport().getIataCode());

		super.getResponse().addData(dataset);
	}

}
