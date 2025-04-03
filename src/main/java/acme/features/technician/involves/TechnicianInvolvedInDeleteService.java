
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
public class TechnicianInvolvedInDeleteService extends AbstractGuiService<Technicians, Involves> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianInvolvedInRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {

		Technicians tech;
		boolean status;
		MaintenanceRecords record;
		Tasks task;
		tech = (Technicians) super.getRequest().getPrincipal().getActiveRealm();
		int involvedInId = super.getRequest().getData("id", int.class);
		task = this.repository.findOneTaskByInvolvedIn(involvedInId);
		record = this.repository.findOneRecordByInvolvedIn(involvedInId);
		status = record != null && task != null && record.isDraftMode() && super.getRequest().getPrincipal().hasRealm(tech);
		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		Involves involved;
		int id;
		id = super.getRequest().getData("id", int.class);
		involved = this.repository.findInvolvedIn(id);

		super.getBuffer().addData(involved);

	}

	@Override
	public void bind(final Involves involved) {
		super.bindObject(involved, "maintanenceRecord", "task");
	}

	@Override
	public void validate(final Involves involved) {
		//Mirar esta validacion...
		super.state(involved.getMaintenanceRecords().isDraftMode(), "*", "customers.form.error.draft-mode");
		;
	}

	@Override
	public void perform(final Involves involved) {

		this.repository.delete(involved);

	}

	@Override
	public void unbind(final Involves involved) {
		Dataset dataset;
		Technicians tech = (Technicians) super.getRequest().getPrincipal().getActiveRealm();

		Collection<Tasks> task = this.repository.findTaskByTechnicianId(tech.getId());

		Collection<MaintenanceRecords> record = this.repository.findNotPublishRecord(tech.getId(), true);

		SelectChoices taskChoices;
		SelectChoices recordChoices;

		taskChoices = SelectChoices.from(task, "description", involved.getTasks());

		recordChoices = SelectChoices.from(record, "maintanenceMoment", involved.getMaintenanceRecords());

		dataset = super.unbindObject(involved, "maintanenceRecord", "task");
		dataset.put("maintanenceRecord", recordChoices);
		dataset.put("task", taskChoices);
		//IsDraftMode()
		dataset.put("draftMode", involved.getMaintenanceRecords().isDraftMode());

		super.getResponse().addData(dataset);
	}

}
