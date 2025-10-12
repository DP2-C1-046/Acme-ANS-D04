
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;

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
import acme.realms.FlightCrewMember;

@GuiService
public class FlightCrewMemberAssignmentUpdateService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

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
		status = flightAssignment != null && flightAssignment.getDraftMode() && super.getRequest().getPrincipal().hasRealm(flightCrewMember);

		if (status) {
			String method;
			int legId;
			method = super.getRequest().getMethod();
			if (method.equals("GET"))
				status = true;
			else {
				legId = super.getRequest().getData("leg", int.class);
				Leg leg = this.repository.findLegById(legId);
				Collection<Leg> uncompletedLegs = this.repository.findUncompletedLegs(MomentHelper.getCurrentMoment());
				status = legId == 0 || uncompletedLegs.contains(leg);
			}
		}

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

		int legId;
		Leg leg;

		legId = super.getRequest().getData("leg", int.class);
		leg = this.repository.findLegById(legId);
		assignment.setLeg(leg);
		assignment.setLastUpdate(MomentHelper.getCurrentMoment());

		super.bindObject(assignment, "flightCrewDuty", "assignmentStatus", "remarks");

	}

	@Override
	public void validate(final FlightAssignment assignment) {
		;
	}

	@Override
	public void perform(final FlightAssignment assignment) {
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
