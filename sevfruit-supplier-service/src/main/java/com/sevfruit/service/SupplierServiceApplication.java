package com.sevfruit.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.sevfruit.model.Price;
import com.sevfruit.model.PriceProduct;
import com.sevfruit.model.Period;
import com.sevfruit.model.Product;
import com.sevfruit.model.Shipment;
import com.sevfruit.model.ShipmentProduct;
import com.sevfruit.model.Supplier;
import com.sevfruit.repo.PriceRepository;
import com.sevfruit.repo.PeriodRepository;
import com.sevfruit.repo.PriceProductRepository;
import com.sevfruit.repo.ProductRepository;
import com.sevfruit.repo.ShipmentProductRepository;
import com.sevfruit.repo.ShipmentRepository;
import com.sevfruit.repo.SupplierRepository;

@SpringBootApplication
@EnableJpaRepositories("com.sevfruit.repo")
@EntityScan("com.sevfruit.model")
@EnableTransactionManagement
public class SupplierServiceApplication {
	private static final Logger logger = LoggerFactory.getLogger(SupplierServiceApplication.class);
	
	@Autowired
	private SupplierRepository supplierRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private PeriodRepository periodRepository;
	
	@Autowired
	private PriceRepository priceRepository;
	
	@Autowired
	private PriceProductRepository priceProdRepository;
	
	@Autowired
	private ShipmentRepository shipmentRepository;
	
	@Autowired
	private ShipmentProductRepository shipmentProductRepository;
	
	
	public static void main(String[] args) {
		SpringApplication.run(SupplierServiceApplication.class, args);
	}
	
	
	
	@Bean
	ApplicationRunner applicationRunner(Environment environment) {
		return args -> {
			logger.info("spring.datasource.url: {}", environment.getProperty("spring.datasource.url"));
			logger.info("spring.jpa.properties.hibernate.default_schema: {}", environment.getProperty("spring.jpa.properties.hibernate.default_schema"));
			logger.info("app.db.init.enabled: {}", environment.getProperty("app.db.init.enabled"));
		};
	}
	
