package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import domain.CreditCard;

import repositories.CreditCardRepository;

@Service
@Transactional
public class CreditCardService {

	@Autowired
	private CreditCardRepository creditCardRepository;

	public CreditCard create() {
		CreditCard res = new CreditCard();

		return res;
	}

	public Collection<CreditCard> findAll() {
		return creditCardRepository.findAll();
	}

	public CreditCard findOne(int id) {
		return creditCardRepository.findOne(id);
	}

	public CreditCard save(CreditCard creditCard) {

		CreditCard saved = creditCardRepository.saveAndFlush(creditCard);
		return saved;
	}

	public void delete(CreditCard creditCard) {
		creditCardRepository.delete(creditCard);
	}
}
