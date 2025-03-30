
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.assignments.FlightAssignment;
import acme.entities.legs.Leg;
import acme.entities.legs.LegStatus;
import acme.realms.FlightCrewMember;

@Repository
public interface FlightCrewMemberAssignmentRepository extends AbstractRepository {

	@Query("select f from FlightAssignment f where f.leg.legStatus = ?1 and f.flightCrewMember.id = ?2")
	Collection<FlightAssignment> assignmentsWithCompletedLegs(LegStatus legStatus, Integer member);

	@Query("select f from FlightAssignment f where f.leg.legStatus in ?1 and f.flightCrewMember.id = ?2")
	Collection<FlightAssignment> assignmentsWithPlannedLegs(Collection<LegStatus> statuses, Integer member);

	@Query("select f from FlightAssignment f where f.id = ?1")
	FlightAssignment findFlightAssignmentById(int id);

	@Query("select l from Leg l where l.id = ?1")
	Leg findLegById(int id);

	@Query("select f.leg from FlightAssignment f where f.flightCrewMember.id = ?1")
	Collection<Leg> findLegsByFlightCrewMemberId(int memberId);

	@Query("SELECT l FROM Leg l")
	Collection<Leg> findAllLegs();

	@Query("SELECT fcm FROM FlightCrewMember fcm")
	Collection<FlightCrewMember> findAllFlightCrewMembers();
}
