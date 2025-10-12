
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
public class FlightCrewMemberActivityLogPublishService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		ActivityLog activityLog;
		masterId = super.getRequest().getData("id", int.class);
		activityLog = this.repository.findActivityLogById(masterId);
		status = activityLog != null && super.getRequest().getPrincipal().hasRealm(activityLog.getFlightAssignment().getFlightCrewMember());
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		ActivityLog activityLog;
		int id;

		id = super.getRequest().getData("id", int.class);
		activityLog = this.repository.findActivityLogById(id);
		super.getBuffer().addData(activityLog);
	}

	@Override
	public void bind(final ActivityLog log) {
		int activityLogId;
		FlightAssignment flightAssignment;
		activityLogId = super.getRequest().getData("id", int.class);
		flightAssignment = this.repository.findFlightAssignmentByActivityLogId(activityLogId);
		log.setFlightAssignment(flightAssignment);
		log.setRegistrationMoment(MomentHelper.getCurrentMoment());
		super.bindObject(log, "typeOfIndicent", "description", "severityLevel");

	}

	@Override
	public void validate(final ActivityLog log) {
		int activityLogId = super.getRequest().getData("id", int.class);
		FlightAssignment flightAssignment = this.repository.findFlightAssignmentByActivityLogId(activityLogId);
		boolean legHasArrive = MomentHelper.isAfter(MomentHelper.getCurrentMoment(), flightAssignment.getLeg().getScheduledArrival());
		super.state(legHasArrive, "*", "acme.validation.flight-crew-member.activity-log.validation.create");
	}

	@Override
	public void perform(final ActivityLog log) {
		log.setDraftMode(false);
		this.repository.save(log);
	}

	@Override
	public void unbind(final ActivityLog log) {
		Dataset dataset;
		dataset = super.unbindObject(log, "draftMode", "typeOfIndicent", "description", "severityLevel");
		super.getResponse().addData(dataset);
	}

}
