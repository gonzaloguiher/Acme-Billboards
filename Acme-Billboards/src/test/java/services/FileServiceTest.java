package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import domain.Contract;
import domain.File;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@Transactional
public class FileServiceTest extends AbstractTest {

	@Autowired
	private FileService fileService;
	
	@Autowired
	private ContractService contractService;
	
	// ANALYSIS OF DATA COVERAGE:
	// Total coverage: 99.4%
	// Covered Instructions: 644
	//  Missed Instructions: 4
	//   Total Instructions: 648
	
	@Test
	public void driverCreateFile() {
		
		final Object testingData[][] = {{"manager1", null},
										{"manager2", null},
										{"manager3", null}};
		
		for(int i = 0; i < testingData.length; i++){
			templateCreateFile((String) testingData[i][0], (Class<?>)testingData[i][1]);
		}
	}
	
	protected void templateCreateFile(String username, Class<?> expected) {
		Class<?> caught = null;
		
		Contract contract = (Contract) contractService.findAll().toArray()[0];
		
		try{
			super.authenticate(username);
			this.fileService.create(contract);
		} catch (Throwable oops){
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
		super.unauthenticate();
	}
	
	@Test
	public void driverSaveFile() {
		
		Object testingData[][] = {{"manager1",  "location", "http:/www.link.com", null},
								  {"manager2",  "location", "http:/www.link.com", null},
								  {"manager3",  "location", "http:/www.link.com", null},
								  {"admin",     "location", "http:/www.link.com", IllegalArgumentException.class},
								  {"customer1", "location", "http:/www.link.com", IllegalArgumentException.class},
								  {"customer2", "location", "http:/www.link.com", IllegalArgumentException.class},
								  {"customer3", "location", "http:/www.link.com", IllegalArgumentException.class}};
		
		for(int i = 0; i < testingData.length; i++){
			templateSaveFile((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>)testingData[i][3]);
		}
	}
	
	protected void templateSaveFile(String username, String location, String image, Class<?> expected){
		Class<?> caught = null;
		
		Contract contract = (Contract) contractService.findAll().toArray()[0];
		
		try{
			super.authenticate(username);
			File file = fileService.create(contract);
			file.setLocation(location);
			file.setImage(image);
			file = fileService.save(file);
		} catch (Throwable oops){
			caught = oops.getClass();
		}
		
		this.checkExceptions(expected, caught);
		super.unauthenticate();
	}
	
	@Test
	public void driverUpdateFile() {
		
		Object testingData[][] = {{"manager1",  "location", "http:/www.link.com", null},
								  {"manager2",  "location", "http:/www.link.com", null},
								  {"manager3",  "location", "http:/www.link.com", null},
								  {"admin",     "location", "http:/www.link.com", IllegalArgumentException.class},
								  {"customer1", "location", "http:/www.link.com", IllegalArgumentException.class},
								  {"customer2", "location", "http:/www.link.com", IllegalArgumentException.class},
								  {"customer3", "location", "http:/www.link.com", IllegalArgumentException.class}};
		
		for(int i = 0; i < testingData.length; i++){
			templateUpdateFile((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>)testingData[i][3]);
		}
	}
	
	protected void templateUpdateFile(String username, String location, String image, Class<?> expected){
		Class<?> caught = null;
		
		Contract contract = (Contract) contractService.findAll().toArray()[0];
		
		try{
			super.authenticate(username);
			File file = fileService.create(contract);
			file.setLocation(location);
			file.setImage(image);
			file = fileService.save(file);
		} catch (Throwable oops){
			caught = oops.getClass();
		}
		
		this.checkExceptions(expected, caught);
		super.unauthenticate();
	}
	
	@Test
	public void driverDeleteFile() {
		
		Object testingData[][] = {{"manager1", null},
								  {"manager2", null},
								  {"manager3", null},
								  {"customer1", IllegalArgumentException.class},
								  {"customer2", IllegalArgumentException.class},
								  {"customer3", IllegalArgumentException.class},
								  {"admin",     IllegalArgumentException.class}};
		
		for(int i = 0; i < testingData.length; i++){
			templateDeleteFile((String) testingData[i][0], (Class<?>)testingData[i][1]);
		}
	}
	
	protected void templateDeleteFile(String username, Class<?> expected){
		Class<?> caught = null;
		
		File file = (File) fileService.findAll().toArray()[0];
		
		try{
			super.authenticate(username);
			fileService.delete(file);
		} catch (Throwable oops){
			caught = oops.getClass();
		}
		
		this.checkExceptions(expected, caught);
		super.unauthenticate();
	}
}
