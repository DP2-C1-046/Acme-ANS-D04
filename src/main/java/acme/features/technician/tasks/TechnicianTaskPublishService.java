
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
public class TechnicianTaskPublishService extends AbstractGuiService<Technicians, Tasks> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianTaskRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int taskId;

		Tasks task;
		Technicians tech;

		tech = (Technicians) super.getRequest().getPrincipal().getActiveRealm();
		taskId = super.getRequest().getData("id", int.class);
		task = this.repository.findTaskById(taskId);

		status = task != null && task.isDraftMode() && super.getRequest().getPrincipal().hasRealm(tech);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Tasks task;
		int id;

		id = super.getRequest().getData("id", int.class);
		task = this.repository.findTaskById(id);

		super.getBuffer().addData(task);
	}

	@Override
	public void bind(final Tasks task) {

		super.bindObject(task, "type", "description", "priority", "estimatedDuration");

	}

	@Override
	public void validate(final Tasks task) {
		;
	}
	@Override
	public void perform(final Tasks task) {
		task.setDraftMode(false);
		this.repository.save(task);
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
