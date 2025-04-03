
package acme.features.technician.involves;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenanceRecords.Involves;
import acme.entities.maintenanceRecords.MaintenanceRecords;
import acme.entities.tasks.Tasks;
import acme.realms.Technicians;

@GuiService
public class TechnicianInvolvedInServiceShow extends AbstractGuiService<Technicians, Involves> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianInvolvedInRepository repository;


	// AbstractService<Manager, ProjectUserStoryLink> ---------------------------
	@Override
	public void authorise() {
		Technicians tech;
		boolean status;
		tech = (Technicians) super.getRequest().getPrincipal().getActiveRealm();
		int id = super.getRequest().getData("id", int.class);
		Involves involved = this.repository.findInvolvedIn(id);
		status = involved != null && super.getRequest().getPrincipal().hasRealm(tech);
		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		Involves involved = this.repository.findInvolvedIn(id);

		super.getBuffer().addData(involved);
	}

	@Override
	public void unbind(final Involves involved) {
		Dataset dataset;
		SelectChoices recordChoices;
		SelectChoices taskChoices;

		Technicians technician = (Technicians) super.getRequest().getPrincipal().getActiveRealm();

		Collection<MaintenanceRecords> records = this.repository.findRecordByTechnicianId(technician.getId());
		Collection<Tasks> tasks = this.repository.findTaskByTechnicianId(technician.getId());

		recordChoices = SelectChoices.from(records, "maintanenceMoment", involved.getMaintenanceRecords());
		taskChoices = SelectChoices.from(tasks, "description", involved.getTasks());

		dataset = super.unbindObject(involved, "maintanenceRecord", "task");
		dataset.put("maintanenceRecord", recordChoices);
		dataset.put("task", taskChoices);

		dataset.put("draftMode", involved.getMaintenanceRecords().isDraftMode());

		super.getResponse().addData(dataset);

	}

}
