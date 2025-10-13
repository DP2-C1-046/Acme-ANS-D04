
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.assignments.FlightAssignment;
import acme.features.flightCrewMember.flightAssignment.FlightCrewMemberAssignmentRepository;

@Validator
public class FlightAssignmentValidator extends AbstractValidator<ValidFlightAssignment, FlightAssignment> {

	@Autowired
	private FlightCrewMemberAssignmentRepository repository;


	@Override
	protected void initialise(final ValidFlightAssignment annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final FlightAssignment fa, final ConstraintValidatorContext ctx) {
		assert ctx != null;

		if (fa == null) {
			super.state(ctx, false, "*", "javax.validation.constraints.NotNull.message");
			return false;
		}

		final boolean hasCrew = fa.getFlightCrewMember() != null;
		final boolean hasLeg = fa.getLeg() != null;

		super.state(ctx, hasCrew, "flightCrewMember", "javax.validation.constraints.NotNull.message");
		super.state(ctx, hasLeg, "leg", "javax.validation.constraints.NotNull.message");
		if (!hasCrew || !hasLeg)
			return !super.hasErrors(ctx);

		final int crewId = fa.getFlightCrewMember().getId();
		final int legId = fa.getLeg().getId();

		final Integer crewAirlineId = this.repository.findAirlineIdByFlightCrewMemberId(crewId);
		final Integer legAirlineId = this.repository.findAirlineIdByLegId(legId);

		super.state(ctx, crewAirlineId != null, "flightCrewMember", "acme.validation.flightassignment.airline.crew-unknown");
		super.state(ctx, legAirlineId != null, "leg", "acme.validation.flightassignment.airline.leg-unknown");

		if (crewAirlineId != null && legAirlineId != null)
			super.state(ctx, crewAirlineId.equals(legAirlineId), "leg", "acme.validation.flightassignment.airline.mismatch");

		return !super.hasErrors(ctx);

	}

}
