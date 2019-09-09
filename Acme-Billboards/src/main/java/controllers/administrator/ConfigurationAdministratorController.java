package controllers.administrator;


import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ConfigurationService;
import controllers.AbstractController;
import domain.Configuration;
import forms.ConfigurationForm;

@Controller
@RequestMapping("/administrator/")
public class ConfigurationAdministratorController extends AbstractController {

	@Autowired
	private ConfigurationService configurationService;

	// Edit-------------------------------------------------------------
	
	@RequestMapping(value = "/configuration", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView res;

		Configuration configuration = (Configuration) configurationService.findAll().toArray()[0];

		res = this.createEditModelAndView(configuration);
		return res;
	}

	// Save FORM-------------------------------------------------------------
	
	@RequestMapping(value = "/configuration", method = RequestMethod.POST, params = "save")
	public ModelAndView save(ConfigurationForm config, BindingResult binding) {
		ModelAndView res;
		try {
			Configuration configuration = configurationService.reconstruct(config, binding);
			configurationService.save(configuration);
			res = new ModelAndView("redirect:/welcome/index.do");
		} catch (ValidationException oops) {
			res = new ModelAndView("administrator/configuration");
			res.addObject("configurationForm", config);
		} catch (Throwable e) {
			res = new ModelAndView("administrator/configuration");
			res.addObject("configurationForm", config);
			res.addObject("errorMessage", "administrator.commit.error");
		}
		return res;
	}

	protected ModelAndView createEditModelAndView(Configuration configuration) {
		
		ModelAndView result = createEditModelAndView(configuration, null);
		
		return result;
	}

	protected ModelAndView createEditModelAndView(Configuration configuration, String messageCode) {
		
		ModelAndView result = new ModelAndView("administrator/configuration");
		
		ConfigurationForm configurationForm = configurationService.construct(configuration);
		
		Boolean language = false;
		if (LocaleContextHolder.getLocale().getLanguage().toLowerCase().equals("es")) {
			language = true;
		}
		if (LocaleContextHolder.getLocale().getLanguage().toLowerCase().equals("en")) {
			language = false;
		}

		result.addObject("language", language);
		result.addObject("configurationForm", configurationForm);
		result.addObject("errorMessage", messageCode);
		return result;
	}
	
}