
package acme.features.maintenanceRecords;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircrafts.Aircraft;
import acme.entities.maintenanceRecords.MaintenanceRecords;
import acme.entities.maintenanceRecords.MaintenanceRecordsStatus;
import acme.realms.Technicians;

@GuiService
public class TechnicianMaintenanceRecordShowService extends AbstractGuiService<Technicians, MaintenanceRecords> {

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
		status = maintenanceRecord != null;

		status = status && (!maintenanceRecord.isDraftMode() || super.getRequest().getPrincipal().hasRealm(technician));

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
	public void unbind(final MaintenanceRecords maintenanceRecord) {
		SelectChoices statuses;
		Collection<Aircraft> aircrafts;
		SelectChoices choices;
		Dataset dataset;

		statuses = SelectChoices.from(MaintenanceRecordsStatus.class, maintenanceRecord.getStatus());

		aircrafts = this.repository.findAvailableAircrafts();
		choices = SelectChoices.from(aircrafts, "model", maintenanceRecord.getAircraft());

		dataset = super.unbindObject(maintenanceRecord, "technician.identity.name", "maintenanceDate", "nextInspectionDueDate", "status", "estimatedCost", "notes", "draftMode");
		dataset.put("statuses", statuses);
		dataset.put("aircraft", choices.getSelected().getKey());
		dataset.put("aircrafts", choices);

		super.getResponse().addData(dataset);
	}
}
