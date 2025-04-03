
package acme.features.technician.tasks;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenanceRecords.Involves;
import acme.entities.maintenanceRecords.MaintenanceRecords;
import acme.entities.tasks.Tasks;
import acme.entities.tasks.TasksType;
import acme.features.technician.maintenanceRecords.TechnicianMaintenanceRecordRepository;
import acme.realms.Technicians;

@GuiService
public class TechnicianTaskCreateService extends AbstractGuiService<Technicians, Tasks> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianTaskRepository				repository;

	@Autowired
	private TechnicianMaintenanceRecordRepository	recordRepository;

	// AbstractGuiService interface ------------------------------------------

	Involves										involved	= new Involves();


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
		Tasks task;
		Technicians tech;
		int recordId = super.getRequest().getData("recordId", int.class);
		MaintenanceRecords record = this.recordRepository.findMaintenanceRecordsById(recordId);

		tech = (Technicians) super.getRequest().getPrincipal().getActiveRealm();

		task = new Tasks();
		task.setTechnicians(tech);

		super.getBuffer().addData(task);

		if (record != null)
			this.involved.setMaintenanceRecords(record);

	}

	@Override
	public void bind(final Tasks task) {

		super.bindObject(task, "type", "description", "priority", "estimatedDuration");
		task.setDraftMode(true);

	}

	@Override
	public void validate(final Tasks task) {

		;
	}

	@Override
	public void perform(final Tasks task) {

		this.repository.save(task);
		this.involved.setTasks(task);
		this.recordRepository.save(this.involved);

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
