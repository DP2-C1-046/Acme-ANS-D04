
package acme.features.technician.tasks;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.tasks.Tasks;
import acme.entities.tasks.TasksType;
import acme.realms.Technicians;

@GuiService
public class TechnicianTaskShowService extends AbstractGuiService<Technicians, Tasks> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianTaskRepository repository;


	// AbstractGuiService interface -------------------------------------------
	@Override
	public void authorise() {
		boolean status;
		Technicians tech;
		int taskId;
		Tasks task;

		taskId = super.getRequest().getData("id", int.class);
		task = this.repository.findTaskById(taskId);
		tech = task == null ? null : task.getTechnicians();
		status = super.getRequest().getPrincipal().hasRealm(tech);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int taskId;
		Tasks task;

		taskId = super.getRequest().getData("id", int.class);

		task = this.repository.findTaskById(taskId);

		super.getBuffer().addData(task);
	}

	@Override
	public void unbind(final Tasks task) {
		Dataset dataset;
		SelectChoices choices;
		choices = SelectChoices.from(TasksType.class, task.getTasksType());

		dataset = super.unbindObject(task, "type", "draftMode", "description", "priority", "estimatedDuration");
		dataset.put("type", choices);
		super.getResponse().addData(dataset);
	}
}
