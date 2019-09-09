package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import repositories.ManagerRepository;
import security.LoginService;
import security.UserAccount;
import domain.Manager;
import forms.ManagerForm;

@Service
@Transactional
public class ManagerService {
	
	@Autowired
	private ManagerRepository managerRepository;
	
	public Manager create(UserAccount ua) {

		Manager result = new Manager();

		result.setUserAccount(ua);
		result.setPackages(new ArrayList<domain.Package>());
		return result;
	}

	public Collection<Manager> findAll() {
		return managerRepository.findAll();
	}

	public Manager findOne(int id) {
		return managerRepository.findOne(id);
	}

	public Manager save(Manager manager) {

		Manager saved = managerRepository.saveAndFlush(manager);
		return saved;
	}

	public void delete(Manager manager) {
		managerRepository.delete(manager);
	}
	
	public Manager findByPrincipal() {
		return managerRepository.getManagerByUserAccountId((LoginService.getPrincipal().getId()));
	}
	
	public ManagerForm rellenaForm(Manager manager , ManagerForm managerForm , List<String> parts) {
		
		if (parts.contains("personal") || parts.isEmpty()) {
			
			// Personal Data
			
			managerForm.setName(manager.getName());
			managerForm.setMiddleName(manager.getMiddleName());
			managerForm.setSurname(manager.getSurname());
			managerForm.setAddress(manager.getAddress());
			managerForm.setEmail(manager.getEmail());
			managerForm.setPhone(manager.getPhone());
			managerForm.setPhoto(manager.getPhoto());
		}
		
		if(parts.contains("account")|| parts.isEmpty()) {
			
			// Account
			
			managerForm.setPassword("default");
			managerForm.setPassword2("default");
			managerForm.setUsername(manager.getUserAccount().getUsername());
		}
		
		return managerForm;
	}
	
	public Double getAvgRequestsPerManager() {
		Double res = managerRepository.getAvgRequestsPerManager();
		if (res == null)
			res = 0d;
		return res;
	}

	public Integer getMinRequestsPerManager() {
		Integer res = managerRepository.getMinRequestsPerManager();
		if (res == null)
			res = 0;
		return res;
	}

	public Integer getMaxRequestsPerManager() {
		Integer res = managerRepository.getMaxRequestsPerManager();
		if (res == null)
			res = 0;
		return res;
	}

	public Double getAvgRequestsPerManagerStatusPending() {
		Double res = managerRepository.getAvgRequestsPerManagerStatusPending();
		if (res == null)
			res = 0d;
		return res;
	}
	
	public Double getAvgRequestsPerManagerStatusApproved() {
		Double res = managerRepository.getAvgRequestsPerManagerStatusApproved();
		if (res == null)
			res = 0d;
		return res;
	}
	
	public Double getAvgRequestsPerManagerStatusRejected() {
		Double res = managerRepository.getAvgRequestsPerManagerStatusRejected();
		if (res == null)
			res = 0d;
		return res;
	}
	
	public Integer getMinRequestsPerManagerStatusPending() {
		Integer res = managerRepository.getMinRequestsPerManagerStatusPending();
		if (res == null)
			res = 0;
		return res;
	}
	
	public Integer getMinRequestsPerManagerStatusApproved() {
		Integer res = managerRepository.getMinRequestsPerManagerStatusApproved();
		if (res == null)
			res = 0;
		return res;
	}
	
	public Integer getMinRequestsPerManagerStatusRejected() {
		Integer res = managerRepository.getMinRequestsPerManagerStatusRejected();
		if (res == null)
			res = 0;
		return res;
	}
	
	public Integer getMaxRequestsPerManagerStatusPending() {
		Integer res = managerRepository.getMaxRequestsPerManagerStatusPending();
		if (res == null)
			res = 0;
		return res;
	}
	
	public Integer getMaxRequestsPerManagerStatusApproved() {
		Integer res = managerRepository.getMaxRequestsPerManagerStatusApproved();
		if (res == null)
			res = 0;
		return res;
	}
	
	public Integer getMaxRequestsPerManagerStatusRejected() {
		Integer res = managerRepository.getMaxRequestsPerManagerStatusRejected();
		if (res == null)
			res = 0;
		return res;
	}
	
	public Collection<Manager> top10ManagersRequestsStatusPending() {

		ArrayList<Manager> res = (ArrayList<Manager>) managerRepository.top10ManagersRequestsStatusPending();

		List<Manager> sub = res;
		if (res.size() > 10) {
			sub = res.subList(0, 9);
		}
		return sub;
	}
	
	public Collection<Manager> top10ManagersRequestsStatusApproved() {

		ArrayList<Manager> res = (ArrayList<Manager>) managerRepository.top10ManagersRequestsStatusApproved();

		List<Manager> sub = res;
		if (res.size() > 10) {
			sub = res.subList(0, 9);
		}
		return sub;
	}
	
	public Collection<Manager> top10ManagersRequestsStatusRejected() {

		ArrayList<Manager> res = (ArrayList<Manager>) managerRepository.top10ManagersRequestsStatusRejected();

		List<Manager> sub = res;
		if (res.size() > 10) {
			sub = res.subList(0, 9);
		}
		return sub;
	}
	
}
