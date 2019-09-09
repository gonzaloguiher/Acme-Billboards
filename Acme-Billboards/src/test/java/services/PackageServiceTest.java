package services;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Manager;
import domain.Package;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@Transactional
public class PackageServiceTest extends AbstractTest {

	@Autowired
	private ManagerService managerService;
	
	@Autowired
	private PackageService packageService;
	
	
	// ANALYSIS OF DATA COVERAGE:
	// Total coverage: 91.7%
	// Covered Instructions: 844
	//  Missed Instructions: 76
	//   Total Instructions: 920
	
	@Test
	public void driverCreatePackage(){
		
		final Object testingData[][] = {{"manager1", null},
										{"manager2", null},
										{"manager3", null},
										{"admin",      IllegalArgumentException.class},
										{"customer1",  IllegalArgumentException.class},
										{"customer2",  IllegalArgumentException.class},
										{"customer3",  IllegalArgumentException.class}};
		
		for(int i = 0; i < testingData.length; i++){
			templateCreatePackage((String) testingData[i][0], (Class<?>)testingData[i][1]);
		}
	}
	
	protected void templateCreatePackage(String username, Class<?> expected){
		Class<?> caught = null;

		try{
			super.authenticate(username);
			this.packageService.create();
		} catch (Throwable oops){
			caught = oops.getClass();
		}
		
		this.checkExceptions(expected, caught);
		super.unauthenticate();
	}
	
