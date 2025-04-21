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
	<acme:input-textbox code="administrator.booking.list.label.customer.identity.fullName" path="booking.customer.identity.fullName" readonly="true"/>
	<acme:input-textbox code="administrator.booking-record.form.label.locatorCode" path="booking.locatorCode" readonly="true"/>
	<acme:input-select code="administrator.booking.form.label.travelClass" path="booking.travelClass" choices="${travelClasses}" readonly="true"/>
	<acme:input-money code="administrator.booking.form.label.price" path="booking.price" readonly="true"/>
	<acme:input-select code="administrator.booking-record.form.label.passenger" path="passenger" choices="${passengers}"
		readonly="${_command != 'create'}" />	
</acme:form>

