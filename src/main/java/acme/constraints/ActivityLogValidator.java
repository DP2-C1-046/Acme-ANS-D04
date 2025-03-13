
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.activityLogs.ActivityLog;
import acme.entities.activityLogs.ActivityLogRepository;
import acme.entities.flights.Leg;
import acme.entities.flights.LegStatus;

@Validator
public class ActivityLogValidator extends AbstractValidator<ValidActivityLog, ActivityLog> {

	@Autowired
	private ActivityLogRepository repository;


	@Override
	protected void initialise(final ValidActivityLog annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final ActivityLog activityLog, final ConstraintValidatorContext context) {
		// Logs can be null
		assert context != null;

		boolean result;
		if (activityLog == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			boolean validLeg;
			Leg existingLeg;

			existingLeg = this.repository.findLegByActivityLogId(activityLog.getId());
			validLeg = existingLeg.getLegStatus() == LegStatus.LANDED;

			super.state(context, validLeg, "leg", "acme.validation.activityLog.invalidLeg");
		}
		result = !super.hasErrors(context);
		return result;
	}

}
