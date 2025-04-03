
package acme.features.technician.tasks;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenanceRecords.Involves;
import acme.entities.tasks.Tasks;
import acme.features.technician.involves.TechnicianInvolvedInRepository;
import acme.realms.Technicians;

@GuiService
public class TechnicianTaskDeleteService extends AbstractGuiService<Technicians, Tasks> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianTaskRepository		repository;

	@Autowired
	private TechnicianInvolvedInRepository	involvedRepository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int taskId;
		Tasks task;
		Technicians tech;

		taskId = super.getRequest().getData("id", int.class);
		task = this.repository.findTaskById(taskId);
		tech = (Technicians) super.getRequest().getPrincipal().getActiveRealm();
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
		Collection<Involves> involved;

		involved = this.repository.findAllInvolvedInById(task.getId());
		this.involvedRepository.deleteAll(involved);
		this.repository.delete(task);

	}

	@Override
	public void unbind(final Tasks task) {
		Dataset dataset;

		dataset = super.unbindObject(task, "type", "description", "priority", "estimatedDuration", "draftMode");

		super.getResponse().addData(dataset);
	}
}
