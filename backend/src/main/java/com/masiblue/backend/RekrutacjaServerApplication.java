package com.masiblue.backend;

import com.masiblue.backend.model.*;
import com.masiblue.backend.service.ApplicationUserService;
import com.masiblue.backend.service.LanguageService;
import com.masiblue.backend.service.PositionService;
import com.masiblue.backend.service.UserAccountService;
import org.apache.commons.codec.language.bm.Languages;
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

	@Autowired
	PositionService positionService;

	@Autowired
	LanguageService languageService;

	public static void main(String[] args) {
		SpringApplication.run(RekrutacjaServerApplication.class, args);
	}

	@Override
	public void run(String... arg0) throws Exception {
		Role moderatorRole = new Role(RoleConstants.MODERATOR_ROLE);
		ApplicationUser admin = new ApplicationUser("Maciej", "Wyrzuc", moderatorRole);
		UserAccount adminAccount = new UserAccount("admin", "admin", admin);
		userAccountService.addNewAccount(adminAccount);

		Role redactorRole = new Role(RoleConstants.REDACTOR_ROLE);
		ApplicationUser test = new ApplicationUser("Justyna", "Michalska", redactorRole);
		UserAccount testAccount = new UserAccount("asia", "asia", test);
		userAccountService.addNewAccount(testAccount);

		positionService.add("Java Developer");
		positionService.add("C++ Developer");
		languageService.add("Polski");
		languageService.add("English");
	}

}
