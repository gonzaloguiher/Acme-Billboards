package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Customer;
import domain.Finder;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@Transactional
public class FinderServiceTest extends AbstractTest {

	@Autowired
	private FinderService finderService;
	
	@Autowired
	private CustomerService customerService;
	
	// ANALYSIS OF DATA COVERAGE:
	// Total coverage: 92.1%
	// Covered Instructions: 116
	//  Missed Instructions: 10
	//   Total Instructions: 126

	@Test
	public void testCreate() {

		Finder finder = finderService.create();

		Assert.isTrue(finder.getPackages().isEmpty());
		Assert.isNull(finder.getKeyword());
		Assert.isNull(finder.getCustomer());
		Assert.isNull(finder.getMoment());

	}

	@Test
	public void testSave() {

		Finder finder, saved;

		authenticate("customer1");

		finder = finderService.findByPrincipal();
		finder.setKeyword("santa");

		saved = this.finderService.save(finder);
		Assert.isTrue(finderService.findAll().contains(saved));

		unauthenticate();
	}

	@Test
	public void testDelete() {

		authenticate("customer1");

		Finder finder = (Finder) this.finderService.findAll().toArray()[0];

		finderService.delete(finder);
		Assert.isTrue(!finderService.findAll().contains(finder));

		unauthenticate();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSaveOther() {

		authenticate(null);

		Customer customer = customerService.findOne(getEntityId("customer2"));
		Finder finder = finderService.findByCustomer(customer);

		finder.setKeyword("Australia");

		finderService.save(finder);

		unauthenticate();

	}

	@Test
	public void testSelfAssigned() {

		authenticate("customer1");

		Finder finder = finderService.findByPrincipal();
		finder.setKeyword("santa");

		finderService.save(finder);

		unauthenticate();

	}
}
