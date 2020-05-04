package com.richi.demo.service;

import java.util.*; 
import java.lang.*; 
import java.io.*; 
  
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.richi.demo.exception.RecordNotFoundException;
import com.richi.demo.model.EmployeeEntity;
import com.richi.demo.repository.EmployeeRepository;

@Service
public class EmployeeService {
	
	@Autowired
	EmployeeRepository repository;
	
	
	List<EmployeeEntity> result;
	
	 int check=0;
	
	public List<EmployeeEntity> getAllEmployees()
	{
		
		if(check==0)
		result = (List<EmployeeEntity>) repository.findAll();
		
		else check=0;
		
		if(result.size() > 0) {
			return result;
		} else {
			return new ArrayList<EmployeeEntity>();
		}
	}
	
	public EmployeeEntity getEmployeeById(Long id) throws RecordNotFoundException 
	{
		Optional<EmployeeEntity> employee = repository.findById(id);
		
		if(employee.isPresent()) {
			return employee.get();
		} else {
			throw new RecordNotFoundException("No user record exist for given id");
		}
	}
	
	public EmployeeEntity createOrUpdateEmployee(EmployeeEntity entity) 
	{
		if(entity.getId()  == null) 
		{
			entity = repository.save(entity);
			
			return entity;
		} 
		else 
		{
			Optional<EmployeeEntity> employee = repository.findById(entity.getId());
			
			if(employee.isPresent()) 
			{
				EmployeeEntity newEntity = employee.get();
				newEntity.setFirstName(entity.getFirstName());
				newEntity.setLastName(entity.getLastName());
				newEntity.setCburnt(entity.getCburnt());
				newEntity.setWorkout(entity.getWorkout());

				newEntity = repository.save(newEntity);
				
				return newEntity;
			} else {
				entity = repository.save(entity);
				
				return entity;
			}
		}
	} 
	
	public void deleteEmployeeById(Long id) throws RecordNotFoundException 
	{
		Optional<EmployeeEntity> employee = repository.findById(id);
		
		if(employee.isPresent()) 
		{
			repository.deleteById(id);
		} else {
			throw new RecordNotFoundException("No user record exist for given id");
		}
	} 
	
	public void sortEmployeeByWorkout() 
	{
		result = (List<EmployeeEntity>) repository.findAll();
		check=1;
		Collections.sort(result, new SortbyWorkout());	
	} 
	
	
}

class SortbyWorkout implements Comparator<EmployeeEntity> {


	
	@Override
	public int compare(EmployeeEntity a, EmployeeEntity b) {
		return -1*(Integer.parseInt(a.getWorkout()) - Integer.parseInt(b.getWorkout()));
	}

	
}
