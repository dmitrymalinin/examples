package com.sevfruit.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.sevfruit.model.Period;
import com.sevfruit.repo.PeriodRepository;

/**
 * Операции с периодами
 * @author dm
 *
 */
@RestController
@RequestMapping(path = "/period")
public class PeriodController {
	@Autowired
	private PeriodRepository periodRepository;
	
	/**
	 * Список всех периодов <br/>
	 * {@code  curl http://localhost:8080/period | jq}
	 * @return
	 */
	@GetMapping("")
	public List<Period> getAll()
	{
		return periodRepository.findAll();
	}
	
	/**
	 * Добавить новый период<br/>
	 * {@code curl -w '\n' -D - -X POST -H "Content-type: application/json" -d '{"name": "2024"}' http://localhost:8080/period}
	 * @param period
	 * @return
	 */
	@PostMapping("")
	public Period add(@RequestBody Period period)
	{
		try
		{
			return periodRepository.save(period);
		} catch (Exception e)
		{
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
}
