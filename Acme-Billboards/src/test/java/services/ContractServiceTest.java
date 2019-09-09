package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Contract;
import domain.File;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@Transactional
public class ContractServiceTest extends AbstractTest {

	@Autowired
	private FileService fileService;
	
	@Autowired
	private PackageService packageService;
	
	@Autowired
	private ContractService contractService;
	
	// ANALYSIS OF DATA COVERAGE:
	// Total coverage: 99.6%
	// Covered Instructions: 567
	//  Missed Instructions: 2
	//   Total Instructions: 569
	
	
	@Test
	public void testSave() {
		
		authenticate("manager1");
		
		Date momentCustomer = new Date(System.currentTimeMillis() - 10000);
		Date momentManager  = new Date(System.currentTimeMillis() - 1000);
		
		domain.Package pakage = (domain.Package) packageService.findAll().toArray()[0];
		
		Contract contract = contractService.create();
		
		Collection<File> files = new ArrayList<File>();
		
		File file1 = (File) fileService.findAll().toArray()[0];
		file1.setLocation("location1");
		file1.setImage("http://www.image1.com");
		fileService.save(file1);
		File file2 = (File) fileService.findAll().toArray()[1];
		file2.setLocation("location2");
		file2.setImage("http://www.image2.com");
		fileService.save(file2);
		File file3 = (File) fileService.findAll().toArray()[2];
		file3.setLocation("location3");
		file3.setImage("http://www.image3.com");
		fileService.save(file3);
		 
		files.add(file1);
		files.add(file2);
		files.add(file3);
		
		contract.setText("text");
		contract.setHash("hash");
		contract.setSignCustomer("signCustomer");
		contract.setSignManager("signManager");
		contract.setMomentCustomer(momentCustomer);
		contract.setMomentManager(momentManager);
		contract.setPakage(pakage);
		contract.setFiles(files);
		
		Contract result = contractService.save(contract);
		Assert.isTrue(contractService.findAll().contains(result));
		
		unauthenticate();
	}
	
//	@Test
//	public void driverSaveContract() {
//		
//		Date momentCustomer = new Date(System.currentTimeMillis() - 1000); 
//		Date momentManager  = new Date(System.currentTimeMillis() - 1000); 
//		
//		domain.Package pakage = (domain.Package) packageService.findAll().toArray()[0];
//		
//		Object testingData[][] = {{"manager1",  "text", "hash", "signCustomer", "signManager", momentCustomer, momentManager, pakage, null},
//								  {"manager2",  "text", "hash", "signCustomer", "signManager", momentCustomer, momentManager, pakage, null},
//								  {"manager3",  "text", "hash", "signCustomer", "signManager", momentCustomer, momentManager, pakage, null},
//								  {"admin",     "text", "hash", "signCustomer", "signManager", momentCustomer, momentManager, pakage, IllegalArgumentException.class},
//								  {"customer1", "text", "hash", "signCustomer", "signManager", momentCustomer, momentManager, pakage, IllegalArgumentException.class},
//								  {"customer2", "text", "hash", "signCustomer", "signManager", momentCustomer, momentManager, pakage, IllegalArgumentException.class},
//								  {"customer3", "text", "hash", "signCustomer", "signManager", momentCustomer, momentManager, pakage, IllegalArgumentException.class}};
//		
//		for(int i = 0; i < testingData.length; i++){
//			templateSaveContract((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], 
//								 (Date) testingData[i][5], (Date) testingData[i][6], (domain.Package) testingData[i][7], (Class<?>)testingData[i][8]);
//		}
//	}
//	
//	protected void templateSaveContract(String username, String text, String hash, String signCustomer, String signManager, 
//										Date momentCustomer, Date momentManager, domain.Package pakage, Class<?> expected) {
//		Class<?> caught = null;
//		
//		try {
//			super.authenticate(username);
//			Contract contract = contractService.create();
//			contract.setText(text);
//			contract.setHash(hash);
//			contract.setSignCustomer(signCustomer);
//			contract.setSignManager(signManager);
//			contract.setMomentCustomer(momentCustomer);
//			contract.setMomentManager(momentManager);
//			contract.setPakage(pakage);
//			contract = contractService.save(contract);
//		} catch (Throwable oops) {
//			caught = oops.getClass();
//		}
//		
//		this.checkExceptions(expected, caught);
//		super.unauthenticate();
//	}
	
