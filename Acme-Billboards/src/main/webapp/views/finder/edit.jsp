<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="finder/filter.do" modelAttribute="finder">

	<form:hidden path="id"      />
	<form:hidden path="version" />
	
	<form:hidden path="moment"   />
    <form:hidden path="customer" />
	
	<acme:textbox code="finder.keyword" path="keyword"/>
	
	<acme:submit name="filter" code="finder.search" />
	<acme:submit name="clear"  code="finder.clear"  />

</form:form>
<br/>

<jstl:if test="${cachedMessage != null}">
	<p style="color: #3a3a3a"><spring:message code="${cachedMessage}" /></p>
	<p style="color: #3a3a3a"><spring:message code="finder.cachedMoment"/>
		<jstl:out value="${finder.moment}"/></p>
	<br/>
</jstl:if>
	
	<display:table name="packages" id="row" requestURI="finder/filter.do" pagesize="5">
		
		<display:column property="ticker" titleKey="package.ticker"/>
		<display:column property="title" titleKey="package.title"/>
		<display:column property="description" titleKey="package.description"/>

	</display:table>