	@Bean
	@ConditionalOnProperty(name = "app.db.init.enabled", havingValue = "true")
	ApplicationRunner initDB()
	{
		return args -> {
			logger.info("initDB...");
			
			supplierRepository.saveAll(List.of(new Supplier("S1"), new Supplier("S2"), new Supplier("S3")));
			final Supplier s1 = supplierRepository.findByName("S1").orElseThrow(); 
			final Supplier s2 = supplierRepository.findByName("S2").orElseThrow();
			final Supplier s3 = supplierRepository.findByName("S3").orElseThrow();
			
			productRepository.saveAll(List.of(new Product("Яблоки красные"), new Product("Яблоки жёлтые"), new Product("Яблоки зелёные"), 
					new Product("Груши жёлтые"), new Product("Груши зелёные"), new Product("Груши красные")));
			final Product pAR = productRepository.findByName("Яблоки красные").orElseThrow();
			final Product pAY = productRepository.findByName("Яблоки жёлтые").orElseThrow();
			final Product pAG = productRepository.findByName("Яблоки зелёные").orElseThrow();
			final Product pPY = productRepository.findByName("Груши жёлтые").orElseThrow();
			final Product pPG = productRepository.findByName("Груши зелёные").orElseThrow();
			final Product pPR = productRepository.findByName("Груши красные").orElseThrow();
			
			periodRepository.saveAll(List.of(new Period("2020"), new Period("2021"), new Period("2022"), new Period("2023")));
			final Period p2020 = periodRepository.findByName("2020").orElseThrow();
			final Period p2021 = periodRepository.findByName("2021").orElseThrow();
			final Period p2022 = periodRepository.findByName("2022").orElseThrow();
			final Period p2023 = periodRepository.findByName("2023").orElseThrow();
			
			final Price price1_2022 = priceRepository.save(new Price(s1, p2022));
			final Price price2_2022 = priceRepository.save(new Price(s2, p2022));
			final Price price3_2022 = priceRepository.save(new Price(s3, p2022));
			final Price price1_2023 = priceRepository.save(new Price(s1, p2023));
//			final Price price2_2023 = priceRepository.save(new Price(s2, p2023));
//			final Price price3_2023 = priceRepository.save(new Price(s3, p2023));
			
			
			final PriceProduct priceProduct_1_1 = priceProdRepository.save(new PriceProduct(price1_2022, pAR, 100_000f));
			final PriceProduct priceProduct_1_2 = priceProdRepository.save(new PriceProduct(price1_2022, pAY, 125_000f));
			final PriceProduct priceProduct_1_4 = priceProdRepository.save(new PriceProduct(price1_2022, pPY, 150_000f));
			final PriceProduct priceProduct_1_5 = priceProdRepository.save(new PriceProduct(price1_2022, pPG, 175_000f));
			final PriceProduct priceProduct_2_2 = priceProdRepository.save(new PriceProduct(price2_2022, pAY, 120_000f));
			final PriceProduct priceProduct_2_3 = priceProdRepository.save(new PriceProduct(price2_2022, pAG, 150_000f));
			final PriceProduct priceProduct_2_5 = priceProdRepository.save(new PriceProduct(price2_2022, pPG, 170_000f));
			final PriceProduct priceProduct_2_6 = priceProdRepository.save(new PriceProduct(price2_2022, pPR, 180_000f));
			final PriceProduct priceProduct_3_1 = priceProdRepository.save(new PriceProduct(price3_2022, pAR, 110_000f));
			final PriceProduct priceProduct_3_3 = priceProdRepository.save(new PriceProduct(price3_2022, pAG, 140_000f));
			final PriceProduct priceProduct_3_4 = priceProdRepository.save(new PriceProduct(price3_2022, pPY, 160_000f));
			final PriceProduct priceProduct_3_6 = priceProdRepository.save(new PriceProduct(price3_2022, pPR, 175_000f));
			
			final Shipment shipment1 = shipmentRepository.save(new Shipment(price1_2022));
			final Shipment shipment2 = shipmentRepository.save(new Shipment(price1_2022));
			final Shipment shipment3 = shipmentRepository.save(new Shipment(price1_2022));
			final Shipment shipment4 = shipmentRepository.save(new Shipment(price2_2022));
			final Shipment shipment5 = shipmentRepository.save(new Shipment(price2_2022));
			final Shipment shipment6 = shipmentRepository.save(new Shipment(price2_2022));
			final Shipment shipment7 = shipmentRepository.save(new Shipment(price3_2022));
			final Shipment shipment8 = shipmentRepository.save(new Shipment(price3_2022));
			final Shipment shipment9 = shipmentRepository.save(new Shipment(price3_2022));
			
			shipmentProductRepository.saveAll(List.of(
				new ShipmentProduct(shipment1, priceProduct_1_1, 1.0f),
				new ShipmentProduct(shipment1, priceProduct_1_2, 1.5f),
				new ShipmentProduct(shipment1, priceProduct_1_5, 1.3f),
				new ShipmentProduct(shipment2, priceProduct_1_4, 2.0f),
				new ShipmentProduct(shipment2, priceProduct_1_5, 2.0f),
				new ShipmentProduct(shipment4, priceProduct_2_3, 2.5f),
				new ShipmentProduct(shipment4, priceProduct_2_6, 2.2f),
				new ShipmentProduct(shipment6, priceProduct_2_2, 3.0f),
				new ShipmentProduct(shipment6, priceProduct_2_5, 2.8f),
				new ShipmentProduct(shipment7, priceProduct_3_1, 2.0f),
				new ShipmentProduct(shipment8, priceProduct_3_6, 3.0f),
				new ShipmentProduct(shipment9, priceProduct_3_4, 2.0f)
				));

			logger.info("initDB OK");
		};
	}
}
