package services;

import java.util.Date;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Administrator;
import domain.CreditCard;
import domain.Customer;
import domain.Manager;

import security.Authority;
import security.UserAccount;
import security.UserAccountService;
import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@Transactional
public class ActorServiceTest extends AbstractTest {

	@Autowired
	private ActorService actorService;
	
	@Autowired
	private ManagerService managerService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private AdministratorService administratorService;
	
	@Autowired
	private UserAccountService userAccountService;
	
	@Autowired
	private CreditCardService creditCardService;
	
	// ANALYSIS OF DATA COVERAGE:
	// Total coverage: 93.0%
	// Covered Instructions: 695
	//  Missed Instructions: 52
	//   Total Instructions: 747
	
	@Test
	public void driverCustomer() {

		UserAccount userAccount = userAccountService.create();
		Authority authority = new Authority();
		authority.setAuthority(Authority.CUSTOMER);
		userAccount.getAuthorities().add(authority);
		userAccount.setUsername("username");
		userAccount.setPassword("password");
		Customer customer = customerService.create(userAccount);

		CreditCard credit = creditCardService.create();
		credit.setCVV(123);
		credit.setHolder("holder");
		credit.setExpirationDate(new Date(System.currentTimeMillis() + 623415234));
		credit.setMake("AMEX");
		credit.setNumber("4576098756783456");

		customer.setName("name");
		customer.setSurname("surname");
		customer.setMiddleName("middleName");
		customer.setVatNumber("ASD12341234");
		customer.setEmail("email@email.com");
		customer.setPhone("612123456");
		customer.setCreditCard(credit);

		Customer customer1 = customerService.findOne(getEntityId("customer1"));

		System.out.println(customer1 + " " + customer);

		Object testingData[][] = {{customer, null}, {customer1, null}, {null, NullPointerException.class}};
		
		for (int i = 0; i < testingData.length; i++) {
			templateCustomer((Customer) testingData[i][0], (Class<?>) testingData[i][1]);
		}
	}

