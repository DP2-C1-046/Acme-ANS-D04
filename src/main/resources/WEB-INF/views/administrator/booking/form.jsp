<%--
- form.jsp
-
- Copyright (C) 2025 Rafael David Caro Medina
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
	<acme:input-textbox code="administrator.booking.form.label.customer.identity.fullName" path="customer.identity.fullName" readonly="true"/>
	
	<acme:input-textbox code="administrator.booking.form.label.locatorCode" path="locatorCode"/>
	
	<acme:input-select path="flight" code="administrator.booking.form.label.flight" choices="${flights}" />
	
	<acme:input-select path="travelClass" code="administrator.booking.form.label.travelClass" choices="${travelClasses}" />
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|publish')}">
			<acme:input-moment code="administrator.booking.form.label.purchaseMoment" path="purchaseMoment" readonly="true"/>
		</jstl:when>
	</jstl:choose>
	
	<acme:input-money code="administrator.booking.form.label.price" path="price"/>
	<acme:input-textbox code="administrator.booking.form.label.lastCardNibble" path="lastCardNibble"/>
		
	<jstl:choose>	 
		<jstl:when test="${_command == 'show' && draftMode == false}">
			<acme:button code="administrator.booking.form.button.passengers" action="/administrator/booking-record/list?masterId=${id}"/>			
		</jstl:when>				
	</jstl:choose>
</acme:form>
