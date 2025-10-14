
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
public class FlightCrewMemberAssignmentCreateService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightCrewMemberAssignmentRepository repository;


	@Override
	public void authorise() {
		boolean status;
		String method;
		int legId;
		method = super.getRequest().getMethod();
		if (method.equals("GET"))
			status = true;
		else {
			legId = super.getRequest().getData("leg", int.class);
			Leg leg = this.repository.findLegById(legId);
			int airlineId;
			FlightCrewMember flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();
			airlineId = flightCrewMember.getAirline().getId();
			Collection<Leg> uncompletedLegs = this.repository.findUncompletedLegs(MomentHelper.getCurrentMoment(), airlineId);
			status = legId == 0 || uncompletedLegs.contains(leg);
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		FlightAssignment flightAssignment = new FlightAssignment();
		flightAssignment.setDraftMode(true);
		super.getBuffer().addData(flightAssignment);

	}

	@Override
	public void bind(final FlightAssignment flightAssignment) {
		FlightCrewMember flightCrewMember;
		int legId;
		Leg leg;

		legId = super.getRequest().getData("leg", int.class);
		leg = this.repository.findLegById(legId);
		flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();
		flightAssignment.setLeg(leg);
		flightAssignment.setFlightCrewMember(flightCrewMember);
		flightAssignment.setLastUpdate(MomentHelper.getCurrentMoment());
		flightAssignment.setAssignmentStatus(AssignmentStatus.PENDING);
		super.bindObject(flightAssignment, "flightCrewDuty", "remarks");
	}

	@Override
	public void validate(final FlightAssignment flightAssignment) {
		;
	}

	@Override
	public void perform(final FlightAssignment flightAssignment) {
		this.repository.save(flightAssignment);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		Collection<Leg> legs;
		SelectChoices legChoices;
		SelectChoices dutyChoices;
		SelectChoices statusChoices;

		FlightCrewMember flightCrewMember;
		flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();
		int airlineId;
		airlineId = flightCrewMember.getAirline().getId();
		Dataset dataset;

		legs = this.repository.findUncompletedLegs(MomentHelper.getCurrentMoment(), airlineId);

		legChoices = SelectChoices.from(legs, "LegLabel", null);
		dutyChoices = SelectChoices.from(FlightCrewDuty.class, null);
		statusChoices = SelectChoices.from(AssignmentStatus.class, null);

		dataset = super.unbindObject(assignment, "draftMode");
		dataset.put("flightCrewMember", flightCrewMember.getIdentity().getFullName());
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);
		dataset.put("flightCrewDuty", dutyChoices.getSelected().getKey());
		dataset.put("duties", dutyChoices);
		dataset.put("currentStatus", statusChoices);

		super.getResponse().addData(dataset);
	}

}
