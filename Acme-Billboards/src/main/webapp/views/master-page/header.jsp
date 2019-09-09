<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

	<div class="page-header" style="background: url(${banner}) no-repeat; "  onclick="location.href=''"></div>

<div>
	<ul id="jMenu">
		<security:authorize access="hasRole('ADMIN')">
			<li><a class="fNiv"><spring:message	code="master.page.package" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="package/list.do" ><spring:message code="master.page.package.list"  /></a></li>
				</ul>
			</li>
			<li><a class="fNiv"><spring:message	code="master.page.system" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="administrator/configuration.do"><spring:message code="master.page.actor.configuration" /></a></li>
					<li><a href="administrator/dashboard.do">    <spring:message code="master.page.actor.dashboard" /></a></li>
				</ul>
			</li>
			<li><a class="fNiv"><spring:message	code="master.page.actor" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="actor/registerAdministrator.do"><spring:message code="master.page.actor.administrator" /></a></li>
					<li><a href="actor/registerManager.do">      <spring:message code="master.page.actor.manager"  /></a></li>
				</ul>
			</li>
		</security:authorize>
		
		<security:authorize access="hasRole('CUSTOMER')">
			<li><a class="fNiv"><spring:message	code="master.page.package" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="package/list.do" ><spring:message code="master.page.package.list"  /></a></li>
				</ul>
			</li>
			<li><a class="fNiv"><spring:message	code="master.page.request" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="request/customer/list.do" ><spring:message code="master.page.customer.request"  /></a></li>
				</ul>
			</li>
			<li><a class="fNiv"><spring:message	code="master.page.contract" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="contract/customer/list.do"><spring:message code="master.page.customer.contract" /></a></li>					
				</ul>
			</li>
		</security:authorize>
		
		<security:authorize access="hasRole('MANAGER')">
			<li><a class="fNiv"><spring:message	code="master.page.package" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="package/list.do" ><spring:message code="master.page.package.list"  /></a></li>
					<li><a href="package/manager/list.do" ><spring:message code="master.page.manager.package"  /></a></li>
				</ul>
			</li>
			<li><a class="fNiv"><spring:message	code="master.page.request" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="request/manager/list.do" ><spring:message code="master.page.manager.request"  /></a></li>
				</ul>
			</li>
			<li><a class="fNiv"><spring:message	code="master.page.contract" /></a>
				<ul>
					<li class="arrow"></li>
					
					<li><a href="contract/manager/list.do"><spring:message code="master.page.manager.contract" /></a></li>					
				</ul>
			</li>
		</security:authorize>
		
		<security:authorize access="isAnonymous()">
			<li><a class="fNiv" href="security/login.do"><spring:message code="master.page.login" /></a></li>
			<li><a class="fNiv"><spring:message	code="master.page.actor" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="actor/registerCustomer.do"><spring:message code="master.page.actor.customer" /></a></li>
				</ul>
			</li>
			<li><a class="fNiv"><spring:message	code="master.page.package" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="package/list.do" ><spring:message code="master.page.package.list"  /></a></li>
				</ul>
			</li>
		</security:authorize>
		
		<security:authorize access="isAuthenticated()">
			<li>
				<a class="fNiv"> 
					<spring:message code="master.page.profile" /> 
			        (<security:authentication property="principal.username" />)
				</a>
				<ul>
					<li class="arrow"></li>
					<li><a href="actor/show.do"><spring:message code="master.page.profile" /></a></li>
					<li><a href="j_spring_security_logout"><spring:message code="master.page.logout" /> </a></li>
				</ul>
			</li>
		</security:authorize>
	</ul>
</div>

<div>
	<a href="?language=en">en</a> | <a href="?language=es">es</a>
</div>

