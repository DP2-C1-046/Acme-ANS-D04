<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code ="flight-crew-member.activity-log.list.label.registrationMoment" path ="registrationMoment" width ="20%"/>
	<acme:list-column code ="flight-crew-member.activity-log.list.label.typeOfIncident" path ="typeOfIndicent" width ="50%"/>
	<acme:list-column code ="flight-crew-member.activity-log.list.label.severityLevel" path ="severityLevel" width ="15%"/>
	<acme:list-column code="flight-crew-member.activity-log.list.label.draft-mode" path="draftMode" width="15%"/>
</acme:list>

<jstl:if test="${_command == 'list'}">
<acme:button code="flight-crew-member.activity-log.list.button.create" action ="/flight-crew-member/activity-log/create?masterId=${masterId}"/>
</jstl:if>