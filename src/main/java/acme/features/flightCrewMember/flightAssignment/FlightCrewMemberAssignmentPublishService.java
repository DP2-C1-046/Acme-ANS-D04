
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.assignments.AssignmentStatus;
import acme.entities.assignments.FlightAssignment;
import acme.entities.assignments.FlightCrewDuty;
import acme.entities.legs.Leg;
import acme.realms.CrewMemberStatus;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightCrewMemberAssignmentPublishService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightCrewMemberAssignmentRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		FlightAssignment assignment;

		masterId = super.getRequest().getData("id", int.class);
		assignment = this.repository.findFlightAssignmentById(masterId);
		status = assignment.getDraftMode() && MomentHelper.isFuture(assignment.getLeg().getScheduledDeparture());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		FlightAssignment assignment;

		assignment = new FlightAssignment();
		super.getBuffer().addData(assignment);
	}

	@Override
	public void bind(final FlightAssignment assignment) {
		super.bindObject(assignment, "flightCrewDuty", "lastUpdate", "assignmentStatus", "remarks", "flightCrewMember", "leg", "draftMode");
	}

	@Override
	public void validate(final FlightAssignment assignment) {
		int flightCrewMemberId;
		int legId;

		boolean completedLeg;
		boolean availableMember;
		boolean hasSimultaneousLegs;
		boolean hasPilot;
		boolean hasCopilot;
		Date departure;
		Date arrival;
		Collection<Leg> simultaneousLegs;
		Collection<FlightAssignment> pilotAssignments;
		Collection<FlightAssignment> copilotAssignments;
		Leg leg;

		flightCrewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		leg = assignment.getLeg();
		legId = leg.getId();

		completedLeg = MomentHelper.isBefore(assignment.getLeg().getScheduledArrival(), MomentHelper.getCurrentMoment());
		availableMember = this.repository.findFlightCrewMemberById(flightCrewMemberId).getCrewMemberStatus().equals(CrewMemberStatus.AVAILABLE);

		hasSimultaneousLegs = false;
		departure = assignment.getLeg().getScheduledDeparture();
		arrival = assignment.getLeg().getScheduledArrival();
		simultaneousLegs = this.repository.findSimultaneousLegsByMemberId(departure, arrival, legId, flightCrewMemberId);
		if (simultaneousLegs.isEmpty())
			hasSimultaneousLegs = true;

		pilotAssignments = this.repository.findFlightAssignmentByLegAndDuty(leg, FlightCrewDuty.PILOT);
		copilotAssignments = this.repository.findFlightAssignmentByLegAndDuty(leg, FlightCrewDuty.COPILOT);

		hasPilot = true;
		hasCopilot = true;
		if (assignment.getFlightCrewDuty().equals(FlightCrewDuty.PILOT) && pilotAssignments.size() + 1 >= 2)
			hasPilot = false;
		if (assignment.getFlightCrewDuty().equals(FlightCrewDuty.COPILOT) && copilotAssignments.size() + 1 >= 2)
			hasCopilot = false;

		if (!this.getBuffer().getErrors().hasErrors("publish")) {
			super.state(!completedLeg, "leg", "acme.validation.flightassignment.leg.completed.message", assignment);
			super.state(availableMember, "flightCrewMember", "acme.validation.flightassignment.flightcrewmember.available.message", assignment);
			super.state(hasSimultaneousLegs, "leg", "acme.validation.flightassignment.leg.overlap.message", assignment);
			super.state(hasPilot, "flightCrewDuty", "acme.validation.flightassignment.duty.pilot.message", assignment);
			super.state(hasCopilot, "flightCrewDuty", "acme.validation.flightassignment.duty.copilot.message", assignment);
		}
	}

	@Override
	public void perform(final FlightAssignment assignment) {
		assignment.setDraftMode(false);
		this.repository.save(assignment);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		Dataset dataset;
		SelectChoices dutyChoice;
		SelectChoices currentStatusChoice;

		SelectChoices legChoice;
		Collection<Leg> legs;

		SelectChoices flightCrewMemberChoice;
		Collection<FlightCrewMember> flightCrewMembers;

		dutyChoice = SelectChoices.from(FlightCrewDuty.class, assignment.getFlightCrewDuty());
		currentStatusChoice = SelectChoices.from(AssignmentStatus.class, assignment.getAssignmentStatus());

		legs = this.repository.findAllLegs();
		legChoice = SelectChoices.from(legs, "id", assignment.getLeg());

		flightCrewMembers = this.repository.findAllFlightCrewMembers();
		flightCrewMemberChoice = SelectChoices.from(flightCrewMembers, "id", assignment.getFlightCrewMember());

		dataset = super.unbindObject(assignment, "flightCrewDuty", "lastUpdate", "assignmentStatus", "remarks", "flightCrewMember", "leg", "draftMode");
		dataset.put("dutyChoice", dutyChoice);
		dataset.put("currentStatusChoice", currentStatusChoice);
		dataset.put("legChoice", legChoice);
		dataset.put("flightCrewMemberChoice", flightCrewMemberChoice);

		super.getResponse().addData(dataset);
	}

}
