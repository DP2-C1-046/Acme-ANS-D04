
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.activityLogs.ActivityLog;
import acme.entities.assignments.FlightAssignment;
import acme.entities.assignments.FlightCrewDuty;
import acme.entities.legs.Leg;
import acme.realms.FlightCrewMember;

@Repository
public interface FlightCrewMemberAssignmentRepository extends AbstractRepository {

	@Query("select f from FlightAssignment f where f.leg.scheduledArrival < CURRENT_TIMESTAMP and f.flightCrewMember.id = ?1")
	Collection<FlightAssignment> assignmentsWithCompletedLegs(Integer member);

	@Query("select f from FlightAssignment f where f.leg.scheduledDeparture > CURRENT_TIMESTAMP and f.flightCrewMember.id = ?1")
	Collection<FlightAssignment> assignmentsWithPlannedLegs(Integer member);

	@Query("select f from FlightAssignment f where f.flightCrewMember.id = ?1")
	Collection<FlightAssignment> findAllAssignmentsByMemberId(Integer member);

	@Query("select f from FlightAssignment f where f.id = ?1")
	FlightAssignment findFlightAssignmentById(int id);

	@Query("select l from Leg l where l.id = ?1")
	Leg findLegById(int id);

	@Query("select f.leg from FlightAssignment f where f.flightCrewMember.id = ?1")
	Collection<Leg> findLegsByFlightCrewMemberId(int memberId);

	@Query("SELECT l FROM Leg l WHERE l.draftMode = false AND l.flight.airline.id = :airlineId")
	Collection<Leg> findLegsByAirlineId(int airlineId);

	@Query("SELECT fcm FROM FlightCrewMember fcm")
	Collection<FlightCrewMember> findAllFlightCrewMembers();

	@Query("select al from ActivityLog al where al.flightAssignment.id = ?1")
	Collection<ActivityLog> findActivityLogsByAssignmentId(int id);

	@Query("SELECT fcm FROM FlightCrewMember fcm WHERE fcm.id = ?1")
	FlightCrewMember findFlightCrewMemberById(int flightCrewMemberId);

	@Query("SELECT fa.leg FROM FlightAssignment fa WHERE (fa.leg.scheduledDeparture < :arrival AND fa.leg.scheduledArrival > :departure) AND fa.leg.id <> :legId AND fa.flightCrewMember.id = :flightCrewMemberId")
	List<Leg> findSimultaneousLegsByMemberId(Date departure, Date arrival, int legId, int flightCrewMemberId);

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.leg = :flightAssignmentLeg and fa.flightCrewDuty = :duty")
	Collection<FlightAssignment> findFlightAssignmentByLegAndDuty(Leg flightAssignmentLeg, FlightCrewDuty duty);

	@Query("""
		select l
		from Leg l
		where l.scheduledDeparture > :now
		  and l.draftMode = false
		  and l.flight.airline.id = :airlineId
		order by l.scheduledDeparture asc
		""")
	Collection<Leg> findUncompletedLegs(Date now, int airlineId);

	@Query("select fa from FlightAssignment fa where fa.leg.id = :legId and fa.draftMode = false")
	Collection<FlightAssignment> findPublishedFlightAssignmentsByLegId(int legId);

	@Query("select fa from FlightAssignment fa where fa.leg.scheduledArrival > :now and fa.flightCrewMember.id = :flightCrewMemberId and fa.draftMode = false")
	Collection<FlightAssignment> findPublishedUncompletedFlightAssignmentsByFlightCrewMemberId(Date now, int flightCrewMemberId);

	@Query("select fa from FlightAssignment fa where fa.flightCrewMember.id = :memberId and fa.draftMode = false and fa.leg.scheduledDeparture < :newArrival and fa.leg.scheduledArrival > :newDeparture")
	Collection<FlightAssignment> findOverlappingPublishedFlightAssignments(int memberId, Date newDeparture, Date newArrival);

	@Query("""
		select f.airline.id
		from FlightCrewMember f
		where f.id = :flightCrewMemberId
		""")
	Integer findAirlineIdByFlightCrewMemberId(int flightCrewMemberId);

	@Query("""
		select fl.airline.id
		from Leg l
		  join l.flight fl
		where l.id = :legId
		""")
	Integer findAirlineIdByLegId(int legId);

}
