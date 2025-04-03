
package acme.features.maintenanceRecords;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircrafts.Aircraft;
import acme.entities.maintenanceRecords.Involves;
import acme.entities.maintenanceRecords.MaintenanceRecords;
import acme.realms.Technicians;

@GuiService
public class TechnicianMaintenanceRecordDeleteService extends AbstractGuiService<Technicians, MaintenanceRecords> {

	// Internal state ------------------------------------------------------------

	@Autowired
	private TechnicianMaintenanceRecordRepository repository;

	// AbstractGuiService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		MaintenanceRecords maintenanceRecord;
		Technicians technician;

		masterId = super.getRequest().getData("id", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordsById(masterId);
		technician = maintenanceRecord == null ? null : maintenanceRecord.getTechnicians();
		status = maintenanceRecord != null && maintenanceRecord.isDraftMode() && super.getRequest().getPrincipal().hasRealm(technician);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		MaintenanceRecords maintenanceRecord;
		int id;

		id = super.getRequest().getData("id", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordsById(id);

		super.getBuffer().addData(maintenanceRecord);
	}

	@Override
	public void bind(final MaintenanceRecords maintenanceRecord) {
		int aircraftId;
		Aircraft aircraft;

		aircraftId = super.getRequest().getData("aircraft", int.class);
		aircraft = this.repository.findAircraftById(aircraftId);

		super.bindObject(maintenanceRecord, "nextInspectionDueDate", "estimatedCost", "notes");
		maintenanceRecord.setAircraft(aircraft);
	}

	@Override
	public void validate(final MaintenanceRecords maintenanceRecord) {
		;
	}

	@Override
	public void perform(final MaintenanceRecords maintenanceRecord) {
		Collection<Involves> involves;

		involves = this.repository.findInvolvesFromMaintenanceRecordsId(maintenanceRecord.getId());
		this.repository.deleteAll(involves);

		this.repository.delete(maintenanceRecord);
	}

	@Override
	public void unbind(final MaintenanceRecords maintenanceRecord) {
		Collection<Aircraft> aircrafts;
		SelectChoices choices;
		Dataset dataset;

		aircrafts = this.repository.findAvailableAircrafts();
		choices = SelectChoices.from(aircrafts, "model", maintenanceRecord.getAircraft());

		dataset = super.unbindObject(maintenanceRecord, "technician.identity.name", "maintenanceDate", "nextInspectionDueDate", "status", "estimatedCost", "notes", "draftMode");
		dataset.put("aircraft", choices.getSelected().getKey());
		dataset.put("aircrafts", choices);

		super.getResponse().addData(dataset);
	}
}
