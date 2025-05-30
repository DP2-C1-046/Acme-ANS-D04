
package acme.features.assistanceAgent.trackingLog;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.TrackingLog;
import acme.entities.claims.TrackingLogStatus;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogPublishService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;


	@Override
	public void authorise() {
		boolean status;
		TrackingLog trackingLog;
		int id;
		AssistanceAgent assistanceAgent;

		id = super.getRequest().getData("id", int.class);
		trackingLog = this.repository.findTrackingLogById(id);
		assistanceAgent = trackingLog == null ? null : trackingLog.getClaim().getAssistanceAgent();
		status = super.getRequest().getPrincipal().hasRealm(assistanceAgent);

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		TrackingLog trackingLog;
		int id;

		id = super.getRequest().getData("id", int.class);
		trackingLog = this.repository.findTrackingLogById(id);

		super.getBuffer().addData(trackingLog);
	}

	@Override
	public void bind(final TrackingLog trackingLog) {
		super.bindObject(trackingLog, "lastUpdateMoment", "step", "resolutionPercentage", "status", "resolution");

	}

	@Override
	public void validate(final TrackingLog trackingLog) {
		boolean valid;
		// Verificar si la Claim asociada está publicada
		valid = trackingLog.getClaim() != null && !trackingLog.getClaim().isDraftMode();
		super.state(valid, "*", "assistanceAgent.trackingLog.form.error.claimNotPublished");

		if (!valid)
			return; // Si la Claim no está publicada, no continuar con otras validaciones
		if (trackingLog.getResolutionPercentage() != null && trackingLog.getResolutionPercentage() != null && trackingLog.getStatus() != null && trackingLog.getResolutionPercentage() < 100.0) {
			valid = trackingLog.getStatus().equals(TrackingLogStatus.PENDING);
			super.state(valid, "status", "assistanceAgent.trackingLog.form.error.badStatus");
		} else if (trackingLog.getStatus() != null) {
			valid = !trackingLog.getStatus().equals(TrackingLogStatus.PENDING);
			super.state(valid, "status", "assistanceAgent.trackingLog.form.error.badStatus2");
		}
		if (trackingLog.getStatus() != null && trackingLog.getStatus().equals(TrackingLogStatus.PENDING)) {
			valid = trackingLog.getResolution() == null || trackingLog.getResolution().isBlank();
			super.state(valid, "resolution", "assistanceAgent.trackingLog.form.error.badResolution");
		} else {
			valid = trackingLog.getResolution() != null && !trackingLog.getResolution().isBlank();
			super.state(valid, "resolution", "assistanceAgent.trackingLog.form.error.badResolution2");
		}
		TrackingLog highestTrackingLog;
		Optional<List<TrackingLog>> trackingLogs = this.repository.findOrderTrackingLogPublished(trackingLog.getClaim().getId());
		if (trackingLogs.isPresent() && trackingLogs.get().size() > 0) {
			highestTrackingLog = trackingLogs.get().get(0);
			long completedTrackingLogs = trackingLogs.get().stream().filter(t -> t.getResolutionPercentage() == 100).count();
			if (highestTrackingLog.getId() != trackingLog.getId())
				if (highestTrackingLog.getResolutionPercentage() == 100 && trackingLog.getResolutionPercentage() == 100) {
					valid = !highestTrackingLog.isDraftMode() && completedTrackingLogs < 2;
					super.state(valid, "resolutionPercentage", "assistanceAgent.trackingLog.form.error.maxcompleted");
				} else {
					valid = highestTrackingLog.getResolutionPercentage() < trackingLog.getResolutionPercentage();
					super.state(valid, "resolutionPercentage", "assistanceAgent.trackingLog.form.error.badPercentage");
				}
		}

	}

	@Override
	public void perform(final TrackingLog trackingLog) {
		trackingLog.setDraftMode(false);
		this.repository.save(trackingLog);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {

		SelectChoices statusChoices;

		Dataset dataset;

		statusChoices = SelectChoices.from(TrackingLogStatus.class, trackingLog.getStatus());

		dataset = super.unbindObject(trackingLog, "lastUpdateMoment", "step", "resolutionPercentage", "status", "resolution", "draftMode");
		dataset.put("statusChoices", statusChoices);

		super.getResponse().addData(dataset);

	}

}
