
package acme.features.airlineManager.legs;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircrafts.Aircraft;
import acme.entities.aircrafts.AircraftStatus;
import acme.entities.airports.Airport;
import acme.entities.legs.Leg;
import acme.entities.legs.LegStatus;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerLegShowService extends AbstractGuiService<AirlineManager, Leg> {

	@Autowired
	private AirlineManagerLegRepository repository;

	// AbstractGuiService interfaced -------------------------------------------


	@Override
	public void authorise() {
		boolean status = false;
		Integer masterId;
		Leg leg;
		AirlineManager airlineManager;

		int managerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		if (!super.getRequest().getData().isEmpty()) {
			masterId = super.getRequest().getData("id", Integer.class);
			if (masterId != null) {
				leg = this.repository.findLegById(masterId);
				airlineManager = leg == null ? null : leg.getFlight().getAirlineManager();
				status = leg != null && super.getRequest().getPrincipal().hasRealm(airlineManager) && managerId == airlineManager.getId();
			}
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Leg leg;
		int id;

		id = super.getRequest().getData("id", int.class);
		leg = this.repository.findLegById(id);

		super.getBuffer().addData(leg);
	}

	@Override
	public void unbind(final Leg leg) {
		SelectChoices statuses;
		Dataset dataset;
		Collection<Aircraft> aircrafts;
		SelectChoices selectedAircrafts;
		Collection<Aircraft> activeAircrafts;
		Collection<Airport> airports;
		SelectChoices selectedDepartureAirport;
		SelectChoices selectedArrivalAirport;

		statuses = SelectChoices.from(LegStatus.class, leg.getLegStatus());
		aircrafts = this.repository.findAllAircrafts();
		activeAircrafts = aircrafts.stream().filter(a -> a.getStatus().equals(AircraftStatus.ACTIVE_SERVICE)).collect(Collectors.toList());
		selectedAircrafts = SelectChoices.from(activeAircrafts, "registrationNumber", leg.getAircraft());

		airports = this.repository.findAllAirports();
		selectedDepartureAirport = SelectChoices.from(airports, "iataCode", leg.getDepartureAirport());
		selectedArrivalAirport = SelectChoices.from(airports, "iataCode", leg.getArrivalAirport());

		dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "draftMode");

		dataset.put("status", leg.getLegStatus());
		dataset.put("statuses", statuses);

		dataset.put("departureAirports", selectedDepartureAirport);
		dataset.put("arrivalAirports", selectedArrivalAirport);
		dataset.put("flight", leg.getFlight().getTag());
		dataset.put("aircrafts", selectedAircrafts);
		dataset.put("duration", leg.getDuration());

		super.getResponse().addData(dataset);
	}

}
