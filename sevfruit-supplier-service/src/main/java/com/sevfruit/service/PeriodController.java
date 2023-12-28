package com.sevfruit.service;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.sevfruit.model.Period;
import com.sevfruit.model.ProductTotal;
import com.sevfruit.model.Supplier;
import com.sevfruit.model.SupplierProductReportLine;
import com.sevfruit.model.SupplierProductsReport;
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
	 * {@code  curl -H "Api-Key: 12345" http://localhost:8080/period | jq}
	 * @return
	 */
	@GetMapping("")
	public List<Period> getAll()
	{
		return periodRepository.findAll();
	}
	
	/**
	 * Заданный период <br/>
	 * {@code  curl -H "Accept-Language: ru" -H "Api-Key: 12345" http://localhost:8080/period/3 | jq}
	 * @param period_id
	 * @param locale
	 * @return
	 */
	@GetMapping("{period_id}")
	public Period get(@PathVariable int period_id, Locale locale)
	{
		final Optional<Period> period = periodRepository.findById(period_id);
		if (period.isPresent())
		{
			return period.get();
		} else
		{
			logger.error("PeriodController.get(): period {} not found", period_id);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, msgSrc.getMessage(ServiceMessages.ERROR_PERIOD_NOT_FOUND, new Integer[]{period_id}, locale));
		}
	}
	
	/**
	 * Добавить новый период поставок<br/>
	 * {@code curl -w '\n' -D - -X POST -H "Api-Key: 12345" -H "Content-type: application/json" -d '{"name": "2024"}' http://localhost:8080/period}
	 * @param period
	 * @return
	 */
	@PostMapping("")
	public ResponseEntity<Period> add(@RequestBody Period period)
	{
		try
		{
			final Period newPeriod = periodRepository.save(period);
			
			final URI location = ServletUriComponentsBuilder.fromCurrentRequestUri()
					.pathSegment("{period_id}").buildAndExpand(newPeriod.getId()).toUri();
			
			return ResponseEntity.created(location).body(newPeriod);
		} catch (Exception e)
		{
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
	
	/**
	 * Отчёт поступление видов продукции по поставщикам с итогами по весу и стоимости.
	 * <br/>
	 * {@code curl -H "Accept-Language: ru" -H "Api-Key: 12345" http://localhost:8080/period/3/report | jq}
	 * @param period_id
	 * @param locale
	 * @return
	 */
	@GetMapping("{period_id}/report")
	public List<SupplierProductsReport> getSupplierProductReport(@PathVariable int period_id, Locale locale)
	{
		final Period period = get(period_id, locale);
		
		final List<SupplierProductReportLine> reportLines = periodRepository.getSupplierProductReport(period);
		
		// Сгруппировать информацию по каждому поставщику
		final List<SupplierProductsReport> res = new LinkedList<>();
		int prev_supplier_id = -1;
		SupplierProductsReport supplierProductsReport = null;
		for (SupplierProductReportLine line : reportLines) {
			Supplier supplier = line.getSupplier();
			if (supplier.getId() != prev_supplier_id)
			{
				prev_supplier_id = supplier.getId();
				supplierProductsReport = new SupplierProductsReport(supplier);
				res.add(supplierProductsReport);
			}
			supplierProductsReport.addProduct(ProductTotal.fromReportLine(line));
		}		
		
		return res;
	}
}
