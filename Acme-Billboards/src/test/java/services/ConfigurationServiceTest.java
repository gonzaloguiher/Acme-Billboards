package services;

import java.util.Collection;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Configuration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@Transactional
public class ConfigurationServiceTest extends AbstractTest {

	@Autowired
	private ConfigurationService configurationService;

	// ANALYSIS OF DATA COVERAGE:
	// Total coverage: 92.6%
	// Covered Instructions: 199
	//  Missed Instructions: 16
	//   Total Instructions: 215

	@Test
	public void testCreateConfigurations() {

		super.authenticate("admin");

		Configuration configuration = configurationService.create();

		Assert.isNull(configuration.getBanner());
		Assert.isNull(configuration.getDefaultPhoneCode());
		Assert.isNull(configuration.getFinderCacheTime());
		Assert.isNull(configuration.getFinderQueryResults());
		Assert.isNull(configuration.getSystemName());
		Assert.isNull(configuration.getWelcomeTextEnglish());
		Assert.isNull(configuration.getWelcomeTextSpanish());

		super.authenticate(null);
	}

	@Test
	public void testSaveConfigurations() {

		super.authenticate("admin");

		Configuration configuration = configurationService.find();
		configuration.setDefaultPhoneCode("+66");
		Configuration saved = configurationService.save(configuration);
		Collection<Configuration> configurations = configurationService.findAll();

		Assert.isTrue(configurations.contains(saved));

		super.authenticate(null);
	}

	@Test
	public void testUpdateConfiguration() {

		super.authenticate("admin");

		Configuration configuration = new Configuration();
		configuration = (Configuration) configurationService.findAll().toArray()[0];
		configuration.setBanner("http://www.pixiv.com");
		configurationService.save(configuration);
		
		super.authenticate(null);
	}

	@Test(expected = ConstraintViolationException.class)
	public void testUpdateWrongBanner() {

		authenticate("admin");

		Configuration configuration = configurationService.find();
		configuration.setBanner("39");
		configurationService.save(configuration);

		unauthenticate();

	}

	@Test(expected = ConstraintViolationException.class)
	public void testUpdateEmptyBanner() {

		authenticate("admin");

		Configuration configuration = configurationService.find();
		configuration.setBanner("");
		configurationService.save(configuration);

		unauthenticate();
	}

	@Test
	public void driver() {
		Object testingData[][] = { { "admin", null },
				{ "hacker1", IllegalArgumentException.class },
				{ null, IllegalArgumentException.class } };

		for (int i = 0; i < testingData.length; i++) {
			template((String) testingData[i][0], (Class<?>) testingData[i][1]);
		}
	}

	protected void template(String username, Class<?> expected) {
		Class<?> caught = null;
		try {
			authenticate(username);
			Configuration configuration = (Configuration) configurationService.findAll().toArray()[0];
			configuration.setBanner("http://www.pixiv.com");
			configurationService.save(configuration);
			unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

}
