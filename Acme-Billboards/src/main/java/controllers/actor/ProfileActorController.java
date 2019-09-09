package controllers.actor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountService;
import services.ActorService;
import services.AdministratorService;
import services.CreditCardService;
import services.CustomerService;
import services.ManagerService;
import controllers.AbstractController;
import domain.Actor;
import domain.Administrator;
import domain.CreditCard;
import domain.Customer;
import domain.Manager;
import forms.ProfileForm;

@Controller
@RequestMapping("actor/")
public class ProfileActorController extends AbstractController {

	// Services ----------------------------------------------------------------

	@Autowired
	private ActorService actorService;

	@Autowired
	private AdministratorService administratorService;

	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private ManagerService managerService;
	
	@Autowired
	private CreditCardService creditCardService;
	
	@Autowired
	private UserAccountService userAccountService;
	
	@Autowired
	private Validator validator;

	// Show --------------------------------------------------------------------

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam(required = false) Integer actorId) {

		ModelAndView result;

		// Diferentes autoridades:

		Authority adminAuth = new Authority();
		adminAuth.setAuthority("ADMIN");

		Authority customerAuth = new Authority();
		customerAuth.setAuthority("CUSTOMER");

		Authority managerAuth = new Authority();
		managerAuth.setAuthority("MANAGER");

		Boolean isCustomer = false;
		Boolean isManager = false;
		Boolean isAdmin = false;

		result = new ModelAndView("actor/show");

		if (actorId != null) { // accedemos al perfil de otro actor
			Actor actor = actorService.findOne(actorId);
			
			result.addObject("actor", actor);
			result.addObject("logged", false);

			if (actor.getUserAccount().getAuthorities().contains(customerAuth)) {
				isCustomer = true;
			}

			if (actor.getUserAccount().getAuthorities().contains(managerAuth)) {
				isManager = true;
			}

			if (actor.getUserAccount().getAuthorities().contains(adminAuth)) {
				isAdmin = true;
			}

		} else {

			Actor actor = actorService.getByUserAccount(LoginService.getPrincipal());
			result.addObject("logged", true);

			if (actor.getUserAccount().getAuthorities().contains(adminAuth)) {
				isAdmin = true;
				Administrator administrator = administratorService.findOne(actor.getId());
				result.addObject("actor", administrator);
			}

			if (actor.getUserAccount().getAuthorities().contains(customerAuth)) {
				isCustomer = true;
				Customer customer = customerService.findOne(actor.getId());
				result.addObject("actor", customer);
				result.addObject("creditCard", customer.getCreditCard());
			}

			if (actor.getUserAccount().getAuthorities().contains(managerAuth)) {
				isManager = true;
				Manager manager = managerService.findOne(actor.getId());
				result.addObject("actor", manager);
			}

		}

		result.addObject("isAdmin", isAdmin);
		result.addObject("isManager", isManager);
		result.addObject("isCustomer", isCustomer);
		result.addObject("requestURI", "actor/show.do");

		return result;
	}

	// Edit -----------------------------------------------------------------

	@RequestMapping(value = "/editPersonal", method = RequestMethod.GET)
	public ModelAndView editPersonal() {

		ModelAndView result = new ModelAndView();
		ProfileForm profileForm = new ProfileForm();
		Actor actor = actorService.getByUserAccount(LoginService.getPrincipal());
		ProfileForm form = actorService.rellenaForm(actor, profileForm, new ArrayList<String>(), null);

		result = createEditModelAndView2(form, "personal");
		return result;
	}

	@RequestMapping(value = "/editCreditCard", method = RequestMethod.GET)
	public ModelAndView editCredit() {

		ModelAndView result = new ModelAndView();
		ProfileForm profileForm = new ProfileForm();
		Actor actor = actorService.getByUserAccount(LoginService.getPrincipal());
		ProfileForm form = actorService.rellenaForm(actor, profileForm, new ArrayList<String>(), null);
		
		result = createEditModelAndView2(form, "credit");
		return result;
	}

	@RequestMapping(value = "/editUserAccount", method = RequestMethod.GET)
	public ModelAndView editAccount() {

		ModelAndView result = new ModelAndView();
		ProfileForm profileForm = new ProfileForm();
		Actor actor = actorService.getByUserAccount(LoginService.getPrincipal());
		ProfileForm form = actorService.rellenaForm(actor, profileForm, new ArrayList<String>(), null);
		
		result = createEditModelAndView2(form, "account");
		return result;
	}

	// Save Personal Data -----------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "savePersonal")
	public ModelAndView savePersonal(@ModelAttribute("profileForm") ProfileForm profileForm, final BindingResult binding) {
		ModelAndView result;

		Actor actor = actorService.getByUserAccount(LoginService.getPrincipal());
		Authority authority = new Authority();
		authority.setAuthority(Authority.CUSTOMER);

		List<String> aux = new ArrayList<>();
		aux.add("account");
		aux.add("credit");
		
		ProfileForm form = actorService.rellenaForm(actor, profileForm, aux, binding);
		
		if (!actor.getUserAccount().getAuthorities().contains(authority)) {
			form.setVatNumber("default");
		}

		validator.validate(form, binding);

		if (binding.hasErrors()) {
			System.out.println(binding);
			result = this.createEditModelAndView2(form, "personal");
			return result;
		} else {
			try {

				if (actor.getUserAccount().getAuthorities().contains(authority)) {
					Customer customer = customerService.findByPrincipal();
					customer.setName(form.getName());
					customer.setSurname(form.getSurname());
					customer.setAddress(form.getAddress());
					customer.setEmail(form.getEmail());
					customer.setPhone(form.getPhone());
					customer.setPhoto(form.getPhoto());
					customer.setVatNumber(form.getVatNumber());
					customer.setMiddleName(form.getMiddleName());
					customerService.save(customer);
					
				} else {
					
					actor.setName(form.getName());
					actor.setMiddleName(form.getMiddleName());
					actor.setSurname(form.getSurname());
					actor.setAddress(form.getAddress());
					actor.setEmail(form.getEmail());
					actor.setPhone(form.getPhone());
					actor.setPhoto(form.getPhoto());
					actorService.save(actor);
				}

				result = new ModelAndView("redirect:show.do");
				return result;

			} catch (final ValidationException oops) {
				System.out.println(oops.getStackTrace());
				oops.printStackTrace();
				result = this.createEditModelAndView2(profileForm, "profileForm.commit.error");
				return result;
				
			} catch (final Throwable oops) {
				System.out.println(oops.getStackTrace());
				oops.printStackTrace();
				result = this.editPersonal();
				return result;
			}
		}
	}

	// UserAccount

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "saveAccount")
	public ModelAndView saveAccount(@ModelAttribute("profileForm") ProfileForm profileForm, final BindingResult binding) {
		ModelAndView result;
		Boolean passMatch = false;
		Actor actor = actorService.getByUserAccount(LoginService.getPrincipal());

		List<String> aux = new ArrayList<>();
		aux.add("credit");
		aux.add("personal");
		ProfileForm form = actorService.rellenaForm(actor, profileForm, aux, binding);

		if (form.getPassword().equals(form.getPassword2())) {
			passMatch = true;
		}

		validator.validate(form, binding);

		if (binding.hasErrors() || !passMatch) {
			System.out.println(binding);
			result = this.createEditModelAndView2(form, "account");
			result.addObject("passMatch", passMatch);
			return result;
		} else {
			try {
				UserAccount account = actorService.getByUserAccount(LoginService.getPrincipal()).getUserAccount();
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

	// UserAccount

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "saveCredit")
	public ModelAndView saveCreditCard(@ModelAttribute("profileForm") ProfileForm profileForm, final BindingResult binding) {
		ModelAndView result;

		Boolean expired = false;
		Calendar cal = Calendar.getInstance();
		Actor a = actorService.getByUserAccount(LoginService.getPrincipal());

		List<String> aux = new ArrayList<>();
		aux.add("account");
		aux.add("personal");
		ProfileForm form = actorService.rellenaForm(a, profileForm, aux, binding);

		if (form.getExpirationYear() == cal.get(Calendar.YEAR) && form.getExpirationMonth() <= cal.get(Calendar.MONTH)) {
			expired = true;
		}

		validator.validate(form, binding);

		if (binding.hasErrors() || expired) {
			System.out.println(binding);
			result = this.createEditModelAndView2(form, "credit");
			result.addObject("isExpired", expired);

			return result;
		} else {
			try {
				
				CreditCard credit = customerService.findByPrincipal().getCreditCard();
				Calendar calendar = Calendar.getInstance();
				calendar.set(form.getExpirationYear(), form.getExpirationMonth(), 1);
				Date date = calendar.getTime();

				credit.setCVV(form.getCVV());
				credit.setExpirationDate(date);
				credit.setHolder(form.getHolder());
				credit.setMake(form.getMake());
				credit.setNumber(form.getNumber());

				creditCardService.save(credit);

				result = new ModelAndView("redirect:show.do");
				return result;

			} catch (final Throwable oops) {
				oops.printStackTrace();
				result = this.editCredit();
				return result;
			}
		}
	}

	// Generate JSON ----------------------------------------------------

	@RequestMapping(value = "/generateData", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView exportData(final HttpServletResponse response) {
		ModelAndView result;
		try {
			final StringBuilder sb = this.actorService.exportData();
			response.setContentType("text/csv");
			response.setHeader("Content-Disposition",
					"attachment;filename=data.csv");
			final ServletOutputStream outStream = response.getOutputStream();
			outStream.println(sb.toString());
			outStream.flush();
			outStream.close();
			result = new ModelAndView("redirect:/welcome/index.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(null, "commit.error");
		}

		return result;
	}

	protected ModelAndView createEditModelAndView(final Actor actor) {
		ModelAndView result;
		result = this.createEditModelAndView(actor, null);
		return result;
	}

	private ModelAndView createEditModelAndView(final Actor actor, final String message) {

		ModelAndView result;
		ProfileForm profileForm = new ProfileForm();

		Authority authority = new Authority();
		authority.setAuthority("ADMIN");

		Authority authority2 = new Authority();
		authority2.setAuthority("CUSTOMER");

		Authority authority3 = new Authority();
		authority3.setAuthority("MANAGER");

		result = new ModelAndView("actor/edit");

		if (actor.getUserAccount().getAuthorities().contains(authority)) {
			result.addObject("type", "administrator");
		}

		if (actor.getUserAccount().getAuthorities().contains(authority2)) {
			result.addObject("type", "customer");
		}

		if (actor.getUserAccount().getAuthorities().contains(authority3)) {
			result.addObject("type", "manager");
		}

		profileForm.setUsername(actor.getUserAccount().getUsername());
		profileForm.setName(actor.getName());
		profileForm.setMiddleName(actor.getMiddleName());
		profileForm.setSurname(actor.getSurname());
		profileForm.setAddress(actor.getAddress());
		profileForm.setPhone(actor.getPhone());
		profileForm.setPhoto(actor.getPhoto());
		profileForm.setEmail(actor.getEmail());
		
		result.addObject("message", message);
		result.addObject("profileForm", profileForm);

		return result;
	}
	
	protected ModelAndView createEditModelAndView2(final ProfileForm form, String type) {
		
		ModelAndView result = this.createEditModelAndView2(form, type ,null);
		return result;
	}

	@SuppressWarnings("deprecation")
	private ModelAndView createEditModelAndView2(final ProfileForm form, String type, final String message) {

		ModelAndView result = new ModelAndView("actor/edit");
		
		Authority authority = new Authority();
		authority.setAuthority(Authority.CUSTOMER);
		
		
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
		result.addObject("showCredit",   type=="credit");
		result.addObject("showPersonal", type=="personal");
		result.addObject("isCustomer", LoginService.getPrincipal().getAuthorities().contains(authority));
		result.addObject("months",months);
		result.addObject("years",years);
		
		return result;
	}
	
}