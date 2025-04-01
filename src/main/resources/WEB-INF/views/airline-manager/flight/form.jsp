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

<acme:form readonly="${readonly}">
	<acme:input-textbox code="airlineManager.flight.form.label.tag" path="tag"/>
	<acme:input-checkbox code="airlineManager.flight.form.label.needsSelfTransfer" path="needsSelfTransfer"/>
	<acme:input-money code="airlineManager.flight.form.label.cost" path="cost"/>
	<acme:input-textbox code="airlineManager.flight.form.label.description" path="description"/>
	
	
	<jstl:choose>
		<jstl:when test="${_command == 'show' && draftMode == false}">
					<acme:button code="airlineManager.flight.form.button.legs" action="/airline-manager/legs/list-flight?masterId=${id}"/>			
				</jstl:when>
				<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish')  && draftMode == true}">
					<acme:submit code="airlineManager.flight.form.button.update" action="/airline-manager/flight/update"/>
					<acme:submit code="airlineManager.flight.form.button.delete" action="/airline-manager/flight/delete"/>
					<acme:submit code="airlineManager.flight.form.button.publish" action="/airline-manager/flight/publish"/>
				</jstl:when>
				<jstl:when test="${_command == 'create'}">
					<acme:submit code="airlineManager.flight.form.button.create" action="/airline-manager/flight/create"/>
				</jstl:when>		
	</jstl:choose>
</acme:form>
