
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.claims.TrackingLog;
import acme.entities.claims.TrackingLogRepository;
import acme.entities.claims.TrackingLogStatus;

@Validator
public class TrackingLogValidator extends AbstractValidator<ValidTrackingLog, TrackingLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private TrackingLogRepository repository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidTrackingLog annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final TrackingLog trackingLog, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result = true;

		if (trackingLog == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");

		// Fetch previous tracking log for resolution comparison
		TrackingLog previousLog = this.repository.findLastTrackingLog(trackingLog.getClaim().getId());

		// Rule 1: If resolution is 100%, status must be ACCEPTED or REJECTED
		if (trackingLog.getResolution() == 100.0 && trackingLog.getStatus() == TrackingLogStatus.PENDING)
			super.state(context, false, "status", "acme.validation.trackinglog.invalid-final-status.message");

		// Rule 2: If status is ACCEPTED or REJECTED, resolution must be 100%
		if ((trackingLog.getStatus() == TrackingLogStatus.ACCEPTED || trackingLog.getStatus() == TrackingLogStatus.REJECTED) && trackingLog.getResolution() < 100.0)
			super.state(context, false, "resolution", "acme.validation.trackinglog.invalid-resolution.message");
		// Rule 3: Resolution percentage must be monotonically increasing
		if (previousLog != null && trackingLog.getResolution() < previousLog.getResolution())
			super.state(context, false, "resolution", "acme.validation.trackinglog.decreasing-resolution.message");

		// Rule 4: If status is ACCEPTED or REJECTED, resolutionReasonOrCompensation must be set
		if ((trackingLog.getStatus() == TrackingLogStatus.ACCEPTED || trackingLog.getStatus() == TrackingLogStatus.REJECTED) && (trackingLog.getResolutionReasonOrCompensation() == null || trackingLog.getResolutionReasonOrCompensation().trim().isEmpty()))
			super.state(context, false, "resolutionReasonOrCompensation", "acme.validation.trackinglog.resolution-reason-required.message");

		// Rule 5: Intermediate logs must remain in PENDING
		if (trackingLog.getResolution() < 100.0 && trackingLog.getStatus() != TrackingLogStatus.PENDING)
			super.state(context, false, "status", "acme.validation.trackinglog.invalid-intermediate-status.message");
		result = !super.hasErrors(context);

		return result;
	}
}
