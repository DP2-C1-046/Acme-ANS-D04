
package acme.features.administrator.aircraft;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.aircrafts.Aircraft;
import acme.entities.airlines.Airline;

@Repository
public interface AdministratorAircraftRepository extends AbstractRepository {

	@Query("SELECT a FROM Airline a WHERE a.id = :id")
	Airline findAirlineById(int id);

	@Query("SELECT a FROM")
	Aircraft findAircraftByRegistrationNumber(String registrationNumber);

	@Query("SELECT a FROM Aircraft WHERE a.id=:id")
	Aircraft findAircraftById(int id);

	@Query("SELECT a FROM Airline a")
	Collection<Airline> findAllAirlines();

	@Query("SELECT a FROM Aircraft a")
	Collection<Aircraft> findAllAircrafts();

}
