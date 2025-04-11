<%--
- form.jsp
-
- Copyright (C) 2012-2025 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="airlineManager.leg.form.label.flightNumber" path="flightNumber"/>
	<acme:input-moment code="airlineManager.leg.form.label.scheduledDeparture" path="scheduledDeparture"/>
	<acme:input-moment code="airlineManager.leg.form.label.scheduledArrival" path="scheduledArrival"/>
	<acme:input-integer code="airlineManager.leg.form.label.duration" path="duration" 
	readonly="true" />
	<acme:input-select code="airlineManager.leg.form.label.legStatus" path="legStatus" choices="${statuses}"/>
	<acme:input-select code="airlineManager.leg.form.label.departureAirport" path="departureAirport" choices="${departureAirports}"/>
	<acme:input-select code="airlineManager.leg.form.label.arrivalAirport" path="arrivalAirport" choices="${arrivalAirports}"/>
	<acme:input-select code="airlineManager.leg.form.label.aircraft" path="aircraft" choices="${aircrafts}"/>
	<acme:input-select code="airlineManager.leg.form.label.flight" path="flight" choices="${flights}"/>
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
			<acme:submit code="airlineManager.leg.form.button.update" action="/airline-manager/leg/update"/>
			<acme:submit code="airlineManager.leg.form.button.delete" action="/airline-manager/leg/delete"/>
			<acme:submit code="airlineManager.leg.form.button.publish" action="/airline-manager/leg/publish"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="airlineManager.leg.form.button.create" action="/airline-manager/leg/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>