
package acme.features.technician.tasks;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenanceRecords.MaintenanceRecords;
import acme.entities.tasks.Tasks;
import acme.entities.tasks.TasksType;
import acme.realms.Technicians;

@GuiService
public class TechnicianRecordTaskListService extends AbstractGuiService<Technicians, Tasks> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianTaskRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {

		boolean status;
		int technicianId;
		int taskId;

		technicianId = super.getRequest().getPrincipal().getActiveRealm().getId();

		status = super.getRequest().getPrincipal().hasRealm(this.repository.findTechnicianById(technicianId));

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Tasks> tasks;
		Technicians tech;
		tech = (Technicians) super.getRequest().getPrincipal().getActiveRealm();
		int id;
		MaintenanceRecords record;

		id = super.getRequest().getData("recordId", int.class);
		record = this.repository.findRecordById(id);

		tasks = this.repository.findTasksByTechId(record.getId(), tech.getId());

		super.getBuffer().addData(tasks);
	}

	@Override
	public void unbind(final Tasks task) {
		Dataset dataset;
		SelectChoices choices;
		choices = SelectChoices.from(TasksType.class, task.getTasksType());

		dataset = super.unbindObject(task, "type", "draftMode", "description", "priority", "estimatedDuration");
		//dataset.put("type", choices.getSelected().getKey());
		//dataset.put("types", choices);
		super.getResponse().addData(dataset);
	}
}
