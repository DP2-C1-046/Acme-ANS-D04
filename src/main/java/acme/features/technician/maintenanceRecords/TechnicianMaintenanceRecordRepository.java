
package acme.features.technician.maintenanceRecords;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.aircrafts.Aircraft;
import acme.entities.maintenanceRecords.Involves;
import acme.entities.maintenanceRecords.MaintenanceRecords;

@Repository
public interface TechnicianMaintenanceRecordRepository extends AbstractRepository {

	@Query("select m from MaintenanceRecords m where m.draftMode = false")
	Collection<MaintenanceRecords> findPublishedMaintenanceRecords();

	@Query("select m from MaintenanceRecords m where m.technicians.id = :techniciansId")
	Collection<MaintenanceRecords> findMaintenanceRecordsByTechnicianId(int techniciansId);

	@Query("select m from MaintenanceRecords m where m.id = :id")
	MaintenanceRecords findMaintenanceRecordsById(int id);

	@Query("select a from Aircraft a where a.id = :id")
	Aircraft findAircraftById(int id);

	@Query("select a from Aircraft a")
	Collection<Aircraft> findAvailableAircrafts();

	@Query("select i from Involves i where i.maintenanceRecords.id = :maintenanceRecordsId")
	Collection<Involves> findInvolvesFromMaintenanceRecordsId(int maintenanceRecordsId);

	@Query("select count(i.tasks) from Involves i where i.maintenanceRecords.id = :maintenanceRecordsId")
	int findTasksByMaintenanceRecordsId(int maintenanceRecordsId);

	@Query("select count(i.tasks) from Involves i where i.maintenanceRecords.id = :maintenanceRecordsId and i.tasks.draftMode = true")
	int findNotPublishedTasksByMaintenanceRecordsId(int maintenanceRecordsId);
}
