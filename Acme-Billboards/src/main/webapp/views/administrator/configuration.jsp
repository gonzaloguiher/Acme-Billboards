<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<security:authorize access="hasRole('ADMIN')">

	<form:form action="administrator/configuration.do" modelAttribute="configurationForm">

	<acme:textbox path="systemName"         code="administrator.systemName"/>
	<acme:textbox path="banner"             code="administrator.banner"/> 
	<acme:textbox path="finderCacheTime"    code="administrator.finderCacheTime"/>
	<acme:textbox path="vatPercentage"      code="administrator.vatPercentage"/>
	<acme:textbox path="defaultPhoneCode"   code="administrator.defaultPhoneCode"/>
	<acme:textbox path="finderQueryResults" code="administrator.finderQueryResults"/>
	<acme:textbox path="welcomeTextEnglish" code="administrator.welcomeTextEnglish"/>
	<acme:textbox path="welcomeTextSpanish" code="administrator.welcomeTextSpanish"/>
	
	<acme:submit name="save" code="administrator.save"/>
	<acme:cancel url="" code="administrator.cancel"/>			

</form:form>

</security:authorize>