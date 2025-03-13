
package acme.forms;

import java.util.List;
import java.util.Map;

import acme.client.components.basis.AbstractForm;
import acme.entities.assignments.AssignmentStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlightCrewMemberDashboard extends AbstractForm {

	private static final long					serialVersionUID	= 1L;

	private List<String>						lastFiveDestinations;

	private Integer								legsWithLowSeverityIncidents;

	private Integer								legsWithMidSeverityIncidents;

	private Integer								legsWithHighSeverityIncidents;

	private List<String>						crewMembersAssigned;

	private Map<AssignmentStatus, List<String>>	flightAssignmentsByStatus;

	private Double								averageFlightAssignments;

	private Integer								minimumFlightAssignments;

	private Integer								maximumFlightAssignments;

	private Double								deviationNumberOfFlightAssignments;

}
