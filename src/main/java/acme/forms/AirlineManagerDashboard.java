/*
 * AirlineManagerDashboard.java
 *
 */

package acme.forms;

import java.util.List;
import java.util.Map;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AirlineManagerDashboard extends AbstractForm {

	// Serialisation version --------------------------------------------------

	private static final long		serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	private Integer					totalAirlineManagers;
	private Double					averageYearsOfExperience;
	private Double					minYearsOfExperience;
	private Double					maxYearsOfExperience;
	private Double					stdDevYearsOfExperience;

	private Map<String, Integer>	managersByAirline;
	private List<String>			topFiveAirlinesWithMostManagers;

	// Ranking and retirement
	private Integer					experienceRanking;
	private Integer					yearsToRetirement;

	// Flight performance
	private Double					ratioOnTimeLegs;
	private Double					ratioDelayedLegs;

	// Popularity of airports
	private String					mostPopularAirport;
	private String					leastPopularAirport;

	// Flight leg statistics
	private Map<String, Integer>	flightLegsByStatus;

	// Flight cost statistics
	private Double					averageFlightCost;
	private Double					minFlightCost;
	private Double					maxFlightCost;
	private Double					stdDevFlightCost;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------
}
