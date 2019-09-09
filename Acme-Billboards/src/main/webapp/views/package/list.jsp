<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="${requestURI}" modelAttribute="packageFinderForm">
	
	<acme:textbox code="package.keyword" path="keyword"/>
	
	<acme:submit name="list" code="package.finder.search" />
	<acme:submit name="cancel" code="package.finder.cancel"/>
</form:form>
<br/> 

<display:table name="packages" id="row" pagesize="5" requestURI="${requestURI}">

		<display:column>
				<a href="package/show.do?packageId=${row.id}">   <spring:message code="package.show"   /></a> <br/>
		<security:authorize access="hasRole('MANAGER')">
			<jstl:if test="${row.isDraft==true}">
				<a href="package/manager/edit.do?packageId=${row.id}">   <spring:message code="package.edit"   /></a> <br/>
				<a href="package/manager/delete.do?packageId=${row.id}"> <spring:message code="package.delete" /></a> <br/>
			</jstl:if>
		</security:authorize>
		</display:column>

	<display:column titleKey="package.ticker"       property="ticker" />
	<display:column titleKey="package.title"        property="title"  />
	<display:column titleKey="package.description"  property="description" />
	
	<spring:message code="package.moment.format" var="formatMoment"/>
	<display:column titleKey="package.startMoment" property="startMoment" format="{0,date,${formatMoment}}"/>
	
	<spring:message code="package.moment.format" var="formatMoment"/>
	<display:column titleKey="package.endMoment"   property="endMoment"   format="{0,date,${formatMoment}}"/>
	
</display:table>

	<security:authorize access="hasRole('MANAGER')">
		<div>
			<a href="package/manager/create.do"> <spring:message code="package.create" /></a>
		</div>
	</security:authorize>