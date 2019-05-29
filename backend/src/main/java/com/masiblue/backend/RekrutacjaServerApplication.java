package com.masiblue.backend;

import com.masiblue.backend.exception.*;
import com.masiblue.backend.model.*;
import com.masiblue.backend.service.LanguageService;
import com.masiblue.backend.service.PositionService;
import com.masiblue.backend.service.TestService;
import com.masiblue.backend.service.UserAccountService;
import com.masiblue.backend.utils.CsvWriter;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;

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

	@Autowired
	TestService testService;

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
		ApplicationUser testApplicationUser = new ApplicationUser("Justyna", "Michalska", redactorRole);
		UserAccount testAccount = new UserAccount("asia", "asia", testApplicationUser);
		userAccountService.addNewAccount(testAccount);

		Role redactorRole2 = new Role(RoleConstants.REDACTOR_ROLE);
		ApplicationUser test2 = new ApplicationUser("Michał", "Milski", redactorRole2);
		UserAccount testAccount2 = new UserAccount("michal", "michal", test2);
		userAccountService.addNewAccount(testAccount2);


		positionService.add("Java Developer");
		positionService.add("C++ Developer");
		languageService.add("Polski");
		languageService.add("English");

        testCsvReadWrite(testApplicationUser);

    }

    private void testCsvReadWrite(ApplicationUser testApplicationUser) throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException, LanguageAlreadExistsException, LanguageNotFoundException, UserAccountNotFoundException, ApplicationUserNotFoundException, InvalidCsvException, PositionNotFoundException {
        Position testPosition = new Position("testowa pozycja");
        Language lang = new Language("english");
        Question q1 = new Question();
        q1.setContent("Hello hello");
        q1.setPossibleAnswers(new HashSet<>(Arrays.asList("Hi", "Hello", "Hola")));
        q1.setId(99);
        q1.setType(Type.O);

        Question q2 = new Question();
        q2.setContent("Test question content 2");
        q2.setPossibleAnswers(new HashSet<>(Arrays.asList("Answer 1", "Answer 2")));
        q2.setId(100);
        q2.setType(Type.S);

        Test testTest = new Test(99, "testowy test", testPosition, testApplicationUser, lang, LocalDateTime.now(),
                LocalDateTime.now(), Arrays.asList(q1, q2));
        String writeFilePath = "C:\\Users\\Asia\\Desktop\\studia\\sem1\\MASI\\csv\\test3.csv";
        CsvWriter.writeToCsvFile(testTest, writeFilePath);

        String positionName ="Java Developer";
        try {
            positionService.add(new Position(positionName));
        } catch (PositionAlreadyExistsException e) {
            e.printStackTrace();
        }
        Position position = positionService.findByName(positionName);

        String readFilePath = "C:\\Users\\Asia\\Desktop\\studia\\sem1\\MASI\\csv\\test2.csv";
        Test test = testService.readTestFromCsv("Test na stanowisko Java Developer", "asia", position.getId(), readFilePath);
        System.out.println("hehe");
    }

}
