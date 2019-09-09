package controllers;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import domain.Actor;
import domain.Configuration;

import security.LoginService;
import services.ActorService;
import services.ConfigurationService;

@Controller
@RequestMapping("/welcome")
public class WelcomeController extends AbstractController {

	@Autowired
	private ActorService actorService;
	
	@Autowired
	private ConfigurationService configurationService;
	
	// Constructors -----------------------------------------------------------

	public WelcomeController() {
		super();
	}

	// Index ------------------------------------------------------------------		

	@RequestMapping(value = "/index")
	public ModelAndView index() {
		
		ModelAndView result;
		

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		String moment = formatter.format(new Date());
		String name = "Anonymous";
		
		if(LoginService.hasRole("CUSTOMER") || LoginService.hasRole("MANAGER") || LoginService.hasRole("ADMIN")) {
			Actor actor = actorService.getByUserAccount(LoginService.getPrincipal());
			name = actor.getName();
		}
		
		Configuration configuration = (Configuration) configurationService.findAll().toArray()[0];
		String welcomeText=" ";
		
		if(LocaleContextHolder.getLocale().getLanguage().toLowerCase().equals("es")){
			welcomeText = configuration.getWelcomeTextSpanish();
		}

		if(LocaleContextHolder.getLocale().getLanguage().toLowerCase().equals("en")){
			welcomeText = configuration.getWelcomeTextEnglish();
		}

		result = new ModelAndView("welcome/index");
		result.addObject("name", name);
		result.addObject("moment", moment);
		result.addObject("welcomeMessageToDisplay", welcomeText);
		result.addObject("systemName", configuration.getSystemName());
		
		return result;
	}

	//TOS ----------------------------------------------------------------------

	@RequestMapping(value = "/TOS")
	public ModelAndView TOS() {
		final ModelAndView result = new ModelAndView("welcome/TOS");
		return result;
	}
}
