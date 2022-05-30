package com.example.relationaldataaccess;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingService {

	@Autowired
	JdbcTemplate jdbcTemplate;
	private final static Logger log = LoggerFactory.getLogger(BookingService.class);
	
	@Transactional
	public void book(String... persons) {
		for (String person: persons) {
			log.info("Prenotazione di "+person);
			jdbcTemplate.update("insert into BOOKINGS(FIRST_NAME) values (?)", person);
		}
	}
	
	public List<String> findAllBookings() {
		return jdbcTemplate.query("select FIRST_NAME from BOOKINGS", 
				(rs, rowNum) -> rs.getString("FIRST_NAME"));
	}
	
}
