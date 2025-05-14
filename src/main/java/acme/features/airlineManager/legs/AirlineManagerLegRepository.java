
package acme.features.airlineManager.legs;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.aircrafts.Aircraft;
import acme.entities.airports.Airport;
import acme.entities.flights.Flight;
import acme.entities.legs.Leg;

@Repository
public interface AirlineManagerLegRepository extends AbstractRepository {

	@Query("SELECT l FROM Leg l")
	Collection<Leg> findAllLegs();

	@Query("SELECT l FROM Leg l WHERE l.id=:id")
	Leg findLegById(int id);

	@Query("SELECT l FROM Leg l WHERE l.flight.id=:flightId")
	Collection<Leg> findLegsByFlightId(int flightId);

	@Query("SELECT l FROM Leg l WHERE l.flight.airlineManager.id=:airlineManagerId")
	Collection<Leg> findLegsByAirlineManagerId(int airlineManagerId);

	@Query("SELECT a FROM Aircraft a")
	Collection<Aircraft> findAllAircrafts();

	@Query("SELECT a FROM Airport a")
	Collection<Airport> findAllAirports();

	@Query("SELECT f FROM Flight f WHERE f.airlineManager.id=:airlineManagerId")
	Collection<Flight> findFlightsByAirlineManagerId(int airlineManagerId);

	@Query("select a from Aircraft a where a.airline.id = :airlineId")
	Collection<Aircraft> findAllAircraftsByAirlineId(int airlineId);

}
