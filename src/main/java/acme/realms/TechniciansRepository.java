
package acme.realms;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface TechniciansRepository extends AbstractRepository {

	@Query("select t from Technicians t where t.licenseNumber = :licenseNumber")
	Technicians findTechnicianByLicenseNumber(String licenseNumber);

}
