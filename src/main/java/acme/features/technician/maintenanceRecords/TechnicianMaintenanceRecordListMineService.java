
package acme.features.technician.maintenanceRecords;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenanceRecords.MaintenanceRecords;
import acme.realms.Technicians;

@GuiService
public class TechnicianMaintenanceRecordListMineService extends AbstractGuiService<Technicians, MaintenanceRecords> {

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
		Collection<MaintenanceRecords> maintenanceRecords;
		int technicianId;

		technicianId = super.getRequest().getPrincipal().getActiveRealm().getId();
		maintenanceRecords = this.repository.findMaintenanceRecordsByTechnicianId(technicianId);

		super.getBuffer().addData(maintenanceRecords);
	}

	@Override
	public void unbind(final MaintenanceRecords maintenanceRecord) {
		Dataset dataset;

		dataset = super.unbindObject(maintenanceRecord, "aircraft.model", "maintenanceDate", "nextInspectionDueDate", "status");
		super.addPayload(dataset, maintenanceRecord, "estimatedCost", "technician.identity.name");

		super.getResponse().addData(dataset);
	}
}
