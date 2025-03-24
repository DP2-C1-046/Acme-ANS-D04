package acme.forms;

import java.util.Map;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssistanceAgentDashboard extends AbstractForm {

    // Serialisation version --------------------------------------------------

    private static final long serialVersionUID = 1L;

    // Attributes -------------------------------------------------------------

    // Ratios de reclamaciones resueltas y rechazadas
    private Double resolvedClaimsRatio;
    private Double rejectedClaimsRatio;

    // Top tres meses con más reclamaciones (clave: mes, valor: cantidad de reclamaciones)
    private Map<Integer, Long> topThreeMonthsByClaims;

    // Estadísticas sobre la cantidad de logs en sus reclamaciones
    private Integer countOfLogs;
    private Double averageLogsPerClaim;
    private Double minLogsPerClaim;
    private Double maxLogsPerClaim;
    private Double stdDevLogsPerClaim;

    // Estadísticas sobre la cantidad de reclamaciones asistidas en el último mes
    private Integer countOfClaimsLastMonth;
    private Double averageClaimsLastMonth;
    private Double minClaimsLastMonth;
    private Double maxClaimsLastMonth;
    private Double stdDevClaimsLastMonth;

    // Derived attributes -----------------------------------------------------

    // Relationships ----------------------------------------------------------

}
