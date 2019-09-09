package services;

import java.util.Collection;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ConfigurationRepository;
import security.LoginService;
import domain.Configuration;
import forms.ConfigurationForm;

@Service
@Transactional
public class ConfigurationService {

	@Autowired
	private ConfigurationRepository configurationRepository;

	@Autowired
	private Validator validator;

	public Configuration create() {
		Configuration res = new Configuration();
		return res;
	}

	public Collection<Configuration> findAll() {
		return configurationRepository.findAll();
	}

	public Configuration findOne(int Id) {
		return configurationRepository.findOne(Id);
	}

	public Configuration find() {
		return configurationRepository.findAll().iterator().next();
	}

	public Configuration save(Configuration a) {
		Configuration saved;
		Assert.isTrue(LoginService.hasRole("ADMIN"));
		Assert.isTrue(this.findAll().size() == 1);
		Assert.isTrue(this.findAll().toArray()[0].equals(a));
		saved = configurationRepository.saveAndFlush(a);
		return saved;
	}

	public void delete(Configuration a) {
		LoginService.hasRole("ADMIN");
		configurationRepository.delete(a);
	}

	public Configuration reconstruct(ConfigurationForm configurationForm, BindingResult binding) {
		
		Configuration result;

		result = this.find();
		result.setBanner(configurationForm.getBanner());
		result.setDefaultPhoneCode(configurationForm.getDefaultPhoneCode());
		result.setFinderCacheTime(configurationForm.getFinderCacheTime());

		result.setFinderQueryResults(configurationForm.getFinderQueryResults());
		result.setWelcomeTextEnglish(configurationForm.getWelcomeTextEnglish());
		result.setWelcomeTextSpanish(configurationForm.getWelcomeTextSpanish());
		result.setSystemName(configurationForm.getSystemName());
		result.setVatPercentage(configurationForm.getVatPercentage());

		validator.validate(result, binding);
		if (binding.hasErrors()) {
			throw new ValidationException();
		}
		return result;
	}

	public ConfigurationForm construct(Configuration conf) {
		
		ConfigurationForm result = new ConfigurationForm();
		
		result.setBanner(conf.getBanner());
		result.setDefaultPhoneCode(conf.getDefaultPhoneCode());
		
		result.setFinderCacheTime(conf.getFinderCacheTime());
		result.setFinderQueryResults(conf.getFinderQueryResults());
		
		result.setVatPercentage(conf.getVatPercentage());
		result.setSystemName(conf.getSystemName());
		
		result.setWelcomeTextEnglish(conf.getWelcomeTextEnglish());
		result.setWelcomeTextSpanish(conf.getWelcomeTextSpanish());
		
		return result;
	}

}