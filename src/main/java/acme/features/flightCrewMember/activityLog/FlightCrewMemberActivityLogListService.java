
package acme.features.flightCrewMember.activityLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
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
		boolean status;
		int masterId;
		FlightAssignment flightAssignment;
		masterId = super.getRequest().getData("masterId", int.class);
		flightAssignment = this.repository.findFlightAssignmentById(masterId);
		status = flightAssignment != null && super.getRequest().getPrincipal().hasRealm(flightAssignment.getFlightCrewMember());
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<ActivityLog> activityLogs;
		int flightAssignmentId;

		flightAssignmentId = super.getRequest().getData("masterId", int.class);

		activityLogs = this.repository.findActivityLogsByAssignmentId(flightAssignmentId);

		super.getBuffer().addData(activityLogs);
	}

	@Override
	public void unbind(final ActivityLog activityLog) {

		Dataset dataset;
		if (activityLog.getDraftMode() == true)
			dataset = super.unbindObject(activityLog, "typeOfIndicent", "description", "severityLevel", "draftMode");
		else
			dataset = super.unbindObject(activityLog, "registrationMoment", "typeOfIndicent", "description", "severityLevel", "draftMode");
		super.getResponse().addData(dataset);

	}

	@Override
	public void unbind(final Collection<ActivityLog> activityLog) {
		int masterId;
		masterId = super.getRequest().getData("masterId", int.class);
		super.getResponse().addGlobal("masterId", masterId);

	}
}
