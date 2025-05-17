
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airlines.Airline;
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
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		FlightAssignment flightAssignment;
		FlightCrewMember flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

		flightAssignment = new FlightAssignment();
		flightAssignment.setFlightCrewMember(flightCrewMember);
		flightAssignment.setLastUpdate(MomentHelper.getCurrentMoment());
		flightAssignment.setDraftMode(true);
		flightAssignment.setAssignmentStatus(AssignmentStatus.PENDING);
		super.getBuffer().addData(flightAssignment);

	}

	@Override
	public void bind(final FlightAssignment flightAssignment) {
		int legId = super.getRequest().getData("leg", int.class);
		Leg leg = this.repository.findLegById(legId);
		flightAssignment.setLeg(leg);

		super.bindObject(flightAssignment, "flightCrewDuty", "assignmentStatus", "remarks");
	}

	@Override
	public void validate(final FlightAssignment flightAssignment) {
		super.state(flightAssignment.getAssignmentStatus() != AssignmentStatus.CANCELLED, "assignmentStatus", "acme.validation.flight-assignment.status-cancelled-not-allowed");
		super.state(flightAssignment.getAssignmentStatus() != AssignmentStatus.CONFIRMED, "assignmentStatus", "acme.validation.flight-assignment.status-confirmed-not-allowed");

		int memberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		FlightCrewMember member = this.repository.findFlightCrewMemberById(memberId);

		Leg leg = flightAssignment.getLeg();

		Airline legAirline = leg.getFlight().getAirline();
		Airline memberAirline = member.getAirline();

		boolean sameAirline = legAirline.getId() == memberAirline.getId();
		super.state(sameAirline, "leg", "acme.validation.flight-crew-member.assignment.form.error.different-airline", flightAssignment);
	}

	@Override
	public void perform(final FlightAssignment flightAssignment) {
		assert flightAssignment != null;
		this.repository.save(flightAssignment);
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
		dataset.put("dutyChoice", dutyChoice);
		dataset.put("currentStatusChoice", currentStatusChoice);
		dataset.put("legChoice", legChoice);

		dataset.put("flightCrewMember.identity.fullName", assignment.getFlightCrewMember().getIdentity().getFullName());
		super.getResponse().addData(dataset);
	}

}
