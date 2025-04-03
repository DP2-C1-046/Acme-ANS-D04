
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
public class TechnicianInvolvedInCreateService extends AbstractGuiService<Technicians, Involves> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianInvolvedInRepository repository;


	// AbstractService<Manager, ProjectUserStoryLink> ---------------------------
	@Override
	public void authorise() {
		Technicians tech;
		boolean status;
		tech = (Technicians) super.getRequest().getPrincipal().getActiveRealm();

		status = super.getRequest().getPrincipal().hasRealm(tech);
		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		Involves involved;

		involved = new Involves();

		super.getBuffer().addData(involved);
	}

	@Override
	public void bind(final Involves involved) {

		super.bindObject(involved, "maintanenceRecord", "task");
	}

	@Override
	public void validate(final Involves involved) {
		MaintenanceRecords maintanenceRecord;
		Tasks task;

		maintanenceRecord = involved.getMaintenanceRecords();
		task = involved.getTasks();

		Technicians tech;
		tech = (Technicians) super.getRequest().getPrincipal().getActiveRealm();

		super.state(maintanenceRecord != null, "*", "technician.involved-in.create.error.null-record");
		//Esto habra que crearlo en algun lado?¿?¿?¿
		super.state(task != null, "*", "technician.involved-in.create.error.null-task");

		boolean exists = this.repository.existsByRecordAndTask(maintanenceRecord, task);
		super.state(!exists, "*", "technician.involved-in.create.error.duplicate-record-task");

	}

	@Override
	public void perform(final Involves involved) {
		this.repository.save(involved);
	}

	@Override
	public void unbind(final Involves involved) {
		//Record=booking
		Dataset dataset;

		Technicians tech = (Technicians) super.getRequest().getPrincipal().getActiveRealm();

		SelectChoices recordChoices;
		SelectChoices taskChoices;

		Collection<Tasks> tasks = this.repository.findTaskByTechnicianId(tech.getId());

		//puedo hacer un error a la hora de mirar si esta ya asociado o no pero pooco mas
		Collection<MaintenanceRecords> records = this.repository.findNotPublishRecord(tech.getId(), true);

		taskChoices = SelectChoices.from(tasks, "description", involved.getTasks());

		recordChoices = SelectChoices.from(records, "maintanenceMoment", involved.getMaintenanceRecords());

		dataset = super.unbindObject(involved, "maintanenceRecord", "task");
		dataset.put("maintanenceRecord", recordChoices);
		dataset.put("task", taskChoices);
		dataset.put("draftMode", true);
		super.getResponse().addData(dataset);

	}
}
