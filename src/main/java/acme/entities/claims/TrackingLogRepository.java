
package acme.entities.claims;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface TrackingLogRepository extends AbstractRepository {

	@Query("SELECT tl FROM TrackingLog tl WHERE tl.claim.id = :claimId ORDER BY tl.lastUpdate DESC")
	Optional<TrackingLog> findLastTrackingLog(int claimId);

	@Query("SELECT tl FROM TrackingLog tl WHERE tl.claim.id = :claimId ORDER BY tl.lastUpdate DESC")
	Optional<List<TrackingLog>> findOrderTrackingLog(Integer claimId);

	@Query("SELECT tl FROM TrackingLog tl WHERE tl.claim.id = :claimId")
	List<TrackingLog> findTrackingLogsByClaimId(int claimId);

}
