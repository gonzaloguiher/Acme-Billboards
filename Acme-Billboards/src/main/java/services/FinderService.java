package services;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Configuration;
import domain.Customer;
import domain.Finder;

import repositories.FinderRepository;

@Service
@Transactional
public class FinderService {

	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private FinderRepository finderRepository;
	
	@Autowired
	private ConfigurationService configurationService;

	public Finder create() {

		Finder result = new Finder();
		result.setPackages(new HashSet<domain.Package>());
		return result;
	}

	public Collection<Finder> findAll() {
		Collection<Finder> result = finderRepository.findAll();
		return result;
	}

	public Finder findOne(int finderId) {
		Finder result = this.finderRepository.findOne(finderId);
		return result;
	}

	public Finder findByPrincipal() {
		
		Customer principal = customerService.findByPrincipal();
		Finder result = findByCustomer(principal);
		
		if (result.getMoment() == null || isVoid(result) || isExpired(result)) {
			result = setAllToParametersToNullAndSave(result);
		}
		return result;
	}

	public Finder save(Finder finder) {
		Finder result;
		Assert.notNull(finder);
		
		if (finder.getId() != 0) {
			Assert.isTrue(this.esDeActorActual(finder));
			if (isVoid(finder)) {
				finder.setMoment(null);
				finder.setPackages(new HashSet<domain.Package>());
			} else {
				finder.setMoment(DateUtils.addMilliseconds(new Date(), -1));
				filterPackages(finder);
			}
		} else {
			Assert.isNull(findByCustomer(finder.getCustomer()));
			Assert.isTrue(isVoid(finder));
			Assert.isNull(finder.getMoment());
		}
		result = this.finderRepository.save(finder);
		return result;
	}

	public Finder clear(Finder finder) {
		
		Assert.notNull(finder);
		Assert.isTrue(this.esDeActorActual(finder));
		
		finder.setMoment(null);
		return this.setAllToParametersToNullAndSave(finder);
	}

	public void delete(Finder finder) {
		
		Assert.notNull(finder);
		Assert.isTrue(finder.getId() != 0);
		
		this.finderRepository.delete(finder);
	}
	
	public Finder findByCustomer(Customer customer) {
		
		Finder result = finderRepository.findByCustomer(customer.getId());
		return result;
	}
	
	private Boolean esDeActorActual(final Finder finder) {
		Boolean result;

		final Customer principal = this.customerService.findByPrincipal();
		final Customer customerFromFinder = finderRepository.findOne(finder.getId()).getCustomer();

		result = principal.equals(customerFromFinder);
		return result;
	}

	public Boolean isVoid(final Finder finder) {
		
		Boolean result = (finder.getKeyword() == null || finder.getKeyword() == "");

		return result;
	}

	private Finder setAllToParametersToNullAndSave(Finder finder) {
		
		Assert.isTrue(finder.getMoment() == null || isExpired(finder));
		
		Finder result;

		finder.setMoment(null);
		finder.setKeyword(null);
		finder.setPackages(null);

		result = save(finder);
		return result;
	}

	public Boolean isExpired(Finder finder) {
		
		Boolean result = true;
		
		Configuration configuration = configurationService.find();
		Double cacheTimeInHours = configuration.getFinderCacheTime();
		
		Date expirationMoment = new Date();
		/* Adding Hours */
		expirationMoment = DateUtils.addHours(expirationMoment, cacheTimeInHours.intValue());
		/* Adding Minutes */
		expirationMoment = DateUtils.addMinutes(expirationMoment, Double.valueOf(60 * (cacheTimeInHours - cacheTimeInHours.intValue())).intValue());

		result = finder.getMoment().after(expirationMoment);
		return result;
	}
	
	/*Don't declare finder parameter as final*/
	public Finder filterPackages(Finder finder) {
		
		String keyword = finder.getKeyword();

		Collection<domain.Package> packages = finderRepository.filterPackages(keyword);
		
		finder.setPackages(packages);
		return finder;
	}
	
	public Double getAvgResultsPerFinder() {
		Double result = finderRepository.getAvgResultsPerFinder();
		if(result == null) result = 0d;
		return result;
	}

	public Integer getMinResultsPerFinder() {
		Integer result = finderRepository.getMinResultsPerFinder();
		if(result == null) result = 0;
		return result;
	}
	
	public Integer getMaxResultsPerFinder() {
		Integer result = finderRepository.getMaxResultsPerFinder();
		if(result == null) result = 0;
		return result;
	}
	
	public Double getStdevResultsPerFinder() {
		Double result = finderRepository.getStdevResultsPerFinder();
		if(result == null) result = 0d;
		return result;
	}
	
}