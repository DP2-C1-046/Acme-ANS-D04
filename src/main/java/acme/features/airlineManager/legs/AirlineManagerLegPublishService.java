
package acme.features.airlineManager.legs;

import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
import acme.entities.legs.Leg;
import acme.entities.legs.LegStatus;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerLegPublishService extends AbstractGuiService<AirlineManager, Leg> {

	@Autowired
	private AirlineManagerLegRepository repository;

	// AbstractGuiService interfaced -------------------------------------------


	@Override
	public void authorise() {
		boolean status = false;
		int masterId;
		Leg leg;
		AirlineManager manager;
		Airport depAirport = null;
		Airport arrAirport = null;
		Aircraft validAircraft = null;
		String legStatus;

		if (!super.getRequest().getData().isEmpty() && super.getRequest().getData() != null) {
			int managerId = super.getRequest().getPrincipal().getActiveRealm().getId();
			masterId = super.getRequest().getData("id", int.class);
			leg = this.repository.findLegById(masterId);
			boolean fN = super.getRequest().hasData("flightNumber");
			manager = leg == null ? null : leg.getFlight().getAirlineManager();
			status = leg != null && super.getRequest().getPrincipal().hasRealm(manager) && managerId == manager.getId() && fN;

			// Validaciones adicionales para POST
			if (super.getRequest().getMethod().equals("POST")) {
				// Validaciones para aeropuertos de salida y llegada
				Integer airDepId = super.getRequest().getData("departureAirport", Integer.class);
				Integer airArrId = super.getRequest().getData("arrivalAirport", Integer.class);

				// Validación del aeropuerto de salida
				if (airDepId != null) {
					depAirport = this.repository.findAirportById(airDepId);
					if (depAirport == null)
						status = false;
					if (airDepId == 0)
						status = true;
				} else
					status = false;

				// Validación del aeropuerto de llegada
				if (airArrId != null) {
					arrAirport = this.repository.findAirportById(airArrId);
					if (arrAirport == null)
						status = false;
					if (airArrId == 0)
						status = true;
				} else
					status = false;

				// Validación de la aeronave
				Integer aircraftId = super.getRequest().getData("aircraft", Integer.class);
				if (aircraftId != null) {
					validAircraft = this.repository.findAircraftById(aircraftId);
					if (validAircraft == null)
						status = false;
					if (aircraftId == 0)
						status = true;
				} else
					status = false;

				// Validación del estado del tramo
				legStatus = super.getRequest().getData("status", String.class);
				if (legStatus != null && !legStatus.equals(LegStatus.ON_TIME.toString()))
					status = false;
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
	public void bind(final Leg leg) {
		super.bindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "status", "departureAirport", "arrivalAirport", "aircraft");
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

		if (leg.getScheduledArrival() != null && leg.getScheduledDeparture() != null) {
			Collection<Leg> allPublishedLegs = this.repository.findAllPublishedLegs();
			for (Leg publishedLeg : allPublishedLegs)
				if (publishedLeg.getAircraft().equals(leg.getAircraft())) {
					boolean overlap = MomentHelper.isBefore(publishedLeg.getScheduledDeparture(), leg.getScheduledArrival()) && MomentHelper.isAfter(publishedLeg.getScheduledArrival(), leg.getScheduledDeparture());
					if (overlap) {
						super.state(false, "aircraft", "acme.validation.leg.same.aircraft.message");
						break;
					}
				}

			Collection<Leg> flightPublishedLegs = this.repository.findAllPublishedLegsByFlightId(leg.getFlight().getId());
			for (Leg publishedLeg : flightPublishedLegs) {
				boolean overlap = MomentHelper.isBefore(publishedLeg.getScheduledDeparture(), leg.getScheduledArrival()) && MomentHelper.isAfter(publishedLeg.getScheduledArrival(), leg.getScheduledDeparture());
				if (overlap) {
					super.state(false, "scheduledDeparture", "acme.validation.leg.overlap.message");
					super.state(false, "scheduledArrival", "acme.validation.leg.overlap.message");
					break;
				}
			}
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

		boolean publishedFlight = leg.getFlight().isDraftMode();
		super.state(publishedFlight, "*", "acme.validation.leg.flight.draftMode");

		// Para vuelos que necesitan conexión directa entre tramos
		Collection<Leg> publishedLegs = this.repository.findAllPublishedLegsByFlightId(leg.getFlight().getId());
		publishedLegs.add(leg);
		List<Leg> orderedLegs = publishedLegs.stream().sorted(Comparator.comparing(Leg::getScheduledDeparture)).collect(Collectors.toList());
		int index = orderedLegs.indexOf(leg);
		if (index != -1 && leg.getFlight().isNeedsSelfTransfer() && leg.getDepartureAirport() != null && leg.getArrivalAirport() != null) {
			// Validar leg anterior
			if (index > 0) {
				Leg previousLeg = orderedLegs.get(index - 1);
				if (!leg.getDepartureAirport().equals(previousLeg.getArrivalAirport()))
					super.state(false, "departureAirport", "acme.validation.leg.departureAirport");
			}

			// Validar siguiente leg
			if (index < orderedLegs.size() - 1) {
				Leg nextLeg = orderedLegs.get(index + 1);
				if (!leg.getArrivalAirport().equals(nextLeg.getDepartureAirport()))
					super.state(false, "arrivalAirport", "acme.validation.leg.arrivalAirport");
			}
		}
	}

	@Override
	public void perform(final Leg leg) {
		assert leg != null;
		leg.setDraftMode(false);
		this.repository.save(leg);
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

		dataset = super.unbindObject(leg, "scheduledDeparture", "scheduledArrival", "legStatus", "departureAirport", "arrivalAirport", "aircraft", "draftMode");
		dataset.put("flightNumber", leg.getFlightNumber());
		dataset.put("statuses", statuses);
		dataset.put("flightId", leg.getFlight().getId());
		dataset.put("departureAirports", selectedDepartureAirport);
		dataset.put("arrivalAirports", selectedArrivalAirport);
		dataset.put("flight", leg.getFlight().getTag());
		dataset.put("aircrafts", selectedAircrafts);
		dataset.put("duration", leg.getDuration());

		super.getResponse().addData(dataset);
	}
}
