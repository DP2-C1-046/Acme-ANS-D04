
package acme.features.maintenanceRecords;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircrafts.Aircraft;
import acme.entities.maintenanceRecords.MaintenanceRecords;
import acme.entities.maintenanceRecords.MaintenanceRecordsStatus;
import acme.realms.Technicians;

@GuiService
public class TechnicianMaintenanceRecordCreateService extends AbstractGuiService<Technicians, MaintenanceRecords> {

	// Internal state ------------------------------------------------------------

	@Autowired
	private TechnicianMaintenanceRecordRepository repository;

	// AbstractGuiService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		MaintenanceRecords maintenanceRecord;
		Technicians technician;

		technician = (Technicians) super.getRequest().getPrincipal().getActiveRealm();

		maintenanceRecord = new MaintenanceRecords();
		maintenanceRecord.setTechnicians(technician);
		maintenanceRecord.setMoment(MomentHelper.getCurrentMoment());
		maintenanceRecord.setDraftMode(true);

		super.getBuffer().addData(maintenanceRecord);
	}

	@Override
	public void bind(final MaintenanceRecords maintenanceRecord) {
		int aircraftId;
		Aircraft aircraft;

		aircraftId = super.getRequest().getData("aircraft", int.class);
		aircraft = this.repository.findAircraftById(aircraftId);

		super.bindObject(maintenanceRecord, "maintenanceDate", "status", "nextInspectionDueDate", "estimatedCost", "notes");
		maintenanceRecord.setAircraft(aircraft);
	}

	@Override
	public void validate(final MaintenanceRecords maintenanceRecord) {
		{
			boolean status;
			status = !maintenanceRecord.getStatus().equals(MaintenanceRecordsStatus.COMPLETED);

			super.state(status, "*", "technician.maintenance-record.create.status");
		}
	}

	@Override
	public void perform(final MaintenanceRecords maintenanceRecord) {
		this.repository.save(maintenanceRecord);
	}

	@Override
	public void unbind(final MaintenanceRecords maintenanceRecord) {
		SelectChoices possibleStatus;
		Collection<Aircraft> aircrafts;
		SelectChoices choices;
		Dataset dataset;

		possibleStatus = new SelectChoices();
		possibleStatus.add("0", "----", maintenanceRecord.getStatus() == null);
		possibleStatus.add("PENDING", "PENDING", maintenanceRecord.getStatus() == MaintenanceRecordsStatus.PENDING);
		possibleStatus.add("IN_PROGRESS", "IN_PROGRESS", maintenanceRecord.getStatus() == MaintenanceRecordsStatus.IN_PROGRESS);

		aircrafts = this.repository.findAvailableAircrafts();
		choices = SelectChoices.from(aircrafts, "model", maintenanceRecord.getAircraft());

		dataset = super.unbindObject(maintenanceRecord, "technician.identity.name", "maintenanceDate", "nextInspectionDueDate", "estimatedCost", "notes");
		dataset.put("statuses", possibleStatus);
		dataset.put("aircraft", choices.getSelected().getKey());
		dataset.put("aircrafts", choices);

		super.getResponse().addData(dataset);
	}

}
