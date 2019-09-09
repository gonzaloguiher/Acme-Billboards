<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<security:authorize access="hasRole('ADMIN')">

<form:form action="actor/registerManager.do" modelAttribute="registerForm" id="form">

	<spring:message code="actor.userAccount"/> :<br>
	<acme:textbox code="actor.username" path="username"/> <br />
	
	<acme:password code="actor.password" path="password" /> <br />

	<acme:password code="actor.password2" path="password2" /> <br />
	
	<jstl:if test="${passMatch == false}">
		<div class="error">passwords do not match</div>
	</jstl:if>

	<spring:message code="actor.personalData"/><br />
	<acme:textbox code="actor.name" path="name"/> <br />
	
	<acme:textbox code="actor.middleName" path="middleName"/> <br />

	<acme:textbox code="actor.surname" path="surname"/> <br />
	
	<acme:textbox code="actor.email" path="email"/> <br />

	<acme:textbox code="actor.phone" path="phone"/> <br />
	
	<acme:textbox code="actor.photo" path="photo"/> <br />
	
	<acme:textbox code="actor.address" path="address"/> <br />
	
	<spring:message code="actor.warning"/>
	<a href="welcome/TOS.do"><spring:message code="actor.TOS"/></a> <br/>
	
	<acme:submit name="save" code="actor.submit"/>
	
	<acme:cancel url="/" code="actor.cancel"/>

</form:form>
</security:authorize>
