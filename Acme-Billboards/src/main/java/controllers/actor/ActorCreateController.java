package controllers.actor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.ActorService;
import controllers.AbstractController;
import domain.Administrator;
import domain.Customer;
import domain.Manager;
import forms.RegisterAdministratorForm;
import forms.RegisterCustomerForm;
import forms.RegisterManagerForm;

@Controller
@RequestMapping("actor/")
public class ActorCreateController extends AbstractController {

	@Autowired
	private ActorService actorService;
	
	// Create -----------------------------------------------------------------

	@RequestMapping(value = "/registerCustomer", method = RequestMethod.GET)
	public ModelAndView registerCustomer() {
		RegisterCustomerForm form = new RegisterCustomerForm();
		return this.createRegisterCustomerModelAndView(form, "CUSTOMER");
	}

	@RequestMapping(value = "/registerManager", method = RequestMethod.GET)
	public ModelAndView registerManager() {
		if(LoginService.hasRole("ADMIN")) {
			RegisterManagerForm form = new RegisterManagerForm();
		return this.createRegisterManagerModelAndView(form, "MANAGER");
		} else {
			return new ModelAndView("error/access");
		}
	}

	@RequestMapping(value = "/registerAdministrator", method = RequestMethod.GET)
	public ModelAndView createAdministrator() {
		if(LoginService.hasRole("ADMIN")) {
			RegisterAdministratorForm form = new RegisterAdministratorForm();
			return this.createRegisterAdministratorModelAndView(form, "ADMIN");
		} else {
			return new ModelAndView("error/access");
		}
	}
	
	// SAVES -------------------------------------------------------

	// CUSTOMER
	@RequestMapping(value = "/registerCustomer", method = RequestMethod.POST, params = "save")
	public ModelAndView saveMember(@ModelAttribute("registerForm") RegisterCustomerForm registerForm, final BindingResult binding) {
		Boolean expired = false;
		Boolean passMatch = false;
		
		Calendar c = Calendar.getInstance();		
		System.out.println("el año es: " + c.get(Calendar.YEAR)+ " y el mes es: "+ c.get(Calendar.MONTH));
		if(registerForm.getExpirationYear()== c.get(Calendar.YEAR) && registerForm.getExpirationMonth()<=c.get(Calendar.MONTH)){
			expired = true;
		}
		
		if(registerForm.getPassword().equals(registerForm.getPassword2())){passMatch=true;}
		
			Customer customer = this.actorService.reconstructCustomer(registerForm, binding);
			
			if (binding.hasErrors() || expired || !passMatch) {
				System.out.println(binding);
				ModelAndView res = this.createRegisterCustomerModelAndView(registerForm, "CUSTOMER");
				res.addObject("isExpired", expired);
				res.addObject("passMatch", passMatch);
				return res;
			} else {
			try {
				actorService.registerCustomer(customer);
				return new ModelAndView("redirect:/");
			
		} catch (final Throwable oops) {
			oops.printStackTrace();
			RegisterCustomerForm form = new RegisterCustomerForm();
			return this.createRegisterCustomerModelAndView(form, "CUSTOMER");
			}
		}
	}

	// MANAGER
	@RequestMapping(value = "/registerManager", method = RequestMethod.POST, params = "save")
	public ModelAndView saveBrotherhood(@ModelAttribute("registerForm") RegisterManagerForm registerForm, final BindingResult binding) {

		Boolean expired = false;
		Boolean passMatch = false;
		
		if(registerForm.getPassword().equals(registerForm.getPassword2())){passMatch=true;}
		
		Manager manager = this.actorService.reconstructManager(registerForm, binding);
		
		if (binding.hasErrors() || expired || !passMatch) {
			System.out.println(binding);
			ModelAndView res = this.createRegisterManagerModelAndView(registerForm);
			res.addObject("isExpired", expired);
			res.addObject("passMatch", passMatch);
			return res;
		} else {
			try {
				actorService.registerManager(manager);
				return new ModelAndView("redirect:/");

			} catch (final Throwable oops) {
				oops.printStackTrace();
				RegisterManagerForm form = new RegisterManagerForm();
				return this.createRegisterManagerModelAndView(form);
			}
		}
	}
	
