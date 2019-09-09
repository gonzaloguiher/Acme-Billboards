package controllers.contract;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ContractService;
import services.ManagerService;

import controllers.AbstractController;
import domain.Contract;
import domain.Manager;

@Controller
@RequestMapping("/contract/manager")
public class ContractManagerController extends AbstractController {

	@Autowired
	private ContractService contractService;

	@Autowired
	private ManagerService managerService;

	// Show-------------------------------------------------------------------------------------------------

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam final int contractId) {

		ModelAndView result;

		Contract contract = contractService.findOne(contractId);
		
		Manager logged = managerService.findByPrincipal();

		if (logged.equals(contract.getPakage().getManager())) {
			result = new ModelAndView("contract/show");
			result.addObject("contract", contract);
			result.addObject("requestURI", "contract/manager/show.do");
			result.addObject("files", contract.getFiles());
		} else {
			result = new ModelAndView("error/access");
		}
		return result;
	}

	// List -------------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;

		Manager manager = managerService.findByPrincipal();

		Collection<Contract> contracts = contractService.getContractsByManager(manager.getId());

		result = new ModelAndView("contract/list");
		result.addObject("contracts", contracts);
		result.addObject("requestURI", "contract/manager/list.do");

		return result;
	}

	// Edit --------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam int contractId) {
		ModelAndView result;

		Contract contract = contractService.findOne(contractId);
		Manager logged = managerService.findByPrincipal();

		if (logged.equals(contract.getPakage().getManager()) && contract.getSignManager() == null) {
			result = createEditModelAndView(contract);
		} else {
			result = new ModelAndView("error/access");
		}

		return result;
	}

	// Sign --------------------------------------------------------------------

	@RequestMapping(value = "/sign", method = RequestMethod.GET)
	public ModelAndView sign(@RequestParam int contractId) {
		
		ModelAndView result;

		Contract contract = contractService.findOne(contractId);
		Manager logged = managerService.findByPrincipal();

		if (logged.equals(contract.getPakage().getManager()) && contract.getSignManager() == null) {
			result = createEditModelAndView2(contract);
		} else {
			result = new ModelAndView("error/access");
		}

		return result;
	}

	// Save ---------------------------------------------------------------------

	@RequestMapping(value = "/edit", params = "save", method = RequestMethod.POST)
	public ModelAndView edit(final Contract contract, final BindingResult bindingResult) {
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

	// Sign -----------------------------------------------------------------------

	@RequestMapping(value = "/sign", params = "save", method = RequestMethod.POST)
	public ModelAndView sign(final Contract contract, final BindingResult bindingResult) {

		ModelAndView result;

		if (bindingResult.hasErrors())
			result = this.createEditModelAndView2(contract);
		else
			try {
				Contract res = contractService.reconstruct(contract, bindingResult);
				this.contractService.save(res);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView2(contract, "contract.commit.error");
			}
		return result;
	}

	protected ModelAndView createEditModelAndView(Contract contract) {
		
		ModelAndView result = createEditModelAndView(contract, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(Contract contract, String messageCode) {

		ModelAndView result;

		result = new ModelAndView("contract/edit");
		result.addObject("contract", contract);
		result.addObject("message", messageCode);

		return result;
	}
	
	protected ModelAndView createEditModelAndView2(Contract contract) {
		
		ModelAndView result = createEditModelAndView2(contract, null);
		return result;
	}

	protected ModelAndView createEditModelAndView2(Contract contract, String messageCode) {

		ModelAndView result;

		result = new ModelAndView("contract/sign");
		result.addObject("contract", contract);
		result.addObject("message", messageCode);

		return result;
	}
}
