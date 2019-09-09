<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<security:authorize access="hasRole('MANAGER')">

	<form:form action="package/manager/edit.do" modelAttribute="pakage">

		<form:hidden path="id" />
		<form:hidden path="version" />
		<form:hidden path="ticker"  />
		<form:hidden path="manager" />
		
		<jstl:if test="${pakage.id == 0}">
		<form:hidden path="isDraft" />
		</jstl:if>

		<acme:textbox  code="package.title"       path="title" />
		<acme:textbox  code="package.description" path="description" />
		<acme:textbox  code="package.price"       path="price" />
		<acme:textarea code="package.photo"       path="photo" />
		<!-- <acme:textbox  code="package.startMoment" path="startMoment" /> --> 
		<!-- <acme:textbox  code="package.endMoment"   path="endMoment"   /> <br/> -->
		
		<spring:message code = "package.startMoment"/>: 
		<input type= "datetime-local" name="startMoment" placeholder="dd/MM/yyyy HH:mm">
		<div class="tooltip"><b>?</b> <span class="tooltiptext">Must be before the end moment</span> </div>
		<form:errors path="startMoment" cssClass="error" /><br>
	
		<spring:message code = "package.endMoment"/>: 
		<input type= "datetime-local" name="endMoment" placeholder="dd/MM/yyyy HH:mm">
		<div class="tooltip"><b>?</b> <span class="tooltiptext">Must be after the start moment</span> </div>
		<form:errors path="endMoment" cssClass="error" /><br>
		
		<jstl:if test="${pakage.id != 0}"> <acme:checkbox code="package.isDraft" path="isDraft"/> <br /> </jstl:if>
		
		<acme:submit code="package.save" name="save"/>
		
		<jstl:if test="${pakage.id != 0}"> <acme:submit code="package.delete" name="delete"/> </jstl:if>

		<acme:cancel url="package/manager/list.do" code="package.cancel"/>

	</form:form>
	
</security:authorize>
