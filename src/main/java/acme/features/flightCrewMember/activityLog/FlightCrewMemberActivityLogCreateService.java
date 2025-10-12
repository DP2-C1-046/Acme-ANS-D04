
package acme.features.flightCrewMember.activityLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLogs.ActivityLog;
import acme.entities.assignments.FlightAssignment;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightCrewMemberActivityLogCreateService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		FlightAssignment flightAssignment;
		masterId = super.getRequest().getData("masterId", int.class);
		flightAssignment = this.repository.findFlightAssignmentById(masterId);
		status = flightAssignment != null && !flightAssignment.getDraftMode() && super.getRequest().getPrincipal().hasRealm(flightAssignment.getFlightCrewMember());
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		ActivityLog activityLog = new ActivityLog();
		FlightAssignment flightAssignment;
		int masterId;
		masterId = super.getRequest().getData("masterId", int.class);
		flightAssignment = this.repository.findFlightAssignmentById(masterId);
		activityLog.setFlightAssignment(flightAssignment);
		activityLog.setDraftMode(true);
		activityLog.setSeverityLevel(0);
		activityLog.setDescription("");
		activityLog.setTypeOfIndicent("");
		super.getBuffer().addData(activityLog);
	}

	@Override
	public void bind(final ActivityLog log) {
		log.setRegistrationMoment(MomentHelper.getCurrentMoment());
		log.setDraftMode(true);
		super.bindObject(log, "typeOfIndicent", "description", "severityLevel");

	}

	@Override
	public void validate(final ActivityLog log) {

		int masterId = super.getRequest().getData("masterId", int.class);
		FlightAssignment assignment = this.repository.findFlightAssignmentById(masterId);
		boolean legIsCompleted = MomentHelper.isAfter(MomentHelper.getCurrentMoment(), assignment.getLeg().getScheduledArrival());
		super.state(legIsCompleted, "*", "acme.validation.flight-crew-member.activity-log.validation.create");
	}

	@Override
	public void perform(final ActivityLog log) {
		this.repository.save(log);
	}

	@Override
	public void unbind(final ActivityLog log) {
		Dataset dataset;

		dataset = super.unbindObject(log, "registrationMoment", "typeOfIndicent", "description", "severityLevel");
		dataset.put("masterId", super.getRequest().getData("masterId", int.class));

		super.getResponse().addData(dataset);
	}
}
