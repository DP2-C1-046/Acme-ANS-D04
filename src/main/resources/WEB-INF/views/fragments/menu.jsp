<%--
- menu.jsp
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
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:menu-bar>
	<acme:menu-left>
		<acme:menu-option code="master.menu.anonymous" access="isAnonymous()">
			<acme:menu-suboption code="master.menu.anonymous.david-blanco"
				action="https://www.fcbarcelona.es/es/" />
			<acme:menu-suboption code="master.menu.anonymous.nerea-jimenez"
				action="https://www.youtube.com/" />
			<acme:menu-suboption code="master.menu.anonymous.rafael-segura"
				action="http://www.twitch.tv/" />
			<acme:menu-suboption code="master.menu.anonymous.rafael-david-caro"
				action="http://www.elmundo.es/" />
			<acme:menu-suboption code="master.menu.anonymous.emilio-espinosa"
				action="https://www.elmundotoday.com/" />
		</acme:menu-option>

		<acme:menu-option code="master.menu.administrator"
			access="hasRealm('Administrator')">
			<acme:menu-suboption
				code="master.menu.administrator.list-user-accounts"
				action="/administrator/user-account/list" />			
			<acme:menu-suboption code="master.menu.administrator.list-aircrafts"
				action="/administrator/aircraft/list" />
			<acme:menu-suboption code="master.menu.administrator.list-bookings"
				action="/administrator/booking/list" />
			<acme:menu-separator />		
			<acme:menu-suboption
				code="master.menu.administrator.populate-db-initial"
				action="/administrator/system/populate-initial" />
			<acme:menu-suboption
				code="master.menu.administrator.populate-db-sample"
				action="/administrator/system/populate-sample" />
			<acme:menu-separator />
			<acme:menu-suboption
				code="master.menu.administrator.shut-system-down"
				action="/administrator/system/shut-down" />
		</acme:menu-option>

		<acme:menu-option code="master.menu.provider"
			access="hasRealm('Provider')">
			<acme:menu-suboption code="master.menu.provider.favourite-link"
				action="http://www.example.com/" />
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.airlineManager" access="hasRealm('AirlineManager')">
			<acme:menu-suboption code="master.menu.airlineManager.list-my-flights" action="/airline-manager/flight/list" />
			<acme:menu-suboption code="master.menu.airlineManager.list-my-legs" action="/airline-manager/leg/list" />
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.assistanceAgent" access="hasRealm('AssistanceAgent')">
			<acme:menu-suboption code="master.menu.assistanceAgent.list-claims" action="/assistance-agent/claim/list"/>			
			<acme:menu-suboption code="master.menu.assistanceAgent.list-claims-pending" action="/assistance-agent/claim/pending"/>	
			<acme:menu-suboption code="master.menu.assistanceAgent.show-dashboard" action="/assistance-agent/assistance-agent-dashboard/show"/>	
		</acme:menu-option>

		<acme:menu-option code="master.menu.flightCrewMember" access="hasRealm('FlightCrewMember')">
			<acme:menu-suboption code="master.menu.flightCrewMember.listCompleted" action="/flight-crew-member/flight-assignment/list"/>
			<acme:menu-suboption code="master.menu.flightCrewMember.listPlanned" action="/flight-crew-member/flight-assignment/list-planned"/>
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.consumer" access="hasRealm('Consumer')">
			<acme:menu-suboption code="master.menu.consumer.favourite-link" action="http://www.example.com/"/>
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.customer" access="hasRealm('Customer')">
			<acme:menu-suboption code="master.menu.customer.list-bookings" action="/customer/booking/list"/>
			<acme:menu-suboption code="master.menu.customer.list-passengers" action="/customer/passenger/list"/>
			<acme:menu-suboption code="master.menu.customer.list-all-passengers" action="/customer/passenger/list?mine=true"/>
			<acme:menu-separator />
			<acme:menu-suboption code="master.menu.customer.show-dashboard" action="/customer/customer-dashboard/show"/>
		</acme:menu-option>
	</acme:menu-left>

	<acme:menu-right>		
		<acme:menu-option code="master.menu.user-account" access="isAuthenticated()">
			<acme:menu-suboption code="master.menu.user-account.general-profile" action="/authenticated/user-account/update"/>
			<acme:menu-suboption code="master.menu.user-account.become-provider" action="/authenticated/provider/create" access="!hasRealm('Provider')"/>
			<acme:menu-suboption code="master.menu.user-account.provider-profile" action="/authenticated/provider/update" access="hasRealm('Provider')"/>
			<acme:menu-suboption code="master.menu.user-account.become-consumer" action="/authenticated/consumer/create" access="!hasRealm('Consumer')"/>
			<acme:menu-suboption code="master.menu.user-account.consumer-profile" action="/authenticated/consumer/update" access="hasRealm('Consumer')"/>
			<acme:menu-suboption code="master.menu.user-account.become-customer" action="/authenticated/customer/create" access="!hasRealm('Customer')"/>
			<acme:menu-suboption code="master.menu.user-account.customer-profile" action="/authenticated/customer/update" access="hasRealm('Customer')"/>
		</acme:menu-option>
	</acme:menu-right>
</acme:menu-bar>

