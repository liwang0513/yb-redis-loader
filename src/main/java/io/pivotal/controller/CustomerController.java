package io.pivotal.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.codearte.jfairy.Fairy;
import io.codearte.jfairy.producer.person.Person;
import io.pivotal.domain.Customer;
import io.pivotal.repo.CustomerRepository;

@RestController
public class CustomerController {
	
	@Autowired
	CustomerRepository customerRepository;
	
	Fairy fairy = Fairy.create();

	
	@RequestMapping(method = RequestMethod.GET, path = "/showdb")
	@ResponseBody
	public String showDB() throws Exception {
		StringBuilder result = new StringBuilder();
		Pageable topTen = new PageRequest(0, 10);
		
		customerRepository.findAll().forEach(item->result.append(item+"<br/>"));
		
		return "Top 10 customers are shown here: <br/>" + result.toString();
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/loaddb")
	@ResponseBody
	public String loadDB(@RequestParam(value = "amount", required = true) String amount) throws Exception {
		
		List<Customer> customers = new ArrayList<>();
		
		Integer num = Integer.parseInt(amount);
		
		for (int i=0; i<num; i++) {
			Person person = fairy.person();
			Customer customer = new Customer(person.nationalIdentificationNumber(), person.fullName(), person.email(), person.getAddress().toString(), person.dateOfBirth().toString());
			customers.add(customer);
		}
		
		customerRepository.saveAll(customers);
		
		return "New customers successfully saved into Database";
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/cleardb")
	@ResponseBody
	public String clearDB() throws Exception {
		
		customerRepository.deleteAll();
		
		return "Database cleared";
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/countdb")
	@ResponseBody
	public Long countDB() throws Exception {
		return customerRepository.count();
	}
	
}
