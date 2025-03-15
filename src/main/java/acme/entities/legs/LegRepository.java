
package acme.entities.legs;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.airports.Airport;

@Repository
public interface LegRepository extends AbstractRepository {

	@Query("SELECT COUNT(L) FROM Leg l WHERE l.flight.id =: flightId")
	Integer numberOfLayovers(@Param("flightId") int flightId);

	@Query("SELECT l.scheduledDeparture FROM Leg l WHERE l.flight.id = :flightId ORDER BY l.scheduledDeparture ASC")
	List<Date> findFirstScheduledDeparture(@Param("flightId") int flightId);

	@Query("SELECT l.scheduledArrival FROM Leg l WHERE l.flight.id = :flightId ORDER BY l.scheduledArrival DESC")
	List<Date> findLastScheduledArrival(@Param("flightId") int flightId);

	@Query("SELECT l.departureAirport FROM Leg l WHERE l.flight.id = :flightId ORDER BY l.scheduledDeparture ASC")
	List<Airport> findFirstAirport(@Param("flightId") int flightId);

	@Query("SELECT l.arrivalAirport FROM Leg l WHERE l.flight.id = :flightId ORDER BY l.scheduledArrival DESC")
	List<Airport> findLastAirport(@Param("flightId") int flightId);

}
