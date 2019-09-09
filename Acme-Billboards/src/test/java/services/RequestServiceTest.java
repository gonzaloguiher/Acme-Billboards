package services;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Customer;
import domain.Package;
import domain.Request;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@Transactional
public class RequestServiceTest extends AbstractTest {

	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private PackageService packageService;
	
	@Autowired
	private RequestService requestService;
	
	// ANALYSIS OF DATA COVERAGE:
	// Total coverage: 99.8%
	// Covered Instructions: 1.015
	//  Missed Instructions: 2
	//   Total Instructions: 1.017
	
	@Test
	public void driverCreateRequest() {
		
		final Object testingData[][] = {{"customer1", null},
										{"customer2", null},
										{"customer3", null},
										{"admin",     IllegalArgumentException.class},
										{"manager1",  IllegalArgumentException.class},
										{"manager2",  IllegalArgumentException.class},
										{"manager3",  IllegalArgumentException.class}};
		
		for(int i = 0; i < testingData.length; i++){
			templateCreateRequest((String) testingData[i][0], (Class<?>)testingData[i][1]);
		}
	}
	
	protected void templateCreateRequest(String username, Class<?> expected) {
		Class<?> caught = null;

		try{
			super.authenticate(username);
			this.requestService.create();
		} catch (Throwable oops){
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
		super.unauthenticate();
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void driverSaveRequest() {
		
		Collection<String> commentsCustomer = new ArrayList<String>();
		Collection<String> commentsManager  = new ArrayList<String>();
		
		String commentCustomer1 = "commentCustomer1";
		String commentCustomer2 = "commentCustomer2";
		String commentCustomer3 = "commentCustomer3";
		
		String commentManager1 = "commentManager1";
		String commentManager2 = "commentManager2";
		String commentManager3 = "commentManager3";
		
		commentsCustomer.add(commentCustomer1);
		commentsCustomer.add(commentCustomer2);
		commentsCustomer.add(commentCustomer3);
		
		commentsManager.add(commentManager1);
		commentsManager.add(commentManager2);
		commentsManager.add(commentManager3);
		
		Customer customer = (Customer) customerService.findAll().toArray()[0];
		Package pakage    = (Package)   packageService.findAll().toArray()[0];
		
		Object testingData[][] = {{"customer1",  "PENDING",  commentsCustomer, commentsManager, customer, pakage, null},
								  {"customer2",  "APPROVED", commentsCustomer, commentsManager, customer, pakage, null},
								  {"customer3",  "REJECTED", commentsCustomer, commentsManager, customer, pakage, null},
								  {"admin",      "PENDING",  commentsCustomer, commentsManager, customer, pakage, IllegalArgumentException.class},
								  {"manager1",   "APPROVED", commentsCustomer, commentsManager, customer, pakage, IllegalArgumentException.class},
								  {"manager2",   "REJECTED", commentsCustomer, commentsManager, customer, pakage, IllegalArgumentException.class},
								  {"manager3",   "PENDING",  commentsCustomer, commentsManager, customer, pakage,  IllegalArgumentException.class}};
		
		for(int i = 0; i < testingData.length; i++){
			templateSaveRequest((String) testingData[i][0], (String) testingData[i][1], (Collection<String>) testingData[i][2], (Collection<String>) testingData[i][3], 
					              (Customer) testingData[i][4], (Package) testingData[i][5], (Class<?>)testingData[i][6]);
		}
	}
	
	protected void templateSaveRequest(String username, String status, Collection<String> commentsCustomer, Collection<String> commentsManager, Customer customer, Package pakage, Class<?> expected){
		Class<?> caught = null;
		
		try{
			super.authenticate(username);
			Request request = requestService.create();
			request.setStatus(status);
			request.setCommentsCustomer(commentsCustomer);
			request.setCommentsManager(commentsManager);
			request.setCustomer(customer);
			request.setPakage(pakage);
			request = requestService.save(request);
		} catch (Throwable oops){
			caught = oops.getClass();
		}
		
		this.checkExceptions(expected, caught);
		super.unauthenticate();
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void driverUpdateRequest() {
		
		Collection<String> commentsCustomer = new ArrayList<String>();
		Collection<String> commentsManager  = new ArrayList<String>();
		
		String commentCustomer1 = "commentCustomer1";
		String commentCustomer2 = "commentCustomer2";
		String commentCustomer3 = "commentCustomer3";
		
		String commentManager1 = "commentManager1";
		String commentManager2 = "commentManager2";
		String commentManager3 = "commentManager3";
		
		commentsCustomer.add(commentCustomer1);
		commentsCustomer.add(commentCustomer2);
		commentsCustomer.add(commentCustomer3);
		
		commentsManager.add(commentManager1);
		commentsManager.add(commentManager2);
		commentsManager.add(commentManager3);
		
		Customer customer = (Customer) customerService.findAll().toArray()[0];
		Package pakage    = (Package)   packageService.findAll().toArray()[0];
		
		Object testingData[][] = {{"customer1",  "PENDING",  commentsCustomer, commentsManager, customer, pakage, null},
								  {"customer2",  "APPROVED", commentsCustomer, commentsManager, customer, pakage, null},
								  {"customer3",  "REJECTED", commentsCustomer, commentsManager, customer, pakage, null},
								  {"admin",      "PENDING",  commentsCustomer, commentsManager, customer, pakage, IllegalArgumentException.class},
								  {"manager1",   "APPROVED", commentsCustomer, commentsManager, customer, pakage, null},
								  {"manager2",   "REJECTED", commentsCustomer, commentsManager, customer, pakage, null},
								  {"manager3",   "PENDING",  commentsCustomer, commentsManager, customer, pakage, null}};
		
		for(int i = 0; i < testingData.length; i++){
			templateUpdateRequest((String) testingData[i][0], (String) testingData[i][1], (Collection<String>) testingData[i][2], (Collection<String>) testingData[i][3], 
					              (Customer) testingData[i][4], (Package) testingData[i][5], (Class<?>)testingData[i][6]);
		}
	}
	
	protected void templateUpdateRequest(String username, String status, Collection<String> commentsCustomer, Collection<String> commentsManager, Customer customer, Package pakage, Class<?> expected){
		Class<?> caught = null;
		
		Request request = (Request) requestService.findAll().toArray()[0];
		
		try{
			super.authenticate(username);
			request.setStatus(status);
			request.setCommentsCustomer(commentsCustomer);
			request.setCommentsManager(commentsManager);
			request.setCustomer(customer);
			request.setPakage(pakage);
			request = requestService.save(request);
		} catch (Throwable oops){
			caught = oops.getClass();
		}
		
		this.checkExceptions(expected, caught);
		super.unauthenticate();
	}
	
	@Test
	public void driverDeleteRequest() {
		
		Object testingData[][] = {{"customer1", null},
								  {"manager1",  IllegalArgumentException.class},
//								  {"customer2", IllegalArgumentException.class},
//								  {"customer3", IllegalArgumentException.class},
								  {"manager2",  IllegalArgumentException.class},
								  {"manager3",  IllegalArgumentException.class},
								  {"admin",     IllegalArgumentException.class}};
		
		for(int i = 0; i < testingData.length; i++){
			templateDeleteRequest((String) testingData[i][0], (Class<?>)testingData[i][1]);
		}
	}
	
	protected void templateDeleteRequest(String username, Class<?> expected){
		Class<?> caught = null;
		
		Request request = (Request) requestService.findAll().toArray()[0];
		
		try{
			super.authenticate(username);
			requestService.delete(request);
		} catch (Throwable oops){
			caught = oops.getClass();
		}
		
		this.checkExceptions(expected, caught);
		super.unauthenticate();
	}
	
	@Test
	public void testDelete() {
		
		authenticate("customer1");
		
		Request request = (Request) requestService.findAll().toArray()[0];
		
		requestService.delete(request);
		
		Assert.isTrue(!requestService.findAll().contains(request));
		
		unauthenticate();
	}
}