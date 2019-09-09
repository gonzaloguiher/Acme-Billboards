<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
		
	<display:table name="files" id="row" requestURI="file/list.do" pagesize="5">
		
	<security:authorize access="hasRole('MANAGER')">
		<jstl:if test="${row.contract.signManager == null}">
		<display:column>
			<a href="file/edit.do?fileId=${row.id}">   <spring:message code="file.edit" />  </a><br>
			<a href="file/delete.do?fileId=${row.id}"> <spring:message code="file.delete" /></a><br>
		</display:column>
		</jstl:if>
	</security:authorize>
		
    <display:column titleKey="file.location" property="location" />
    <display:column titleKey="file.image"    property="image" />
		    
	</display:table>