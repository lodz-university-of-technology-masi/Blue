package com.masiblue.backend;

import com.masiblue.backend.model.ApplicationUser;
import com.masiblue.backend.model.Role;
import com.masiblue.backend.model.UserAccount;
import com.masiblue.backend.service.ApplicationUserService;
import com.masiblue.backend.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication()
@EnableJpaAuditing
public class RekrutacjaServerApplication implements CommandLineRunner {

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	UserAccountService userAccountService;


	public static void main(String[] args) {
		SpringApplication.run(RekrutacjaServerApplication.class, args);
	}

	@Override
	public void run(String... arg0) throws Exception {
		Role adminRole = new Role("ADMIN");
		ApplicationUser admin = new ApplicationUser("Maciej", "Wyrzuc", adminRole);
		UserAccount adminAccount = new UserAccount("admin", "admin", admin);
		userAccountService.save(adminAccount);

		Role testRole = new Role("MODERATOR");
		ApplicationUser test = new ApplicationUser("Joanna", "Górczak", testRole);
		UserAccount testAccount = new UserAccount("asia", "asia", test);
		userAccountService.save(testAccount);
	}

}