	// ADMIN
	@RequestMapping(value = "/registerAdministrator", method = RequestMethod.POST, params = "save")
	public ModelAndView saveAdmin(@ModelAttribute("registerForm") RegisterAdministratorForm registerForm, final BindingResult binding) {
		
		Boolean expired = false;
		Boolean passMatch = false;
		
		if(registerForm.getPassword().equals(registerForm.getPassword2())){passMatch=true;}
		
		Administrator administrator = this.actorService.reconstructAdministrator(registerForm,binding);
		
		if (binding.hasErrors()|| expired || !passMatch) {
			ModelAndView res = this.createRegisterAdministratorModelAndView(registerForm, "ADMIN");
			res.addObject("isExpired", expired);
			res.addObject("passMatch", passMatch);
			return res;
		} else {
			try {
				actorService.registerAdministrator(administrator);
				return new ModelAndView("redirect:/");

			} catch (final Throwable oops) {
				oops.printStackTrace();
				RegisterAdministratorForm form = new RegisterAdministratorForm();
				return this.createRegisterAdministratorModelAndView(form, "ADMIN");
			}
		}
	}
	
	protected ModelAndView createRegisterManagerModelAndView(RegisterManagerForm form) {
		ModelAndView result;
		result = this.createRegisterManagerModelAndView(form, null);
		return result;
	}
	
	protected ModelAndView createRegisterCustomerModelAndView(RegisterCustomerForm form) {
		ModelAndView result;
		result = this.createRegisterCustomerModelAndView(form, null);
		return result;
	}
	
	protected ModelAndView createRegisterAdministratorModelAndView(RegisterAdministratorForm form) {
		ModelAndView result;
		result = this.createRegisterAdministratorModelAndView(form, null);
		return result;
	}

	@SuppressWarnings("deprecation")
	protected ModelAndView createRegisterAdministratorModelAndView(RegisterAdministratorForm form, String messageCode) {
		ModelAndView res;
		
		Date d = new Date();
		Collection <Integer> months = new ArrayList<>();
		Collection <Integer> years = new ArrayList<>();
		for (int i = 0; i < 11; i++) {
			months.add(i+1);
			years.add(d.getYear()+i+1900);
		}
			
		System.out.println("Creo las vista de registrar administrator");
		res = new ModelAndView("actor/registerAdministrator");
		res.addObject("registerForm", form);
		res.addObject("months", months);
		res.addObject("years", years);
		res.addObject("message", messageCode);
		return res;
	}
	
	@SuppressWarnings("deprecation")
	protected ModelAndView createRegisterManagerModelAndView(RegisterManagerForm form, String messageCode) {
		ModelAndView res;
		
		Date d = new Date();
		Collection <Integer> months = new ArrayList<>();
		Collection <Integer> years = new ArrayList<>();
		for (int i = 0; i < 11; i++) {
			months.add(i+1);
			years.add(d.getYear()+i+1900);
		}
			
		System.out.println("Creo las vista de registrar manager");
		res = new ModelAndView("actor/registerManager");
		res.addObject("registerForm", form);
		res.addObject("months", months);
		res.addObject("years", years);
		res.addObject("message", messageCode);
		return res;
	}
	
	@SuppressWarnings("deprecation")
	protected ModelAndView createRegisterCustomerModelAndView(RegisterCustomerForm form, String messageCode) {
		ModelAndView res;
		
		Date d = new Date();
		Collection <Integer> months = new ArrayList<>();
		Collection <Integer> years = new ArrayList<>();
		for (int i = 0; i < 11; i++) {
			months.add(i+1);
			years.add(d.getYear()+i+1900);
		}
			
		System.out.println("Creo las vista de registrar customer");
		res = new ModelAndView("actor/registerCustomer");
		res.addObject("registerForm", form);
		res.addObject("months", months);
		res.addObject("years", years);
		res.addObject("message", messageCode);
		return res;
	}
	
}
