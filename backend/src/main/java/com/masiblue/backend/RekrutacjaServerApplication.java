package com.masiblue.backend;

import com.masiblue.backend.exception.UserAccountAlreadyExistsException;
import com.masiblue.backend.model.*;
import com.masiblue.backend.service.*;
import com.masiblue.backend.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootApplication()
@EnableJpaAuditing
public class RekrutacjaServerApplication implements CommandLineRunner {

    @Autowired
    private RoleService roleService;

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

	@Autowired
	QuestionService questionService;

	public static void main(String[] args) {
		SpringApplication.run(RekrutacjaServerApplication.class, args);
	}

	@Override
	public void run(String... arg0) throws Exception {
        addProdTestData();
        //addDevTestData();
	}

    private void addProdTestData() throws UserAccountAlreadyExistsException {
        List<MasiGroup> masiGroups = Arrays.asList(
                new MasiGroup("Blue", 3),
                new MasiGroup("Yellow", 4),
                new MasiGroup("Green", 3),
                new MasiGroup("Red", 4),
                new MasiGroup("Orange", 4),
                new MasiGroup("Black", 5),
                new MasiGroup("White", 3),
                new MasiGroup("Magenta", 5),
                new MasiGroup("Violet", 4),
                new MasiGroup("Tester", 3));

        Role moderatorRole = new Role(RoleConstants.MODERATOR_ROLE);
        Role redactorRole = new Role(RoleConstants.REDACTOR_ROLE);
        Role userRole = new Role(RoleConstants.USER_ROLE);
        roleService.addRole(moderatorRole);
        roleService.addRole(redactorRole);
        roleService.addRole(userRole);

        String password = "pass";
        for (MasiGroup masiGroup : masiGroups) {
            int userIterator = 1;
            while (userIterator <= masiGroup.numberOfUsers) {
                String adminFirstName = String.format("%s Admin %d", masiGroup.groupName, userIterator);
                String adminUsername = String.format("a%s%d", masiGroup.groupName.toLowerCase(), userIterator);
                ApplicationUser admin = new ApplicationUser(adminFirstName, "", moderatorRole);
                UserAccount adminAccount = new UserAccount(adminUsername, password, admin);
                userAccountService.addNewAccount(adminAccount);

                String redactorFirstName = String.format("%s Redactor %d", masiGroup.groupName, userIterator);
                String redactorUsername = String.format("r%s%d", masiGroup.groupName.toLowerCase(), userIterator);
                ApplicationUser redactor = new ApplicationUser(redactorFirstName, "", redactorRole);
                UserAccount redactorAccount = new UserAccount(redactorUsername, password, redactor);
                userAccountService.addNewAccount(redactorAccount);
                userIterator++;
            }
        }
    }

    private class MasiGroup {
        private String groupName;
        private int numberOfUsers;

        MasiGroup(String groupName, int numberOfUsers) {
            this.groupName = groupName;
            this.numberOfUsers = numberOfUsers;
        }
    }


/*
	private void addDevTestData() throws EmptyQuestionContentException, ApplicationUserNotFoundException, UserAccountNotFoundException, QuestionTypeNotFoundException, TestNotFoundException, NotOwnerException, AnswerListEmptyException, UserAccountAlreadyExistsException, LanguageAlreadExistsException, PositionNotFoundException, RedactorNotFoundException, LanguageNotFoundException, PositionAlreadyExistsException {

        Role moderatorRole = new Role(RoleConstants.MODERATOR_ROLE);
        Role redactorRole = new Role(RoleConstants.REDACTOR_ROLE);
        Role userRole = new Role(RoleConstants.USER_ROLE);
        roleService.addRole(moderatorRole);
        roleService.addRole(redactorRole);
        roleService.addRole(userRole);

        //Role moderatorRole = roleService.findByName(RoleConstants.MODERATOR_ROLE);
        ApplicationUser admin = new ApplicationUser("Maciej", "Wyrzuc", moderatorRole);
        UserAccount adminAccount = new UserAccount("admin", "admin", admin);
        userAccountService.addNewAccount(adminAccount);

        //Role redactorRole = roleService.findByName(RoleConstants.REDACTOR_ROLE);
        ApplicationUser testApplicationUser = new ApplicationUser("Justyna", "Michalska", redactorRole);
        UserAccount testAccount = new UserAccount("asia", "asia", testApplicationUser);
        userAccountService.addNewAccount(testAccount);

        positionService.add("Java Developer");
        positionService.add("C++ Developer");
        languageService.add("Polski");
        languageService.add("English");

        TestCreateDTO testCreateDTO = new TestCreateDTO("super tescik", 1, 2);

        testService.addNewTest(testCreateDTO, "asia");
        Test createdTest = testService.findById(1);

        QuestionCreateDTO qDTO = new QuestionCreateDTO(Type.O, 1, "How old are you?", null);
        questionService.addNewQuestion(qDTO,"asia");
        Set<String> answers2 = new HashSet<>();
        answers2.add("72");
        answers2.add("75");
        answers2.add("68");
        QuestionCreateDTO qDTO2 = new QuestionCreateDTO(Type.W, 1, "How old is George Bush?", answers2);
        questionService.addNewQuestion(qDTO2,"asia");
        Set<String> answers3 = new HashSet<>();
        answers3.add("Andrzej");
        answers3.add("Roman");
        answers3.add("Józef");
        QuestionCreateDTO qDTO3 = new QuestionCreateDTO(Type.W, 1, "What is the name of current polish president?", answers3);
        questionService.addNewQuestion(qDTO3,"asia");
    }
*/

}
