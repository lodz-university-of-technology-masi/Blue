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
		UserAccount adminAccount = new UserAccount();
		adminAccount.setUsername("admin");
		adminAccount.setPassword("admin");
		ApplicationUser admin = new ApplicationUser();
		admin.setFirstName("Maciej");
		admin.setLastName("Wyrzuc");
		Role adminRole = new Role();
		adminRole.setName("ADMIN");
		admin.setRole(adminRole);
		adminAccount.setApplicationUser(admin);
		userAccountService.save(adminAccount);

		UserAccount testAccount = new UserAccount();
		testAccount.setUsername("asia");
		testAccount.setPassword("asia");
		ApplicationUser test = new ApplicationUser();
		test.setFirstName("Joanna");
		test.setLastName("Górczak");
		Role testRole = new Role();
		testRole.setName("MODERATOR");
		test.setRole(testRole);
		testAccount.setApplicationUser(test);
		userAccountService.save(testAccount);
	}

}
