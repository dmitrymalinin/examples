package com.sevfruit.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.sevfruit.model.PriceProduct;
import com.sevfruit.model.Shipment;
import com.sevfruit.model.ShipmentProduct;
import com.sevfruit.repo.ShipmentProductRepository;
import com.sevfruit.repo.ShipmentRepository;

/**
 * Операции с поставками
 * @author dm
 *
 */
@RestController
@RequestMapping(path = "/shipment")
public class ShipmentController {
	@Autowired
	private ShipmentRepository shipmentRepository;
	
	@Autowired
	private ShipmentProductRepository shipmentProductRepository;
	
	/**
	 * Список всех поставок <br/>
	 * {@code  curl -H "Api-Key: 12345" http://localhost:8080/shipment | jq}
	 * @return
	 */
	@GetMapping("")
	public List<Shipment> getAll()
	{
		return shipmentRepository.findAll();
	}
	
	/**
	 * Добавить новую поставку <br/>
	 * {@code curl -w '\n' -D - -X POST -H "Api-Key: 12345" -H "Content-type: application/json" -d '{"price":3, "products": [{"priceProduct":{"product":4},"quantity":2.0}, {"priceProduct":{"product":1},"quantity":12.0}]}' http://localhost:8080/shipment }
	 * @param shipment
	 * @return
	 */
	@PostMapping("")
	@Transactional
	public Shipment add(@RequestBody Shipment shipment)
	{
		try
		{
			final Shipment newShipment = shipmentRepository.save(shipment);
			if (shipment.getProducts() != null)
			{
				shipment.getProducts().forEach(sp -> {
					if (sp.getPriceProduct() != null)
					{
						shipmentProductRepository.save(new ShipmentProduct(newShipment, new PriceProduct(newShipment.getPrice().getId(), sp.getPriceProduct().getProduct().getId()), sp.getQuantity()));
					}
				});
			}
			return newShipment;
		} catch (Exception e)
		{
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
}
