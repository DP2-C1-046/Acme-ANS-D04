
package acme.features.maintenanceRecords;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.maintenanceRecords.MaintenanceRecords;
import acme.realms.Technicians;

@GuiController
public class TechnicianMaintenanceRecordController extends AbstractGuiController<Technicians, MaintenanceRecords> {

	// Internal state ------------------------------------------------------------

	@Autowired
	private TechnicianMaintenanceRecordListService		listService;

	@Autowired
	private TechnicianMaintenanceRecordListMineService	listMineService;

	@Autowired
	private TechnicianMaintenanceRecordShowService		showService;

	@Autowired
	private TechnicianMaintenanceRecordCreateService	createService;

	@Autowired
	private TechnicianMaintenanceRecordUpdateService	updateService;

	@Autowired
	private TechnicianMaintenanceRecordDeleteService	deleteService;

	@Autowired
	private TechnicianMaintenanceRecordPublishService	publishService;

	// Constructors --------------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);

		super.addCustomCommand("list-mine", "list", this.listMineService);
		super.addCustomCommand("publish", "update", this.publishService);
	}
}