	protected void templateCustomer(Customer customer, Class<?> expected) {
		Class<?> caught = null;
		try {

			actorService.registerCustomer(customer);

		} catch (Throwable oops) {
			oops.printStackTrace();
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	
	@Test
	public void driverManager() {

		UserAccount userAccount = userAccountService.create();
		Authority authority = new Authority();
		authority.setAuthority(Authority.MANAGER);
		userAccount.getAuthorities().add(authority);
		userAccount.setUsername("username");
		userAccount.setPassword("password");
		Manager manager = managerService.create(userAccount);

		manager.setName("name");
		manager.setSurname("surname");
		manager.setMiddleName("middleName");
		manager.setEmail("email@email.com");
		manager.setPhone("612123456");

		Manager manager1 = managerService.findOne(getEntityId("manager1"));

		System.out.println(manager1 + " " + manager);

		Object testingData[][] = {{manager, null}, {manager1, null}, {null, NullPointerException.class}};
		
		for (int i = 0; i < testingData.length; i++) {
			templateManager((Manager) testingData[i][0], (Class<?>) testingData[i][1]);
		}
	}

	protected void templateManager(Manager manager, Class<?> expected) {
		Class<?> caught = null;
		try {
			authenticate("admin");
			actorService.registerManager(manager);
			unauthenticate();
		} catch (Throwable oops) {
			oops.printStackTrace();
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	
	@Test
	public void driverAdministrator() {

		UserAccount userAccount = userAccountService.create();
		Authority authority = new Authority();
		authority.setAuthority(Authority.ADMIN);
		userAccount.getAuthorities().add(authority);
		userAccount.setUsername("username");
		userAccount.setPassword("password");
		Administrator administrator1 = administratorService.create(userAccount);

		administrator1.setName("name");
		administrator1.setSurname("surname");
		administrator1.setMiddleName("middleName");
		administrator1.setEmail("email@email.com");
		administrator1.setPhone("612123456");

		Administrator administrator = (Administrator) administratorService.findAll().toArray()[0];

		System.out.println(administrator + " " + administrator1);

		Object testingData[][] = {{administrator, IllegalArgumentException.class}, {administrator1, IllegalArgumentException.class}, {null, NullPointerException.class}};
		
		for (int i = 0; i < testingData.length; i++) {
			templateAdministrator((Administrator) testingData[i][0], (Class<?>) testingData[i][1]);
		}
	}

	protected void templateAdministrator(Administrator administrator, Class<?> expected) {
		Class<?> caught = null;
		try {
			if(administrator.equals(administratorService.findAll().toArray()[0])){
				authenticate("administrator");
			}else {
				authenticate(null);
			}
			actorService.registerAdministrator(administrator);

		} catch (Throwable oops) {
			oops.printStackTrace();
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	
	@Test
	public void testRegisterAdmin(){
		
		authenticate("admin");
		
		UserAccount userAccount = userAccountService.create();
		Authority authority = new Authority();
		authority.setAuthority(Authority.ADMIN);
		userAccount.getAuthorities().add(authority);
		userAccount.setUsername("admin10");
		userAccount.setPassword("admin10");		
		Administrator administrator = administratorService.create(userAccount);
		
		administrator.setName("name");
		administrator.setSurname("surname");
		administrator.setMiddleName("middleName");
		administrator.setEmail("email@email.com");
		administrator.setPhone("612123456");
		
		Administrator result = actorService.registerAdministrator(administrator);
		
		Assert.isTrue(administratorService.findAll().contains(result));
		
		unauthenticate();
	}
	
	@Test
	public void testEditAdministrator(){
		
		authenticate("admin");
		
		Administrator administrator = administratorService.findByPrincipal();
		
		administrator.setName("adminUpdatedName");
		administrator.setSurname("adminUpdatedSurname");
		administrator.setMiddleName("adminUpdatedMiddleName");
		administrator.setEmail("adminUpdated@email.com");
		administrator.setAddress("adminUpdatedAddress");
		administrator.setPhoto("http://www.photoUpdated.com");
		
		Administrator result = administratorService.save(administrator);
		
		Assert.isTrue(administratorService.findAll().contains(result));
		
		unauthenticate();
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testIncorrectEditAdministrator(){
		
		authenticate("admin");
		
		Administrator administrator = administratorService.findByPrincipal();
		
		administrator.setName("");
		administrator.setSurname("");
		administrator.setEmail("adminUpdated.email.com");
		administrator.setAddress("adminUpdatedAddress");
		administrator.setPhoto("photoUpdated.com");
		
		Administrator result = administratorService.save(administrator);
		
		Assert.isTrue(administratorService.findAll().contains(result));
		
		unauthenticate();
	}
	
	@Test
	public void testEditManager(){
		
		authenticate("manager1");
		
		Manager manager = managerService.findByPrincipal();
		
		manager.setName("managerUpdatedName");
		manager.setSurname("managerUpdatedSurname");
		manager.setMiddleName("managerUpdatedMiddleName");
		manager.setEmail("managerUpdated@email.com");
		manager.setAddress("managerUpdatedAddress");
		manager.setPhoto("http://www.photoUpdated.com");
		
		Manager result = managerService.save(manager);
		
		Assert.isTrue(managerService.findAll().contains(result));
		
		unauthenticate();
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testIncorrectEditManager(){
		
		authenticate("manager1");
		
		Manager manager = managerService.findByPrincipal();
		
		manager.setName("");
		manager.setSurname("");
		manager.setEmail("managerUpdated.email.com");
		manager.setAddress("managerUpdatedAddress");
		manager.setPhoto("photoUpdated.com");
		
		Manager result = managerService.save(manager);
		
		Assert.isTrue(managerService.findAll().contains(result));
		
		unauthenticate();
	}
	
	@Test
	public void testEditCustomer(){
		
		authenticate("customer1");
		
		Customer customer = customerService.findByPrincipal();
		
		customer.setName("customerUpdatedName");
		customer.setSurname("customerUpdatedSurname");
		customer.setMiddleName("customerUpdatedMiddleName");
		customer.setEmail("customerUpdated@email.com");
		customer.setAddress("customerUpdatedAddress");
		customer.setPhoto("http://www.photoUpdated.com");
		
		Customer result = customerService.save(customer);
		
		Assert.isTrue(customerService.findAll().contains(result));
		
		unauthenticate();
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testIncorrectEditCustomer(){
		
		authenticate("customer1");
		
		Customer customer = customerService.findByPrincipal();
		
		customer.setName("");
		customer.setSurname("");
		customer.setEmail("managerUpdated.email.com");
		customer.setAddress("managerUpdatedAddress");
		customer.setPhoto("photoUpdated.com");
		
		Customer result = customerService.save(customer);
		
		Assert.isTrue(customerService.findAll().contains(result));
		
		unauthenticate();
	}
}