	@Test
	public void testSave() {
		
		authenticate("manager1");
		
		Date startMoment = new Date(System.currentTimeMillis() + 10000);
		Date endMoment   = new Date(System.currentTimeMillis() + 1000);
		
		domain.Package pakage = packageService.create();
		
		Manager manager = (Manager) managerService.findAll().toArray()[0];
		
		pakage.setTitle("Title package");
		pakage.setDescription("Description package");
		pakage.setStartMoment(startMoment);
		pakage.setEndMoment(endMoment);
		pakage.setPhoto("http://www.photo.com");
		pakage.setPrice(245.65);
		pakage.setManager(manager);
		
		domain.Package result = packageService.save(pakage);
		Assert.isTrue(packageService.findAll().contains(result));
		
		unauthenticate();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testSaveNotAuthenticated() {
		
		authenticate(null);
		
		Date startMoment = new Date(System.currentTimeMillis() - 10000);
		Date endMoment   = new Date(System.currentTimeMillis() - 1000);
		
		domain.Package pakage = packageService.create();
		
		Manager manager = (Manager) managerService.findAll().toArray()[0];
		
		pakage.setTitle("Title package");
		pakage.setDescription("Description package");
		pakage.setStartMoment(startMoment);
		pakage.setEndMoment(endMoment);
		pakage.setPhoto("http://www.photo.com");
		pakage.setPrice(245.65);
		pakage.setManager(manager);
		
		domain.Package result = packageService.save(pakage);
		Assert.isTrue(packageService.findAll().contains(result));
		
		unauthenticate();
	}
	
	@Test(expected = javax.validation.ConstraintViolationException.class)
	public void testSaveIncorrectData() {
		
		authenticate("manager1");
		
		Date startMoment = new Date(System.currentTimeMillis() - 10000);
		Date endMoment   = new Date(System.currentTimeMillis() - 1000);
		
		domain.Package pakage = packageService.create();
		
		Manager manager = (Manager) managerService.findAll().toArray()[0];
		
		pakage.setTitle("");
		pakage.setDescription("");
		pakage.setStartMoment(startMoment);
		pakage.setEndMoment(endMoment);
		pakage.setPrice(245.65);
		pakage.setManager(manager);
		
		domain.Package result = packageService.save(pakage);
		Assert.isTrue(packageService.findAll().contains(result));
		
		unauthenticate();
	}
	
	@Test(expected = javax.validation.ConstraintViolationException.class)
	public void testSaveIncorrectDate() {
		
		authenticate("manager1");
		
		domain.Package pakage = packageService.create();
		
		Manager manager = (Manager) managerService.findAll().toArray()[0];
		
		pakage.setTitle("Title package");
		pakage.setDescription("Description package");
		pakage.setPhoto("http://www.photo.com");
		pakage.setPrice(245.65);
		pakage.setManager(manager);
		
		domain.Package result = packageService.save(pakage);
		Assert.isTrue(packageService.findAll().contains(result));
		
		unauthenticate();
	}
	
	@Test
	public void driverUpdatePackage() {
		
		domain.Package pakage = (Package) packageService.findAll().toArray()[0];
		
		Object testingData[][] = {{"manager1",  "title", "description", 255.65, "https://www.photo.com", true, null},
								  {"manager2",  "title", "description", 312.45, "https://www.photo.com", true, null},
								  {"manager3",  "title", "description", 225.80, "https://www.photo.com", true, null},
								  {"manager1",  pakage.getTitle(), "description", 255.65, "https://www.photo.com", true, null},
								  {"manager2",  "title", pakage.getDescription(), 312.45, "https://www.photo.com", true, null},
								  {"manager3",  "title", "description", 225.80, pakage.getPhoto(), true, null},
								  {"admin",     "title", "description", 255.65, "https://www.photo.com", true, IllegalArgumentException.class},
								  {"customer1", "title", "description", 255.65, "https://www.photo.com", true, IllegalArgumentException.class},
								  {"customer2", "title", "description", 312.45, "https://www.photo.com", true, IllegalArgumentException.class},
								  {"customer3", "title", "description", 225.80, "https://www.photo.com", true, IllegalArgumentException.class}};
		
		for(int i = 0; i < testingData.length; i++){
			templateUpdatePackage((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Double) testingData[i][3], 
					              (String) testingData[i][4], (Boolean) testingData[i][5], (Class<?>)testingData[i][6]);
		}
	}
	
	protected void templateUpdatePackage(String username, String title, String description, Double price, String photo, Boolean isDraft, Class<?> expected){
		Class<?> caught = null;
		
		domain.Package pakage = (Package) packageService.findAll().toArray()[0];
		
		try{
			super.authenticate(username);
			pakage.setTitle(title);
			pakage.setDescription(description);
			pakage.setPrice(price);
			pakage.setPhoto(photo);
			pakage.setIsDraft(isDraft);
			pakage = packageService.save(pakage);
		} catch (Throwable oops){
			caught = oops.getClass();
		}
		
		this.checkExceptions(expected, caught);
		super.unauthenticate();
	}
	
	@Test
	public void driverDeletePackage() {
		
		Object testingData[][] = {{"manager1",  null},
//								  {"manager2",  IllegalArgumentException.class},
//								  {"manager3",  IllegalArgumentException.class},
								  {"admin",     IllegalArgumentException.class},
								  {"customer1", IllegalArgumentException.class},
								  {"customer2", IllegalArgumentException.class},
								  {"customer3", IllegalArgumentException.class}};
		
		for(int i = 0; i < testingData.length; i++){
			templateDeletePackage((String) testingData[i][0], (Class<?>)testingData[i][1]);
		}
	}
	
	protected void templateDeletePackage(String username, Class<?> expected){
		Class<?> caught = null;
		
		domain.Package pakage = (Package) packageService.findAll().toArray()[6];
		
		try{
			super.authenticate(username);
			packageService.delete(pakage);
		} catch (Throwable oops){
			caught = oops.getClass();
		}
		
		this.checkExceptions(expected, caught);
		super.unauthenticate();
	}
	
//	@Test
//	public void testDelete() {
//		
//		authenticate("manager1");
//		
//		domain.Package pakage = (Package) packageService.findAll().toArray()[6];
//		
//		packageService.delete(pakage);
//		
//		Assert.isTrue(!packageService.findAll().contains(pakage));
//		
//		unauthenticate();
//	}
}