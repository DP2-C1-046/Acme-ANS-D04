/*
 * CustomerValidator.java
 *
 * Copyright (C) 2025 G3-C1.046
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.Customer;

@Validator
public class CustomerValidator extends AbstractValidator<ValidCustomer, Customer> {

	@Override
	protected void initialise(final ValidCustomer annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Customer customer, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (customer == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			String fullName = customer.getUserAccount().getIdentity().getFullName();
			String[] nameParts = fullName.split(", ");
			String initials = "";

			String[] surnameParts = nameParts[0].split(" ");
			initials = nameParts[1].substring(0, 1).toUpperCase();
			initials += surnameParts[0].substring(0, 1).toUpperCase();

			if (surnameParts.length > 1)
				initials += surnameParts[1].substring(0, 1).toUpperCase();

			boolean validIdentifier;

			String identifier = customer.getIdentifier();

			boolean validLength = identifier.length() >= 8 && identifier.length() <= 9;
			boolean validPattern = identifier.matches("^" + initials + "\\d{6}$");

			validIdentifier = validLength && validPattern;

			super.state(context, validIdentifier, "identifier", "acme.validation.customer.identifier.message");
		}

		result = !super.hasErrors(context);

		return result;
	}

}
