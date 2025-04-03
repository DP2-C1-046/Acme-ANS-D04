
package acme.features.technician.involves;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.maintenanceRecords.Involves;
import acme.realms.Technicians;

@GuiController
public class TechnicianInvolvedInController extends AbstractGuiController<Technicians, Involves> {

	@Autowired
	private TechnicianInvolvedInServiceShow		showService;

	@Autowired
	private TechnicianInvolvedInServiceList		listService;

	@Autowired
	private TechnicianInvolvedInUpdateService	updateService;

	@Autowired
	private TechnicianInvolvedInCreateService	createService;

	@Autowired
	private TechnicianInvolvedInDeleteService	deleteService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("delete", this.deleteService);
	}
}
