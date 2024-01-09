package com.sevfruit.repo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaContext;

import com.sevfruit.model.Price;
import com.sevfruit.model.Product;

import jakarta.persistence.EntityManager;

public class PriceSaveImpl implements PriceSave<Price> {
	private static final Logger logger = LoggerFactory.getLogger(PriceSaveImpl.class);
	
	private final EntityManager em;
	private final EntityManager productEm;
	
	public PriceSaveImpl(JpaContext context) {
		super();
		logger.trace("PriceSaveImpl.ctor()");
		this.em = context.getEntityManagerByManagedType(Price.class);
		this.productEm = context.getEntityManagerByManagedType(Product.class);
	}

	@Override
	public <S extends Price> S save(S price) {
		logger.trace("PriceSaveImpl.save({})", price);
		if (price.getProducts() != null)
		{			
			price.getProducts().forEach(pp -> {
				// Установить Price для PriceProduct
				pp.setPrice(price);
				// Merge Product into context, refresh and set managed entity to PriceProduct
				final Product managedProduct = productEm.merge(pp.getProduct());
				productEm.refresh(managedProduct);
				pp.setProduct(managedProduct);
			});
		}
		logger.trace("PriceSaveImpl.save(): updated price: {}", price);
				
		em.persist(price);
		
		em.refresh(price);
		
		logger.trace("PriceSaveImpl.save() OK {}", price);
		return price;
	}
	
}
