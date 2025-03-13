// AdministratorDashboard.java

package acme.forms;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdministratorDashboard extends AbstractForm {

	// Serialisation version --------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// Attributes 
	// Airports
	private int					totalAirportsRegional;
	private int					totalAirportsDomestic;
	private int					totalAirportsInternational;

	// Airlines
	private int					numLuxuryAirlines;
	private int					numStandardAirlines;
	private int					numLowCostAirlines;
	private double				ratioAirlinesWithEmailAndPhone;

	// Aircrafts
	private double				ratioAircraftsInActiveService;
	private double				ratioAircraftsUnderMaintenance;

	// Reviews
	private double				ratioReviewsAboveFive;
	private int					countReviewsLast10Weeks;
	private double				avgReviewsLast10Weeks;
	private double				minReviewsLast10Weeks;
	private double				maxReviewsLast10Weeks;
	private double				stdDevReviewsLast10Weeks;
}
