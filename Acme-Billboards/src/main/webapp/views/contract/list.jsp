<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<security:authorize access="hasRole('MANAGER')">

<display:table name="contracts" id="row" pagesize="5" requestURI="contract/manager/list.do">

	<security:authorize access="hasRole('MANAGER')">
		<display:column>
			<a href="contract/manager/show.do?contractId=${row.id}">   <spring:message code="contract.show"   /></a> <br/>
			<jstl:if test="${row.signManager == null}">
			<a href="contract/manager/sign.do?contractId=${row.id}">   <spring:message code="contract.sign"   /></a> <br/>
			<a href="contract/manager/edit.do?contractId=${row.id}">   <spring:message code="contract.edit"   /></a> <br/>
			</jstl:if>
		</display:column>
	</security:authorize>

	<display:column titleKey="contract.text" property="text" />
	<display:column titleKey="contract.hash" property="hash" />
	
	<spring:message code="contract.moment.format" var="formatMoment"/>
	<display:column titleKey="contract.momentCustomer" property="momentCustomer" format="{0,date,${formatMoment}}"/>
	
	<spring:message code="contract.moment.format" var="formatMoment"/>
	<display:column titleKey="contract.momentManager"  property="momentManager"  format="{0,date,${formatMoment}}"/>
	
</display:table>

</security:authorize>

<security:authorize access="hasRole('CUSTOMER')">

<display:table name="contracts" id="row" pagesize="5" requestURI="contract/customer/list.do">

	<security:authorize access="hasRole('CUSTOMER')">
		<display:column class ="${row.status}">
			<a href="contract/customer/show.do?contractId=${row.id}">   <spring:message code="contract.show"   /></a> <br/>
			<jstl:if test="${row.signCustomer == null}">
			<a href="contract/customer/sign.do?contractId=${row.id}">   <spring:message code="contract.sign"   /></a> <br/>
			</jstl:if>
		</display:column>
	</security:authorize>

	<display:column titleKey="contract.text" property="text" class ="${row.status}" />
	<display:column titleKey="contract.hash" property="hash" class ="${row.status}" />
	
	<spring:message code="contract.moment.format" var="formatMoment"/>
	<display:column titleKey="contract.momentCustomer" property="momentCustomer" format="{0,date,${formatMoment}}" class ="${row.status}" />
	
	<spring:message code="contract.moment.format" var="formatMoment"/>
	<display:column titleKey="contract.momentManager"  property="momentManager"  format="{0,date,${formatMoment}}" class ="${row.status}" />
	
</display:table>

</security:authorize>