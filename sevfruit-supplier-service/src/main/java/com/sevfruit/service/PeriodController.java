package com.sevfruit.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.sevfruit.model.Period;
import com.sevfruit.model.Supplier;
import com.sevfruit.model.SupplierProductReportLine;
import com.sevfruit.model.ProductTotal;
import com.sevfruit.repo.PeriodRepository;

/**
 * Операции с периодами
 * @author dm
 *
 */
@RestController
@RequestMapping(path = "/period")
public class PeriodController {
	private static final Logger logger = LoggerFactory.getLogger(PeriodController.class);
	
	@Autowired
	private MessageSource msgSrc;
	
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
	
	/**
	 * Отчёт поступление видов продукции по поставщикам с итогами по весу и стоимости.
	 * <br/>
	 * {@code curl -H "Accept-Language: ru" http://localhost:8080/period/3/report | jq}
	 * @param period_id
	 * @param locale
	 * @return
	 */
	@GetMapping("{period_id}/report")
	public Map<Supplier, List<ProductTotal>> getSupplierProductReport(@PathVariable int period_id, Locale locale)
	{
		final Optional<Period> period = periodRepository.findById(period_id);
		if (!period.isPresent())
		{
			logger.error("getSupplierProductReport(): period {} not found", period_id);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, msgSrc.getMessage(ServiceMessages.ERROR_PERIOD_NOT_FOUND, new Integer[]{period_id}, locale));
		}
		final List<SupplierProductReportLine> reportLines = periodRepository.getSupplierProductReport(period.get());
		
		return reportLines.stream().collect(
				Collectors.groupingBy(SupplierProductReportLine::getSupplier, LinkedHashMap::new, Collectors.mapping(
						ProductTotal::fromReportLine, Collectors.toList())
				)
			);
	}
}
