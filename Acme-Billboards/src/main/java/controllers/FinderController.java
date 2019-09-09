package controllers;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.FinderService;
import services.PackageService;

import domain.Finder;

@Controller
@RequestMapping("/finder")
public class FinderController extends AbstractController {

	@Autowired
	private FinderService finderService;

	@Autowired
	private PackageService packageService;

	@RequestMapping(value = "/filter", method = RequestMethod.GET)
	public ModelAndView filter() {
		
		ModelAndView result = createEditModelAndView(finderService.findByPrincipal());
		System.out.println("Finder Results: " + finderService.findByPrincipal().getPackages());
		return result;
	}

	@RequestMapping(value = "/filter", method = RequestMethod.POST, params = "filter")
	public ModelAndView filter(Finder finder, final BindingResult binding) {
		ModelAndView result;
		if (binding.hasErrors()) {
			result = createEditModelAndView(finder);
		} else {
			try {
				Finder updatedFinder = finderService.save(finder);
				result = createEditModelAndView(updatedFinder);
			} catch (final Throwable oops) {
				oops.printStackTrace();
				result = createEditModelAndView(finder, "finder.commit.error");
			}
		}
		return result;
	}

	@RequestMapping(value = "/filter", method = RequestMethod.POST, params = "clear")
	public ModelAndView clear(Finder finder, final BindingResult binding) {
		ModelAndView result;
		if (binding.hasErrors()) {
			result = createEditModelAndView(finder);
		} else {
			try {
				Finder clearedFinder = finderService.clear(finder);
				result = createEditModelAndView(clearedFinder);
			} catch (final Throwable oops) {
				oops.printStackTrace();
				result = createEditModelAndView(finder, "finder.commit.error");
			}
		}
		return result;
	}

	protected ModelAndView createEditModelAndView(Finder finder) {
		
		ModelAndView result = createEditModelAndView(finder, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(Finder finder, String messageCode) {
		
		Collection<domain.Package> packages = new HashSet<domain.Package>();
		String cachedMessageCode = null;

		ModelAndView result = new ModelAndView("finder/edit");

		if (finderService.findOne(finder.getId()).getMoment() == null || finderService.isVoid(finder) || finderService.isExpired(finder)) {
			packages.addAll(packageService.findAll());
		} else {
			packages.addAll(finderService.findOne(finder.getId()).getPackages());
			cachedMessageCode = "finder.cachedMessage";
		}
		result.addObject("cachedMessage", cachedMessageCode);
		result.addObject("finder", finder);
		result.addObject("packages", packages);
		result.addObject("message", messageCode);
		return result;
	}

}
