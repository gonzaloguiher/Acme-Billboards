package controllers.contract;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ContractService;
import services.CustomerService;
import services.RequestService;

import controllers.AbstractController;
import domain.Contract;
import domain.Customer;
import domain.Request;

@Controller
@RequestMapping("/contract/customer")
public class ContractCustomerController extends AbstractController {

	@Autowired
	private RequestService requestService;

	@Autowired
	private ContractService contractService;

	@Autowired
	private CustomerService customerService;

	// Show--------------------------------------------------------------------

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam final int contractId) {

		ModelAndView result;

		Contract contract = contractService.findOne(contractId);

		result = new ModelAndView("contract/show");
		result.addObject("contract", contract);
		result.addObject("requestURI", "contract/customer/show.do");
		result.addObject("files", contract.getFiles());
		return result;
	}

	// List -------------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;

		Customer customer = customerService.findByPrincipal();

		Collection<Contract> contracts = new ArrayList<>();

		for (Request request : requestService.getRequestsByCustomer(customer.getId())) {
			if (request.getContract() != null && request.getContract().getSignManager() != null) {
				contracts.add(request.getContract());
			}
		}

		result = new ModelAndView("contract/list");
		result.addObject("contracts", contracts);
		result.addObject("requestURI", "contract/customer/list.do");

		return result;
	}

	// Sign -------------------------------------------------------------------

	@RequestMapping(value = "/sign", method = RequestMethod.GET)
	public ModelAndView sign(@RequestParam int contractId) {
		
		ModelAndView result = null;

		Contract contract = contractService.findOne(contractId);
		Customer logged = customerService.findByPrincipal();

		for (Request request : requestService.getRequestsByCustomer(logged.getId())) {
			if (request.getContract() != null && request.getContract().equals(contract)) {
				if (request.getContract() != null && request.getContract().getSignManager() != null && request.getContract().getSignCustomer() == null && request.getContract().equals(contract)) {
					result = createEditModelAndView(contract);
				} else {
					result = new ModelAndView("error/access");
				}
			}
		}

		return result;
	}
	
	// Sign ------------------------------------------------------------------

	@RequestMapping(value = "/sign", params = "save", method = RequestMethod.POST)
	public ModelAndView sign(final Contract contract, final BindingResult bindingResult) {

		ModelAndView result;

		if (bindingResult.hasErrors())
			result = this.createEditModelAndView(contract);
		else
			try {
				Contract res = contractService.reconstruct(contract, bindingResult);
				this.contractService.save(res);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(contract, "contract.commit.error");
			}
		return result;
	}

	protected ModelAndView createEditModelAndView(Contract contract) {

		ModelAndView result = createEditModelAndView(contract, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(Contract contract, String messageCode) {

		ModelAndView result;

		result = new ModelAndView("contract/sign");
		result.addObject("contract", contract);
		result.addObject("message", messageCode);
		
		return result;
	}
}
