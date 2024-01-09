package com.sevfruit.repo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaContext;

import com.sevfruit.model.PriceProduct;
import com.sevfruit.model.Shipment;
import com.sevfruit.model.ShipmentProduct;
import com.sevfruit.model.ShipmentProductId;

import jakarta.persistence.EntityManager;

public class ShipmentSaveImpl implements ShipmentSave<Shipment> {
	private static final Logger logger = LoggerFactory.getLogger(ShipmentSaveImpl.class);
	
	private final EntityManager em;
	private final EntityManager shipmentProductEm;
	private final EntityManager priceProductEm;
	
	public ShipmentSaveImpl(JpaContext context) {
		super();
		logger.trace("ShipmentSaveImpl.ctor()");
		this.em = context.getEntityManagerByManagedType(Shipment.class);
		this.shipmentProductEm = context.getEntityManagerByManagedType(ShipmentProduct.class);
		this.priceProductEm = context.getEntityManagerByManagedType(PriceProduct.class);
	}

	@Override
	public <S extends Shipment> S save(S shipment) {
		logger.trace("ShipmentSaveImpl.save({})", shipment);
		em.persist(shipment);
		logger.trace("ShipmentSaveImpl.save(): persisted shipment: {}", shipment);
		if (shipment.getProducts() != null)
		{			
			for (ShipmentProduct sp: shipment.getProducts())
			{
				// Установить Shipment для ShipmentProduct
				sp.setShipment(shipment);
				
				final PriceProduct pp = sp.getPriceProduct();
				logger.trace("ShipmentSaveImpl.save(): pp: {}", pp);
				if (pp != null)
				{
					// Установить Price для PriceProduct
					pp.setPrice(shipment.getPrice());
					
					// Установить ID для PriceProduct
					pp.getId().setPrice_id(shipment.getPrice_id());
					pp.getId().setProduct_id(pp.getProduct().getId());
					
					final PriceProduct managedPP = priceProductEm.merge(pp);
					priceProductEm.refresh(managedPP);
					sp.setPriceProduct(managedPP);
					logger.trace("ShipmentSaveImpl.save(): managedPP: {}", managedPP);
					
					// Установить ID для ShipmentProduct
					sp.setId(new ShipmentProductId(shipment.getId(), shipment.getPrice_id(), managedPP.getProduct().getId()));
					
					shipmentProductEm.persist(sp);
					logger.trace("ShipmentSaveImpl.save(): sp: {}", sp);					
				}
			}
		}

		logger.trace("ShipmentSaveImpl.save() OK {}", shipment);
		return shipment;
	}
	
}
