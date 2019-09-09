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

import security.Authority;
import security.UserAccount;
import security.UserAccountService;
import services.ManagerService;

import controllers.AbstractController;
import domain.Manager;
import forms.ManagerForm;

@Controller
@RequestMapping("/actor/manager")
public class ProfileManagerController extends AbstractController {

	@Autowired
	private ManagerService managerService;
	
	@Autowired
	private UserAccountService userAccountService;

	@Autowired
	private Validator validator;

	// Edit -----------------------------------------------------------------

	@RequestMapping(value = "/editPersonal", method = RequestMethod.GET)
	public ModelAndView editPersonal() {

		ModelAndView result = new ModelAndView();
		ManagerForm managerForm = new ManagerForm();
		Manager manager = managerService.findByPrincipal();
		ManagerForm form = managerService.rellenaForm(manager, managerForm, new ArrayList<String>());

		result = createEditModelAndView(form, "personal");
		result.addObject("authority", Authority.MANAGER);
		result.addObject("actionURL", "actor/manager/editPersonal.do");
		return result;
	}

	@RequestMapping(value = "/editUserAccount", method = RequestMethod.GET)
	public ModelAndView editAccount() {

		ModelAndView result = new ModelAndView();
		ManagerForm managerForm = new ManagerForm();
		Manager manager = managerService.findByPrincipal();
		ManagerForm form = managerService.rellenaForm(manager, managerForm, new ArrayList<String>());

		result = createEditModelAndView(form, "account");
		result.addObject("authority", Authority.MANAGER);
		result.addObject("actionURL", "actor/manager/editUserAccount.do");
		return result;
	}

	// Save Personal Data -----------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "savePersonal")
	public ModelAndView savePersonal(ManagerForm managerForm, final BindingResult binding) {
		ModelAndView result;

		Manager manager = managerService.findByPrincipal();

		List<String> aux = new ArrayList<>();
		aux.add("account");
		ManagerForm form = managerService.rellenaForm(manager, managerForm, aux);

		validator.validate(form, binding);

		if (binding.hasErrors()) {
			System.out.println(binding);
			result = this.createEditModelAndView(form, "personal");
			return result;
		} else {
			try {

				manager.setName(form.getName());
				manager.setMiddleName(form.getMiddleName());
				manager.setSurname(form.getSurname());
				manager.setAddress(form.getAddress());
				manager.setEmail(form.getEmail());
				manager.setPhone(form.getPhone());
				manager.setPhoto(form.getPhoto());

				managerService.save(manager);

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
	public ModelAndView saveAccount(ManagerForm managerForm, final BindingResult binding) {
		ModelAndView result;
		Boolean passMatch = false;
		Manager manager = managerService.findByPrincipal();

		List<String> aux = new ArrayList<>();
		aux.add("personal");
		
		ManagerForm form = managerService.rellenaForm(manager, managerForm, aux);

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
				UserAccount account = managerService.findByPrincipal().getUserAccount();
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
	
	protected ModelAndView createEditModelAndView(final ManagerForm form, String type) {
		
		ModelAndView result = this.createEditModelAndView(form, type ,null);
		return result;
	}

	@SuppressWarnings("deprecation")
	private ModelAndView createEditModelAndView(final ManagerForm form, String type, final String message) {

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