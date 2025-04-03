
package acme.features.technician.tasks;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.maintenanceRecords.Involves;
import acme.entities.maintenanceRecords.MaintenanceRecords;
import acme.entities.tasks.Tasks;
import acme.realms.Technicians;

@Repository
public interface TechnicianTaskRepository extends AbstractRepository {

	@Query("select t from Tasks t where t.technicians.id=:technicianId")
	Collection<Task> findTasksByTechnicianId(@Param("technicianId") int technicianId);

	@Query("select br from Involves br where br.tasks.id=:id")
	Collection<Involves> findAllInvolvedInById(int id);

	@Query("Select c from Technicians c where c.id=:id")
	Technicians findTechnicianById(final int id);

	@Query("select b.tasks from Involves b WHERE b.maintenanceRecords.id = :recordId and b.tasks.technicians.id =:id")
	Collection<Tasks> findTasksByTechId(@Param("recordId") int recordId, @Param("id") int id);

	@Query("SELECT b from Tasks b WHERE b.id=:id")
	Tasks findTaskById(final int id);

	@Query("SELECT b from MaintenanceRecords b WHERE b.id=:id")
	MaintenanceRecords findRecordById(final int id);

}
