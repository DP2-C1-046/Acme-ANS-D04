
package acme.features.administrator.aircraft;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircrafts.Aircraft;
import acme.entities.aircrafts.AircraftStatus;
import acme.entities.airlines.Airline;

@GuiService
public class AdministratorAircraftUpdateService extends AbstractGuiService<Administrator, Aircraft> {

	@Autowired
	private AdministratorAircraftRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}
	@Override
	public void load() {
		Aircraft aircraft;
		int id = super.getRequest().getData("id", int.class);
		aircraft = this.repository.findAircraftById(id);
		super.getBuffer().addData(aircraft);
	}

	@Override
	public void unbind(final Aircraft aircraft) {

		Dataset dataset;
		SelectChoices choices;
		SelectChoices selectedAirlines;
		Collection<Airline> airlines;

		choices = SelectChoices.from(AircraftStatus.class, aircraft.getStatus());
		airlines = this.repository.findAllAirlines();
		selectedAirlines = SelectChoices.from(airlines, "name", aircraft.getAirline());

		dataset = super.unbindObject(aircraft, "model", "registrationNumber", "capacity", "cargoWeight", "status", "details");
		dataset.put("statuses", choices);

		dataset.put("airline", selectedAirlines.getSelected().getKey());
		dataset.put("airlines", selectedAirlines);

		super.getResponse().addData(dataset);

	}

}
