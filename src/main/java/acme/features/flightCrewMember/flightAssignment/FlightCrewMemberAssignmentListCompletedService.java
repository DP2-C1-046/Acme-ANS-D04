
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.assignments.FlightAssignment;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightCrewMemberAssignmentListCompletedService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightCrewMemberAssignmentRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {

		int memberId = super.getRequest().getPrincipal().getActiveRealm().getId();

		//Collection<FlightAssignment> assignments = this.repository.assignmentsWithCompletedLegs(memberId);
		Collection<FlightAssignment> allAssignments = this.repository.findAllAssignmentsByMemberId(memberId);

		Date now = MomentHelper.getCurrentMoment();
		Collection<FlightAssignment> completedAssignments = allAssignments.stream().filter(fa -> fa.getLeg().getScheduledArrival().before(now)).collect(Collectors.toList());
		//super.getBuffer().addData(assignments);
		super.getBuffer().addData(completedAssignments);

	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		Dataset dataset;

		dataset = super.unbindObject(assignment, "flightCrewDuty", "lastUpdate", "assignmentStatus");
		super.addPayload(dataset, assignment, "remarks", "draftMode", "flightCrewMember.identity.fullName", "leg.legStatus");

		super.getResponse().addData(dataset);
	}

}
