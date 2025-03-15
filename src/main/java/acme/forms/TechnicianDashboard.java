
package acme.forms;

import java.util.List;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TechnicianDashboard extends AbstractForm {

	// Serialisation version --------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// Maintenance Records
	private Integer				totalMaintenancePending;
	private Integer				totalMaintenanceInProgress;
	private Integer				totalMaintenanceCompleted;

	// Nearest Inspection Due Date
	private String				nearestInspectionDueDate;

	// Top 5 Aircrafts with Most Maintenance Tasks
	private List<String>		top5AircraftMostMaintenanceTasks;

	// Maintenance Cost Statistics (Last Year)
	private double				avgMaintenanceCost;
	private Integer				minMaintenanceCost;
	private Integer				maxMaintenanceCost;
	private double				stdDevMaintenanceCost;

	// Task Duration Statistics (Tasks Assigned to Technician)
	private double				avgTaskDuration;
	private Integer				minTaskDuration;
	private Integer				maxTaskDuration;
	private double				stdDevTaskDuration;
}
