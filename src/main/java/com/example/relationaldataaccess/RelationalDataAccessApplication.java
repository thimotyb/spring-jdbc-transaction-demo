package com.example.relationaldataaccess;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class RelationalDataAccessApplication implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(RelationalDataAccessApplication.class);	
	
	public static void main(String args[]) {
	    SpringApplication.run(RelationalDataAccessApplication.class, args);
	}

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	BookingService service;
	
	@Override
	public void run(String... args) throws Exception {
		
		log.info("Creazione tabelle in corso...");
		
		jdbcTemplate.execute("DROP TABLE customers IF EXISTS");
	    jdbcTemplate.execute("CREATE TABLE customers(" +
	        "id SERIAL, first_name VARCHAR(255), last_name VARCHAR(255))");
	    
	    List<Object[]> customers = new ArrayList<>();
	    
		customers.add(new Object[] {"John", "Kennedy"});
		customers.add(new Object[] {"Mark", "Salzburg"});
		customers.add(new Object[] {"Mike", "Theano"});
		
		jdbcTemplate.batchUpdate("INSERT INTO customers(first_name, last_name) VALUES (?,?)", customers);
		
		log.info("Verifica rilettura oggetti inseriti");
		
		List<Customer> results = jdbcTemplate.query("SELECT * FROM customers", 
				(rs, rowNum) -> 
		new Customer(rs.getLong("id"), rs.getString("first_name"), rs.getString("last_name")));
		
		results.forEach(c -> log.info(c.toString()));
		
		//------------------------------//
		
		service.book("Pino", "Gino", "Rino");
		service.findAllBookings().forEach(System.out::println);
		
		try {
			service.book("Mino", "Addolorato", "Tino");
		} catch (Exception e) {
			log.error("Errore prenotazione "+e.getMessage());
		}
		service.findAllBookings().forEach(System.out::println);
		
	}
	
}
