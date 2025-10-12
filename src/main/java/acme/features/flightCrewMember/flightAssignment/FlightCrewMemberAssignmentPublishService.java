
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
		FlightAssignment flightAssignment;
		FlightCrewMember flightCrewMember;

		masterId = super.getRequest().getData("id", int.class);
		flightAssignment = this.repository.findFlightAssignmentById(masterId);
		flightCrewMember = flightAssignment == null ? null : flightAssignment.getFlightCrewMember();
		status = flightAssignment != null && super.getRequest().getPrincipal().hasRealm(flightCrewMember);

		if (status) {
			String method;
			int legtId;

			method = super.getRequest().getMethod();
			if (method.equals("GET"))
				status = true;
			else {
				legtId = super.getRequest().getData("leg", int.class);
				Leg leg = this.repository.findLegById(legtId);
				Collection<Leg> uncompletedLegs = this.repository.findUncompletedLegs(MomentHelper.getCurrentMoment());
				status = legtId == 0 || uncompletedLegs.contains(leg);
			}
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		FlightAssignment flightAssignment;
		int id;

		id = super.getRequest().getData("id", int.class);
		flightAssignment = this.repository.findFlightAssignmentById(id);
		super.getBuffer().addData(flightAssignment);
	}

	@Override
	public void bind(final FlightAssignment assignment) {

		FlightCrewMember flightCrewMember;
		int legId;
		Leg leg;

		flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();
		legId = super.getRequest().getData("leg", int.class);
		leg = this.repository.findLegById(legId);

		super.bindObject(assignment, "flightCrewDuty", "assignmentStatus", "remarks");
		assignment.setFlightCrewMember(flightCrewMember);
		assignment.setLeg(leg);
		assignment.setLastUpdate(MomentHelper.getCurrentMoment());
	}

	@Override
	public void validate(final FlightAssignment assignment) {
		if (assignment.getAssignmentStatus() != null && assignment.getFlightCrewDuty() != null && assignment.getLeg() != null) {
			if (assignment.getFlightCrewDuty() == FlightCrewDuty.PILOT || assignment.getFlightCrewDuty() == FlightCrewDuty.COPILOT) {
				Collection<FlightAssignment> assignmentsOfLeg = this.repository.findPublishedFlightAssignmentsByLegId(assignment.getLeg().getId());
				for (FlightAssignment fa : assignmentsOfLeg)
					if (fa.getFlightCrewDuty() == FlightCrewDuty.PILOT && assignment.getFlightCrewDuty() == FlightCrewDuty.PILOT || fa.getFlightCrewDuty() == FlightCrewDuty.COPILOT && assignment.getFlightCrewDuty() == FlightCrewDuty.COPILOT) {
						super.state(false, "flightCrewDuty", "flight-crew-member.flight-assignment.validation.duty.publish");
						break;
					}
			}
			if (assignment.getFlightCrewMember().getCrewMemberStatus() != CrewMemberStatus.AVAILABLE)
				super.state(false, "flightCrewMember", "flight-crew-member.flight-assignment.validation.availability-status.publish");
			Collection<FlightAssignment> currentUserAssignments = this.repository.findPublishedUncompletedFlightAssignmentsByFlightCrewMemberId(MomentHelper.getCurrentMoment(), assignment.getFlightCrewMember().getId());
			for (FlightAssignment fa : currentUserAssignments)
				if (fa.getLeg().getFlightNumber() == assignment.getLeg().getFlightNumber()) {
					super.state(false, "leg", "flight-crew-member.flight-assignment.validation.flight-number.publish");
					break;
				}
			Date newDeparture = assignment.getLeg().getScheduledDeparture();
			Date newArrival = assignment.getLeg().getScheduledArrival();
			Collection<FlightAssignment> overlapping = this.repository.findOverlappingPublishedFlightAssignments(assignment.getFlightCrewMember().getId(), newDeparture, newArrival);
			if (!overlapping.isEmpty())
				super.state(false, "leg", "flight-crew-member.flight-assignment.validation.overlapping-leg.publish");
			if (assignment.getAssignmentStatus() == AssignmentStatus.PENDING)
				super.state(false, "assignmentStatus", "flight-crew-member.flight-assignment.validation.current-status.publish");
		}
		assert assignment != null;

	}

	@Override
	public void perform(final FlightAssignment assignment) {
		assignment.setDraftMode(false);
		this.repository.save(assignment);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		Collection<Leg> legs;
		SelectChoices legChoices;
		SelectChoices dutyChoices;
		SelectChoices statusChoices;
		Dataset dataset;
		FlightCrewMember flightCrewMember;
		flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

		legs = this.repository.findUncompletedLegs(MomentHelper.getCurrentMoment());

		if (!legs.contains(assignment.getLeg()))
			legChoices = SelectChoices.from(legs, "LegLabel", null);
		else
			legChoices = SelectChoices.from(legs, "LegLabel", assignment.getLeg());

		dutyChoices = SelectChoices.from(FlightCrewDuty.class, assignment.getFlightCrewDuty());
		statusChoices = SelectChoices.from(AssignmentStatus.class, assignment.getAssignmentStatus());

		dataset = super.unbindObject(assignment, "lastUpdate", "remarks", "draftMode");
		dataset.put("flightCrewMember", flightCrewMember.getIdentity().getFullName());
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);
		dataset.put("flightCrewDuty", dutyChoices.getSelected().getKey());
		dataset.put("duties", dutyChoices);
		dataset.put("currentStatus", statusChoices);

		super.getResponse().addData(dataset);
	}

}
