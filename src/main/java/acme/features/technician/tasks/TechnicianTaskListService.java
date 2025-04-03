
package acme.features.technician.tasks;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.Task;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.tasks.Tasks;
import acme.realms.Technicians;

@GuiService
public class TechnicianTaskListService extends AbstractGuiService<Technicians, Tasks> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianTaskRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		Technicians tech;

		tech = (Technicians) super.getRequest().getPrincipal().getActiveRealm();
		status = super.getRequest().getPrincipal().hasRealm(tech);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Task> task;
		int technicianId;

		technicianId = super.getRequest().getPrincipal().getActiveRealm().getId();
		task = this.repository.findTasksByTechnicianId(technicianId);

		super.getBuffer().addData(task);
	}

	@Override
	public void unbind(final Tasks task) {
		Dataset dataset;

		dataset = super.unbindObject(task, "type", "description", "priority", "estimatedDuration");
		super.getResponse().addData(dataset);
	}
}
