
package acme.forms;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TechnicianDashboard extends AbstractForm {

	// Serialisation version --------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// Maintenance Records
	private int					totalMaintenancePending;
	private int					totalMaintenanceInProgress;
	private int					totalMaintenanceCompleted;

	// Nearest Inspection Due Date
	private String				nearestInspectionDueDate;

	// Top Aircrafts with Most Maintenance Tasks
	private String				topAircraft1;
	private String				topAircraft2;
	private String				topAircraft3;
	private String				topAircraft4;
	private String				topAircraft5;

	// Maintenance Cost Statistics (Last Year)
	private double				avgMaintenanceCost;
	private double				minMaintenanceCost;
	private double				maxMaintenanceCost;
	private double				stdDevMaintenanceCost;

	// Task Duration Statistics (Tasks Assigned to Technician)
	private double				avgTaskDuration;
	private double				minTaskDuration;
	private double				maxTaskDuration;
	private double				stdDevTaskDuration;
}
