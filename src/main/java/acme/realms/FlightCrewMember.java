
package acme.realms;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractRole;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.entities.Airline;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FlightCrewMember extends AbstractRole {

	private static final long	serialVersionUID	= 1L;

	//Attributes -----------------------------------
	//	code (unique, pattern "^[A-Z]{2-3}\d{6}$", where the first two or three letters correspond to their initials),
	//a phone number (pattern "^\+?\d{6,15}$"), 
	//their language skills (final up to 255 characters), 
	//their availability status ("AVAILABLE", "ON VACATION", "ON LEAVE"), 
	//the airline they are working for, 
	//and their salary. 
	//Optionally, the system may store his or her years of experience. 
	@Mandatory
	@ValidString(pattern = "^[A-Z]{2,3}\\d{6}$")
	@Column(unique = true)
	private String				code;

	@Mandatory
	@Automapped
	@ValidString(pattern = "^\\+?\\d{6,15}$")
	private String				phoneNumber;

	@Mandatory
	@Automapped
	@ValidString
	private String				languageSkills;

	@Mandatory
	@Automapped
	private Status				status;

	@Mandatory
	@Automapped
	@ValidMoney
	private Money				salary;

	@Optional
	@ValidNumber
	private Integer				yearsOfExperience;

	//Relationships ----------------------------------
	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airline				airline;

}
