
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLogs.ActivityLog;
import acme.entities.assignments.AssignmentStatus;
import acme.entities.assignments.FlightAssignment;
import acme.entities.assignments.FlightCrewDuty;
import acme.entities.legs.Leg;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightCrewMemberAssignmentDeleteService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightCrewMemberAssignmentRepository repository;


	@Override
	public void authorise() {
		FlightAssignment flightAssignment;
		boolean status;
		int assignmentId;
		int memberId;

		assignmentId = super.getRequest().getData("id", int.class);
		flightAssignment = this.repository.findFlightAssignmentById(assignmentId);
		memberId = flightAssignment == null ? null : super.getRequest().getPrincipal().getActiveRealm().getId();
		status = flightAssignment != null && flightAssignment.getFlightCrewMember().getId() == memberId && flightAssignment.getDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		FlightAssignment assignment;
		int id;

		id = super.getRequest().getData("id", int.class);
		assignment = this.repository.findFlightAssignmentById(id);

		super.getBuffer().addData(assignment);
	}

	@Override
	public void bind(final FlightAssignment assignment) {
		super.bindObject(assignment, "flightCrewDuty", "assignmentStatus", "remarks", "leg");
	}

	@Override
	public void validate(final FlightAssignment assignment) {
		;
	}

	@Override
	public void perform(final FlightAssignment assignment) {
		Collection<ActivityLog> logs;

		logs = this.repository.findActivityLogsByAssignmentId(assignment.getId());
		this.repository.deleteAll(logs);
		this.repository.delete(assignment);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		Dataset dataset;
		SelectChoices dutyChoice;
		SelectChoices currentStatusChoice;

		SelectChoices legChoice;
		Collection<Leg> legs;

		dutyChoice = SelectChoices.from(FlightCrewDuty.class, assignment.getFlightCrewDuty());
		currentStatusChoice = SelectChoices.from(AssignmentStatus.class, assignment.getAssignmentStatus());

		legs = this.repository.findAllLegs();
		legChoice = SelectChoices.from(legs, "flightNumber", assignment.getLeg());

		dataset = super.unbindObject(assignment, "flightCrewDuty", "lastUpdate", "assignmentStatus", "remarks", "leg");
		dataset.put("flightCrewMember", assignment.getFlightCrewMember().getId());
		dataset.put("dutyChoice", dutyChoice);
		dataset.put("currentStatusChoice", currentStatusChoice);
		dataset.put("legChoice", legChoice);

		super.getResponse().addData(dataset);
	}

}
