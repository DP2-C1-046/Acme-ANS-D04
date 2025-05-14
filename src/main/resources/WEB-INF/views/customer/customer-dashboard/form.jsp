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

<h2>
	<acme:print code="customer.dashboard.form.title.general-indicators"/>
</h2>

<table class="table table-sm">
	<tr>
		<th scope="row">			
			<span style="text-align: right;" >
				<acme:print code="customer.dashboard.form.label.lastFiveDestinations"/>
			</span>			
		</th>
		<td>
			<acme:print value="${lastFiveDestinations}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<span style="text-align: right; display: inline-block; min-width:50px; font-variant-numeric: tabular-nums;">
				<acme:print code="customer.dashboard.form.label.moneySpentInBookingsLastYear"/>
			</span>
		</th>
		<td>
			<acme:print value="${moneySpentInBookingsLastYear}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<span style="text-align: right; display: inline-block; min-width:50px; font-variant-numeric: tabular-nums;">
				<acme:print code="customer.dashboard.form.label.bookingsInTravelClassECONOMY"/>
			</span>
		</th>
		<td>
			<acme:print value="${bookingsInTravelClassECONOMY}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<span style="text-align: right; display: inline-block; min-width:50px; font-variant-numeric: tabular-nums;">
				<acme:print code="customer.dashboard.form.label.bookingsInTravelClassBUSINESS"/>
			</span>
		</th>
		<td>
			<acme:print value="${bookingsInTravelClassBUSINESS}"/>
		</td>
	</tr>
	<tr><td>&nbsp;</td></tr>
	<tr>
		<th>		
			<h5><acme:print code="customer.dashboard.form.title.last-five-years"/></h5>
		</th>		
	</tr>	
	<tr>		
		<th scope="row">
			<acme:print code="customer.dashboard.form.label.countOfBookingsL5Y"/>
		</th>
		<td>
			<span style="text-align: right; display: inline-block; min-width:30px; font-variant-numeric: tabular-nums;">
				<acme:print value="${countOfBookingsL5Y}"/>
			</span>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="customer.dashboard.form.label.averageBookingsCostL5Y"/>
		</th>
		<td>
			<span style="text-align: right; display: inline-block; min-width:50px; font-variant-numeric: tabular-nums;">
				<acme:print value="${averageBookingsCostL5Y}"/>
			</span>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="customer.dashboard.form.label.minBookingsCostL5Y"/>
		</th>
		<td>
			<span style="text-align: right; display: inline-block; min-width:50px; font-variant-numeric: tabular-nums;">
				<acme:print value="${minBookingsCostL5Y}"/>
			</span>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="customer.dashboard.form.label.maxBookingsCostL5Y"/>
		</th>
		<td>
			<span style="text-align: right; display: inline-block; min-width:50px; font-variant-numeric: tabular-nums;">
				<acme:print value="${maxBookingsCostL5Y}"/>
			</span>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="customer.dashboard.form.label.stdDesvBookingsCostL5Y"/>
		</th>
		<td>
			<span style="text-align: right; display: inline-block; min-width:50px; font-variant-numeric: tabular-nums;">
				<acme:print value="${stdDesvBookingsCostL5Y}"/>
			</span>
		</td>
	</tr>	
	<tr><td>&nbsp;</td></tr>
	<tr>
		<th>		
			<h5><acme:print code="customer.dashboard.form.title.numbers-of-passengers"/></h5>
		</th>		
	</tr>
	<tr>		
		<th scope="row">
			<acme:print code="customer.dashboard.form.label.countOfPassengers"/>
		</th>
		<td>
			<span style="text-align: right; display: inline-block; min-width:30px; font-variant-numeric: tabular-nums;">
				<acme:print value="${countOfPassengers}"/>
			</span>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="customer.dashboard.form.label.averagePassengerNumber"/>
		</th>
		<td>
			<span style="text-align: right; display: inline-block; min-width:50px; font-variant-numeric: tabular-nums;">
				<acme:print value="${averagePassengerNumber}"/>
			</span>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="customer.dashboard.form.label.minPassengerNumber"/>
		</th>
		<td>
			<span style="text-align: right; display: inline-block; min-width:50px; font-variant-numeric: tabular-nums;">
				<acme:print value="${minPassengerNumber}"/>
			</span>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="customer.dashboard.form.label.maxPassengerNumber"/>
		</th>
		<td>
			<span style="text-align: right; display: inline-block; min-width:50px; font-variant-numeric: tabular-nums;">
				<acme:print value="${maxPassengerNumber}"/>
			</span>
		</td>
	</tr>	
</table>

<acme:return/>

