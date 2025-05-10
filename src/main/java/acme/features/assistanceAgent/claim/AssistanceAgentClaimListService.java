
package acme.features.assistanceAgent.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimListService extends AbstractGuiService<AssistanceAgent, Claim> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssistanceAgentClaimRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Claim> claims;
		int agentId;

		agentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		claims = this.repository.findAllCompletedClaimsByAgentId(agentId);

		super.getBuffer().addData(claims);
	}

	@Override
	public void unbind(final Claim claim) {
		Dataset dataset;
		Boolean indicator = claim.indicator();

		dataset = super.unbindObject(claim, "passengerEmail", "type");
		dataset.put("indicator", indicator);
		super.addPayload(dataset, claim, "registrationMoment", "description", "leg.flightNumber");

		super.getResponse().addData(dataset);
	}

}
