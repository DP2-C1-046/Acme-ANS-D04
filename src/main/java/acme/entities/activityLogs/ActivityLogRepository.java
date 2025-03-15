
package acme.entities.activityLogs;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.legs.Leg;

public interface ActivityLogRepository extends AbstractRepository {

	@Query("SELECT fa.leg FROM ActivityLog al JOIN al.flightAssignment fa WHERE al.id = :activityLogId")
	Leg findLegByActivityLogId(int activityLogId);

}
