
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.Technicians;
import acme.realms.TechniciansRepository;

@Validator
public class TechniciansValidator extends AbstractValidator<ValidTechnicians, Technicians> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private TechniciansRepository repository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidTechnicians technician) {
		assert technician != null;
	}

	@Override
	public boolean isValid(final Technicians technician, final ConstraintValidatorContext context) {

		assert context != null;

		boolean result;

		{
			boolean uniqueTechnician;
			Technicians existingTechnician;

			existingTechnician = this.repository.findTechnicianByLicenseNumber(technician.getLicenseNumber());
			uniqueTechnician = existingTechnician == null || existingTechnician.equals(technician);

			super.state(context, uniqueTechnician, "licenseNumber", "acme.validation.technician.duplicated-license-number.message");

		}
		{
			boolean correctLicenseNubmer;

			char firstLetterName = technician.getIdentity().getName().charAt(0);
			char firstLetterSurname = technician.getIdentity().getSurname().charAt(0);

			correctLicenseNubmer = technician.getLicenseNumber().charAt(0) == firstLetterName && technician.getLicenseNumber().charAt(1) == firstLetterSurname;

			super.state(context, correctLicenseNubmer, "licenseNumber", "acme.validation.technician.wrong-initials-license-number.message");

		}
		result = !super.hasErrors(context);

		return result;
	}

}