	@Test
	public void driverUpdateContract() {
		
		Date momentCustomer = new Date(System.currentTimeMillis() - 1000); 
		Date momentManager  = new Date(System.currentTimeMillis() - 1000); 
		
		Contract contract = (Contract) contractService.findAll().toArray()[0];
		
		Object testingData[][] = {{"manager1",  "text", contract.getHash(), contract.getSignCustomer(), "signManager", contract.getMomentCustomer(), momentManager, null},
								  {"manager2",  "text", contract.getHash(), contract.getSignCustomer(), "signManager", contract.getMomentCustomer(), momentManager, null},
								  {"manager3",  "text", contract.getHash(), contract.getSignCustomer(), "signManager", contract.getMomentCustomer(), momentManager, null},
								  {"customer1", contract.getText(), contract.getHash(), "signCustomer", contract.getSignManager(), momentCustomer, contract.getMomentManager(), null},
								  {"customer2", contract.getText(), contract.getHash(), "signCustomer", contract.getSignManager(), momentCustomer, contract.getMomentManager(), null},
								  {"customer3", contract.getText(), contract.getHash(), "signCustomer", contract.getSignManager(), momentCustomer, contract.getMomentManager(), null},
								  {"admin",     contract.getText(), contract.getHash(), "signCustomer", "signManager", momentCustomer, momentManager, IllegalArgumentException.class}};
		
		for(int i = 0; i < testingData.length; i++){
			templateUpdateContract((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], 
								   (Date) testingData[i][5], (Date) testingData[i][6], (Class<?>)testingData[i][7]);
		}
	}
	
	protected void templateUpdateContract(String username, String text, String hash, String signCustomer, String signManager, Date momentCustomer, Date momentManager, Class<?> expected) {
		Class<?> caught = null;
		
		Contract contract = (Contract) contractService.findAll().toArray()[0];
		
		try {
			super.authenticate(username);
			contract.setText(text);
			contract.setHash(hash);
			contract.setSignCustomer(signCustomer);
			contract.setSignManager(signManager);
			contract.setMomentCustomer(momentCustomer);
			contract.setMomentManager(momentManager);
			contract = contractService.save(contract);
		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		
		this.checkExceptions(expected, caught);
		super.unauthenticate();
	}
	
//	@Test
//	public void driverDeleteContract() {
//		
//		Object testingData[][] = {{"manager1", null},
//								  {"manager2", null},
//								  {"manager3", null},
//								  {"customer1", IllegalArgumentException.class},
//								  {"customer2", IllegalArgumentException.class},
//								  {"customer3", IllegalArgumentException.class},
//								  {"admin",     IllegalArgumentException.class}};
//		
//		for(int i = 0; i < testingData.length; i++){
//			templateDeleteContract((String) testingData[i][0], (Class<?>) testingData[i][1]);
//		}
//	}
//	
//	protected void templateDeleteContract(String username, Class<?> expected) {
//		Class<?> caught = null;
//		
//		Contract contract = (Contract) contractService.findAll().toArray()[0];
//		
//		try {
//			super.authenticate(username);
//			contract.setStatus("DRAFT");
//			Contract saved = contractService.save(contract);
//			contractService.testDelete(saved);
//		} catch (Throwable oops) {
//			caught = oops.getClass();
//		}
//		
//		this.checkExceptions(expected, caught);
//		super.unauthenticate();
//	}
	
	@Test
	public void testDelete() {
		
		authenticate("manager1");
		
		Contract contract = (Contract) contractService.findAll().toArray()[0];
		
		contract.setStatus("DRAFT");
		
		Contract saved = contractService.save(contract);
		
		contractService.testDelete(saved);
		
		Assert.isTrue(!contractService.findAll().contains(saved));
		
		unauthenticate();
	}
}
