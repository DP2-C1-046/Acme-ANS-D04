
package acme.features.technician.involves;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.bookings.Booking;
import acme.entities.maintenanceRecords.Involves;
import acme.entities.maintenanceRecords.MaintenanceRecords;
import acme.entities.tasks.Tasks;

@Repository
public interface TechnicianInvolvedInRepository extends AbstractRepository {

	@Query("select b from Involves b where b.maintenanceRecords.technicians.id=:technicianId")
	Collection<Involves> findAllInvolvedInByTechnicianId(int technicianId);

	//ESTE NO SE USA¿¿?¿?¿?¿
	@Query("select b from Booking b where b.id=:id")
	Booking findBookingById(int id);

	//cambiado
	@Query("select b.maintenanceRecords from Involves b where b.id=?1")
	MaintenanceRecords findOneRecordByInvolvedIn(int id);

	//cambiado
	@Query("select b.tasks from Involves b where b.id=?1")
	Tasks findOneTaskByInvolvedIn(int id);

	//cambiado
	@Query("select b from Involves b where b.id=:id")
	Involves findInvolvedIn(int id);

	//encontrar las tasks de un tecnico
	@Query("select b from Tasks b where b.technicians.id=:id")
	Collection<Tasks> findTaskByTechnicianId(int id);

	//encontrar el record con el tecnico
	@Query("select b from MaintenanceRecords b where b.technicians.id=:id")
	Collection<MaintenanceRecords> findRecordByTechnicianId(int id);

	//CAMBIADO
	@Query("SELECT COUNT(b) > 0 FROM Involves b WHERE b.maintenanceRecords = :maintenanceRecords AND b.tasks = :tasks")
	boolean existsByRecordAndTask(@Param("maintenanceRecords") MaintenanceRecords maintenanceRecords, @Param("tasks") Tasks tasks);

	//CAMBIADO
	@Query("select b from MaintenanceRecords b where b.technicians.id=:id and b.draftMode=:draftMode")
	Collection<MaintenanceRecords> findNotPublishRecord(@Param("id") int id, @Param("draftMode") boolean draftMode);

}
