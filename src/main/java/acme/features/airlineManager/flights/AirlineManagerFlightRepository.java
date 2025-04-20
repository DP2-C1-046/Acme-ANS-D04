
package acme.features.airlineManager.flights;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flights.Flight;
import acme.entities.legs.Leg;

@Repository
public interface AirlineManagerFlightRepository extends AbstractRepository {

	@Query("select f from Flight f where f.airlineManager.id = :airlineManagerId")
	Collection<Flight> findAllFlightsByManagerId(int airlineManagerId);

	@Query("select f from Flight f where f.id = :id")
	Flight findFlightById(int id);

	@Query("select l from Leg l where l.flight.id = :flightId")
	Collection<Leg> findLegsByFlightId(@Param("flightId") int flightId);

}
