package controllers.profile;

import java.util.ArrayList;
import java.util.Calendar;
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
import services.CreditCardService;
import services.CustomerService;

import controllers.AbstractController;
import domain.CreditCard;
import domain.Customer;
import forms.CustomerForm;

@Controller
@RequestMapping("/actor/customer")
public class ProfileCustomerController extends AbstractController {

	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private CreditCardService creditCardService;
	
	@Autowired
	private UserAccountService userAccountService;

	@Autowired
	private Validator validator;

	// Edit -----------------------------------------------------------------

	@RequestMapping(value = "/editPersonal", method = RequestMethod.GET)
	public ModelAndView editPersonal() {

		ModelAndView result = new ModelAndView();
		CustomerForm customerForm = new CustomerForm();
		Customer customer = customerService.findByPrincipal();
		CustomerForm form = customerService.rellenaForm(customer, customerForm, new ArrayList<String>());

		result = createEditModelAndView(form, "personal");
		result.addObject("authority", Authority.CUSTOMER);
		result.addObject("actionURL", "actor/customer/editPersonal.do");
		return result;
	}

	@RequestMapping(value = "/editUserAccount", method = RequestMethod.GET)
	public ModelAndView editAccount() {

		ModelAndView result = new ModelAndView();
		CustomerForm customerForm = new CustomerForm();
		Customer customer = customerService.findByPrincipal();
		CustomerForm form = customerService.rellenaForm(customer, customerForm, new ArrayList<String>());

		result = createEditModelAndView(form, "account");
		result.addObject("authority", Authority.CUSTOMER);
		result.addObject("actionURL", "actor/customer/editUserAccount.do");
		return result;
	}

	@RequestMapping(value = "/editCreditCard", method = RequestMethod.GET)
	public ModelAndView editCredit() {

		ModelAndView result = new ModelAndView();
		CustomerForm customerForm = new CustomerForm();
		Customer customer = customerService.findByPrincipal();
		CustomerForm form = customerService.rellenaForm(customer, customerForm, new ArrayList<String>());

		result = createEditModelAndView(form, "credit");
		result.addObject("authority", Authority.CUSTOMER);
		result.addObject("actionURL", "actor/customer/editCreditCard.do");
		return result;
	}

	// Save Personal Data -----------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "savePersonal")
	public ModelAndView savePersonal(CustomerForm customerForm, final BindingResult binding) {
		ModelAndView result;

		Customer customer = customerService.findByPrincipal();

		List<String> aux = new ArrayList<>();
		aux.add("account");
		aux.add("credit");

		CustomerForm form = customerService.rellenaForm(customer, customerForm, aux);

		validator.validate(form, binding);

		if (binding.hasErrors()) {
			System.out.println(binding);
			result = this.createEditModelAndView(form, "personal");
			return result;
		} else {
			try {

				customer.setAddress(form.getAddress());
				customer.setEmail(form.getEmail());
				customer.setName(form.getName());
				customer.setPhone(form.getPhone());
				customer.setPhoto(form.getPhoto());
				customer.setSurname(form.getSurname());
				customer.setVatNumber(form.getVatNumber());

				customerService.save(customer);

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
	public ModelAndView saveAccount(CustomerForm customerForm, final BindingResult binding) {
		ModelAndView result;
		
		Boolean passMatch = false;
		Customer customer = customerService.findByPrincipal();

		List<String> aux = new ArrayList<>();
		aux.add("credit");
		aux.add("personal");
		
		CustomerForm form = customerService.rellenaForm(customer, customerForm, aux);

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
				UserAccount account = customerService.findByPrincipal().getUserAccount();
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

	// Save CreditCard -----------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "saveCredit")
	public ModelAndView saveCreditCard(CustomerForm customerForm, final BindingResult binding) {
		ModelAndView result;

		Boolean expired = false;
		Calendar cal = Calendar.getInstance();
		Customer customer = customerService.findByPrincipal();

		List<String> aux = new ArrayList<>();
		aux.add("account");
		aux.add("personal");
		
		CustomerForm form = customerService.rellenaForm(customer, customerForm, aux);

		if (form.getExpirationYear() == cal.get(Calendar.YEAR) && form.getExpirationMonth() <= cal.get(Calendar.MONTH)) {
			expired = true;
		}

		validator.validate(form, binding);

		if (binding.hasErrors() || expired) {
			System.out.println(binding);
			result = this.createEditModelAndView(form, "credit");
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

				result = new ModelAndView("redirect:/actor/show.do");
				return result;

			} catch (final Throwable oops) {
				oops.printStackTrace();
				result = this.editCredit();
				return result;
			}
		}
	}

	protected ModelAndView createEditModelAndView(final CustomerForm form, String type) {

		ModelAndView result = this.createEditModelAndView(form, type, null);
		return result;
	}

	@SuppressWarnings("deprecation")
	private ModelAndView createEditModelAndView(final CustomerForm form, String type, final String message) {

		ModelAndView result = new ModelAndView("actor/edit");

		Date d = new Date();
		Collection<Integer> months = new ArrayList<>();
		Collection<Integer> years = new ArrayList<>();

		for (int i = 0; i < 11; i++) {
			months.add(i + 1);
			years.add(d.getYear() + i + 1900);
		}

		result.addObject("message", message);
		result.addObject("profileForm", form);
		result.addObject("showAccount",  type == "account");
		result.addObject("showCredit",   type == "credit");
		result.addObject("showPersonal", type == "personal");
		result.addObject("months", months);
		result.addObject("years", years);
		return result;
	}

}
