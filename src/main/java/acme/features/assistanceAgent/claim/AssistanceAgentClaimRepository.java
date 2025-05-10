
package acme.features.assistanceAgent.claim;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claims.Claim;
import acme.entities.claims.TrackingLog;
import acme.entities.legs.Leg;

@Repository
public interface AssistanceAgentClaimRepository extends AbstractRepository {

	@Query("SELECT DISTINCT c FROM Claim c JOIN TrackingLog t ON t.claim.id = c.id WHERE t.lastUpdateMoment = (SELECT MAX(t2.lastUpdateMoment) FROM TrackingLog t2 WHERE t2.claim = c) AND (t.indicator != 'PENDING' AND c.assistanceAgents.id = :agentId)")
	Collection<Claim> findAllCompletedClaimsByAgentId(int agentId);

	@Query("SELECT c FROM Claim c WHERE c.id = :id")
	Claim findClaimById(int id);

	@Query("SELECT DISTINCT c FROM Claim c JOIN TrackingLog t ON t.claim.id = c.id WHERE t.lastUpdateMoment = (SELECT MAX(t2.lastUpdateMoment) FROM TrackingLog t2 WHERE t2.claim = c) AND (t.indicator = 'PENDING' AND c.assistanceAgents.id = :agentId)")
	Collection<Claim> findAllPendingClaimsByAgentId(int agentId);

	@Query("SELECT l FROM Leg l")
	Collection<Leg> findAllLeg();

	@Query("select l from Leg l where l.id = :id")
	Leg findLegById(int id);

	@Query("select tl FROM TrackingLog tl where tl.claim.id = :claimId")
	Collection<TrackingLog> findTrackingLogsByClaimId(int claimId);
}
