package controllers.profile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import controllers.AbstractController;
import domain.Administrator;
import forms.AdministratorForm;

import security.Authority;
import security.UserAccount;
import security.UserAccountService;
import services.AdministratorService;

@Controller
@RequestMapping("/actor/administrator")
public class ProfileAdministratorController extends AbstractController {

	@Autowired
	private AdministratorService administratorService;
	
	@Autowired
	private UserAccountService userAccountService;

	@Autowired
	private Validator validator;

	// Edit -----------------------------------------------------------------

	@RequestMapping(value = "/editPersonal", method = RequestMethod.GET)
	public ModelAndView editPersonal() {

		ModelAndView result = new ModelAndView();
		AdministratorForm administratorForm = new AdministratorForm();
		Administrator administrator = administratorService.findByPrincipal();
		AdministratorForm form = administratorService.rellenaForm(administrator, administratorForm, new ArrayList<String>());

		result = createEditModelAndView(form, "personal");
		result.addObject("authority", Authority.ADMIN);
		result.addObject("actionURL", "actor/administrator/editPersonal.do");
		return result;
	}

	@RequestMapping(value = "/editUserAccount", method = RequestMethod.GET)
	public ModelAndView editAccount() {

		ModelAndView result = new ModelAndView();
		AdministratorForm administratorForm = new AdministratorForm();
		Administrator administrator = administratorService.findByPrincipal();
		AdministratorForm form = administratorService.rellenaForm(administrator, administratorForm, new ArrayList<String>());

		result = createEditModelAndView(form, "account");
		result.addObject("authority", Authority.ADMIN);
		result.addObject("actionURL", "actor/administrator/editUserAccount.do");
		return result;
	}

	// Save Personal Data -----------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "savePersonal")
	public ModelAndView savePersonal(AdministratorForm administratorForm, final BindingResult binding) {
		ModelAndView result;

		Administrator administrator = administratorService.findByPrincipal();

		List<String> aux = new ArrayList<>();
		aux.add("account");
		AdministratorForm form = administratorService.rellenaForm(administrator, administratorForm, aux);

		validator.validate(form, binding);

		if (binding.hasErrors()) {
			System.out.println(binding);
			result = this.createEditModelAndView(form, "personal");
			return result;
		} else {
			try {

				administrator.setName(form.getName());
				administrator.setMiddleName(form.getMiddleName());
				administrator.setSurname(form.getSurname());
				administrator.setAddress(form.getAddress());
				administrator.setEmail(form.getEmail());
				administrator.setPhone(form.getPhone());
				administrator.setPhoto(form.getPhoto());

				administratorService.save(administrator);

				result = new ModelAndView("redirect:/actor/show.do");
				return result;

			} catch (final Throwable oops) {
				oops.printStackTrace();
				result = this.editPersonal();
				return result;
			}
		}
	}

	// Save UserAccount -----------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "saveAccount")
	public ModelAndView saveAccount(AdministratorForm administratorForm, final BindingResult binding) {
		ModelAndView result;
		Boolean passMatch = false;
		Administrator administrator = administratorService.findByPrincipal();

		List<String> aux = new ArrayList<>();
		aux.add("personal");
		
		AdministratorForm form = administratorService.rellenaForm(administrator, administratorForm, aux);

		if (form.getPassword().equals(form.getPassword2())) {
			passMatch = true;
		}

		validator.validate(form, binding);
		
		if (binding.hasErrors() || !passMatch) {
			System.out.println(binding);
			result = this.createEditModelAndView(form, "account");
			result.addObject("passMatch", passMatch);
			return result;
		} else {
			try {
				UserAccount account = administratorService.findByPrincipal().getUserAccount();
				account.setPassword(form.getPassword());
				account.setUsername(form.getUsername());
				userAccountService.save(account);
				result = new ModelAndView("redirect:/j_spring_security_logout");
				return result;

			} catch (final Throwable oops) {
				oops.printStackTrace();
				result = this.editAccount();
				return result;
			}
		}
	}
	
	protected ModelAndView createEditModelAndView(final AdministratorForm form, String type) {
		
		ModelAndView result = this.createEditModelAndView(form, type ,null);
		return result;
	}

	@SuppressWarnings("deprecation")
	private ModelAndView createEditModelAndView(final AdministratorForm form, String type, final String message) {

		ModelAndView result = new ModelAndView("actor/edit");
		
		Date d = new Date();
		Collection <Integer> months = new ArrayList<>();
		Collection <Integer> years = new ArrayList<>();
		
		for (int i = 0; i < 11; i++) {
			months.add(i+1);
			years.add(d.getYear()+i+1900);
		}
		
		result.addObject("message", message);
		result.addObject("profileForm", form);
		result.addObject("showAccount",  type=="account");
		result.addObject("showPersonal", type=="personal");
		result.addObject("months",months);
		result.addObject("years",years);
		
		return result;
	}
}