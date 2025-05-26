
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

	@Query("SELECT l FROM Leg l WHERE l.draftMode = false")
	Collection<Leg> findAllPublishedLegs();

	@Query("SELECT l FROM Leg l WHERE l.id=:id")
	Leg findLegById(int id);

	@Query("SELECT l FROM Leg l WHERE l.flight.id=:flightId")
	Collection<Leg> findLegsByFlightId(int flightId);

	@Query("SELECT l FROM Leg l WHERE l.flight.id=:flightId AND l.draftMode = false")
	Collection<Leg> findAllPublishedLegsByFlightId(int flightId);

	@Query("SELECT l FROM Leg l WHERE l.flight.airlineManager.id=:airlineManagerId")
	Collection<Leg> findLegsByAirlineManagerId(int airlineManagerId);

	@Query("SELECT a FROM Aircraft a")
	Collection<Aircraft> findAllAircrafts();

	@Query("SELECT a FROM Airport a")
	Collection<Airport> findAllAirports();

	@Query("SELECT f FROM Flight f WHERE f.id=:id")
	Flight findFlightById(int id);

	@Query("SELECT f FROM Flight f WHERE f.airlineManager.id=:airlineManagerId")
	Collection<Flight> findFlightsByAirlineManagerId(int airlineManagerId);

	@Query("SELECT a FROM Aircraft a WHERE a.airline.id=:airlineId")
	Collection<Aircraft> findAllAircraftsByAirlineId(int airlineId);

	@Query("SELECT a FROM Airport a WHERE a.id=:airportId")
	Airport findAirportById(int airportId);

	@Query("SELECT a FROM Aircraft a WHERE a.id=:aircraftId")
	Aircraft findAircraftById(int aircraftId);
}
