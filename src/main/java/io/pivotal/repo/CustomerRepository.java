package io.pivotal.repo;

import io.pivotal.domain.Customer;

import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, String> {
	
}
