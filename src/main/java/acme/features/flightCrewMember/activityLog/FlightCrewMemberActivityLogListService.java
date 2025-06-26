
package acme.features.flightCrewMember.activityLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLogs.ActivityLog;
import acme.entities.assignments.FlightAssignment;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightCrewMemberActivityLogListService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;


	@Override
	public void authorise() {
		int masterId;
		int memberId;
		FlightAssignment flightAssignment;
		boolean status;

		masterId = super.getRequest().getData("masterId", int.class);
		memberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		flightAssignment = this.repository.findFlightAssignmentById(masterId);

		status = flightAssignment != null && flightAssignment.getFlightCrewMember().getId() == memberId && flightAssignment.getLeg().getScheduledArrival().before(MomentHelper.getCurrentMoment()) && !flightAssignment.getDraftMode();
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<ActivityLog> logs;
		int masterId;

		masterId = super.getRequest().getData("masterId", int.class);
		logs = this.repository.findActivityLogsByAssignmentId(masterId);
		super.getBuffer().addData(logs);
	}

	@Override
	public void unbind(final ActivityLog activityLog) {
		Dataset dataset;
		int masterId;

		dataset = super.unbindObject(activityLog, "registrationMoment", "typeOfIndicent", "severityLevel");
		masterId = super.getRequest().getData("masterId", int.class);

		super.addPayload(dataset, activityLog, "draftMode");
		super.getResponse().addGlobal("masterId", masterId);
		super.getResponse().addData(dataset);
	}
}
