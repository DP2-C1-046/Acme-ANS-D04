
package acme.features.flightCrewMember.flightAssignment;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.assignments.FlightAssignment;
import acme.realms.FlightCrewMember;

@GuiController
public class FlightCrewMemberAssignmentController extends AbstractGuiController<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightCrewMemberAssignmentListCompletedService	listCompletedService;

	@Autowired
	private FlightCrewMemberAssignmentListPlannedService	listPlannedService;

	@Autowired
	private FlightCrewMemberAssignmentShowService			showService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listCompletedService);
		super.addCustomCommand("list-planned", "list", this.listPlannedService);
		super.addBasicCommand("show", this.showService);
	}

}
