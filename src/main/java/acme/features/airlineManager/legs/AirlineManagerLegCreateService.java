
package acme.features.airlineManager.legs;

import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircrafts.Aircraft;
import acme.entities.aircrafts.AircraftStatus;
import acme.entities.airlines.Airline;
import acme.entities.airports.Airport;
import acme.entities.flights.Flight;
import acme.entities.legs.Leg;
import acme.entities.legs.LegStatus;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerLegCreateService extends AbstractGuiService<AirlineManager, Leg> {

	@Autowired
	private AirlineManagerLegRepository repository;

	// AbstractGuiService interfaced -------------------------------------------


	@Override
	public void authorise() {
		Integer flightId;
		Flight flight;
		AirlineManager airlineManager;
		boolean status = false;
		Airport depAirport = null;
		Airport arrAirport = null;
		Aircraft validAircraft = null;
		String legStatus;

		airlineManager = (AirlineManager) super.getRequest().getPrincipal().getActiveRealm();

		if (!super.getRequest().getData().isEmpty()) {
			flightId = super.getRequest().getData("flightId", Integer.class);
			if (flightId != null) {
				flight = this.repository.findFlightById(flightId);
				if (flight != null && flight.getAirlineManager().equals(airlineManager))
					status = true;

				if (flight != null && !flight.isDraftMode())
					status = false;
				// Validaciones
				if (super.getRequest().getMethod().equals("POST")) {
					// Departure airport and arrival airport
					Integer airDepId = super.getRequest().getData("departureAirport", Integer.class);
					Integer airArrId = super.getRequest().getData("arrivalAirport", Integer.class);

					if (airDepId != null) {
						depAirport = this.repository.findAirportById(airDepId);
						if (depAirport == null)
							status = false;
						if (airDepId == 0)
							status = true;
					} else
						status = false;

					if (airArrId != null) {
						arrAirport = this.repository.findAirportById(airArrId);
						if (arrAirport == null)
							status = false;
						if (airArrId == 0)
							status = true;
					} else
						status = false;

					// Aircraft null
					Integer aircraftId = super.getRequest().getData("aircraft", Integer.class);
					if (aircraftId != null) {
						validAircraft = this.repository.findAircraftById(aircraftId);
						if (validAircraft == null)
							status = false;
						if (aircraftId == 0)
							status = true;
					} else
						status = false;

					// Status different of ON_TIME
					legStatus = super.getRequest().getData("status", String.class);
					if (legStatus != null && !legStatus.equals(LegStatus.ON_TIME.toString()))
						status = false;
				}
			}
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Leg leg;
		int flightId = super.getRequest().getData("flightId", int.class);
		Flight flight = this.repository.findFlightById(flightId);

		leg = new Leg();
		leg.setDraftMode(true);
		leg.setFlight(flight);
		leg.setLegStatus(LegStatus.ON_TIME);

		super.getBuffer().addData(leg);
	}

	@Override
	public void bind(final Leg leg) {
		super.bindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "legStatus", "departureAirport", "arrivalAirport", "aircraft", "flight");
	}

	@Override
	public void validate(final Leg leg) {
		if (leg.getScheduledDeparture() != null && MomentHelper.isBefore(leg.getScheduledDeparture(), MomentHelper.getCurrentMoment()))
			super.state(false, "scheduledDeparture", "acme.validation.leg.scheduledDeparture.past");

		if (leg.getScheduledArrival() != null && leg.getScheduledDeparture() != null && MomentHelper.isBefore(leg.getScheduledArrival(), leg.getScheduledDeparture()))
			super.state(false, "scheduledDeparture", "acme.validation.leg.departure.after.arrival.message");

		if (leg.getScheduledArrival() != null && leg.getScheduledDeparture() != null) {
			Date departureWithDelta = MomentHelper.deltaFromMoment(leg.getScheduledDeparture(), 5, ChronoUnit.MINUTES);
			if (MomentHelper.isBefore(leg.getScheduledArrival(), MomentHelper.getCurrentMoment()))
				super.state(false, "scheduledArrival", "acme.validation.leg.scheduledArrival.past");

			if (MomentHelper.isBefore(leg.getScheduledArrival(), departureWithDelta))
				super.state(false, "scheduledArrival", "acme.validation.leg.departure.arrival.difference.message");
		}

		if (leg.getDepartureAirport() != null && leg.getDepartureAirport().equals(leg.getArrivalAirport())) {
			super.state(false, "arrivalAirport", "acme.validation.leg.same.departure.arrival.airport");
			super.state(false, "departureAirport", "acme.validation.leg.same.departure.arrival.airport");
		}

		if (leg.getAircraft() != null) {
			boolean operativeAircraft = leg.getAircraft().getStatus().equals(AircraftStatus.ACTIVE_SERVICE);
			super.state(operativeAircraft, "aircraft", "acme.validation.leg.operative.aircraft.message");

			Airline airline = leg.getAircraft().getAirline();
			if (leg.getFlightNumber().length() == 7 && !leg.getFlightNumber().substring(0, 3).equals(airline.getCode())) {
				super.state(false, "flightNumber", "acme.validation.leg.invalid.iata.flightNumber");
				super.state(false, "flightNumber", "The airline's IATA code: " + airline.getCode());
			}
		}

		if (!leg.isDraftMode())
			super.state(false, "*", "acme.validation.leg.create.no.draftmode");

		if (leg.getFlight() != null && !leg.getFlight().isDraftMode())
			super.state(false, "*", "acme.validation.leg.create.no.flight.draftmode");
	}

	@Override
	public void perform(final Leg leg) {
		this.repository.save(leg);
	}

	@Override
	public void unbind(final Leg leg) {
		int airlineManagerId;
		Collection<Flight> flights;
		Collection<Airport> airports;
		Collection<Aircraft> aircrafts;
		SelectChoices choicesFlight;
		SelectChoices choicesArrivalAirports;
		SelectChoices choicesDepartureAirports;
		SelectChoices choicesAircraft;
		SelectChoices choicesStatus;
		Dataset dataset;

		airlineManagerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		flights = this.repository.findFlightsByAirlineManagerId(airlineManagerId);
		airports = this.repository.findAllAirports();
		aircrafts = this.repository.findAllAircrafts();
		choicesFlight = SelectChoices.from(flights, "tag", leg.getFlight());
		choicesArrivalAirports = SelectChoices.from(airports, "iataCode", leg.getArrivalAirport());
		choicesDepartureAirports = SelectChoices.from(airports, "iataCode", leg.getDepartureAirport());
		choicesAircraft = SelectChoices.from(aircrafts, "registrationNumber", leg.getAircraft());
		choicesStatus = SelectChoices.from(LegStatus.class, leg.getLegStatus());

		dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "legStatus", "draftMode", "flight", "arrivalAirport", "departureAirport", "aircraft");
		dataset.put("flight", choicesFlight.getSelected().getKey());
		dataset.put("flights", choicesFlight);
		dataset.put("arrivalAirport", choicesArrivalAirports.getSelected().getKey());
		dataset.put("arrivalAirports", choicesArrivalAirports);
		dataset.put("departureAirport", choicesDepartureAirports.getSelected().getKey());
		dataset.put("departureAirports", choicesDepartureAirports);
		dataset.put("aircraft", choicesAircraft.getSelected().getKey());
		dataset.put("aircrafts", choicesAircraft);
		dataset.put("status", choicesStatus.getSelected().getKey());
		dataset.put("statuses", choicesStatus);
		super.addPayload(dataset, leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "legStatus", "draftMode", "flight", "arrivalAirport", "departureAirport", "aircraft");

		super.getResponse().addData(dataset);
	}
}
