package com.cv.maker.config.dbmigrations;

import com.cv.maker.config.Constants;
import com.cv.maker.domain.Authority;
import com.cv.maker.domain.Collaborator;
import com.cv.maker.domain.HumanResource;
import com.cv.maker.domain.User;
import com.cv.maker.security.AuthoritiesConstants;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import java.time.Instant;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Creates the initial database setup.
 */
@ChangeUnit(id = "users-initialization", order = "001")
public class InitialSetupMigration {

    private final MongoTemplate template;

    private final PasswordEncoder passwordEncoder;

    public InitialSetupMigration(MongoTemplate template, PasswordEncoder passwordEncoder) {
        this.template = template;
        this.passwordEncoder = passwordEncoder;
    }

    @Execution
    public void changeSet() {
        Authority userAuthority = createUserAuthority();
        userAuthority = template.save(userAuthority);
        Authority adminAuthority = createAdminAuthority();
        adminAuthority = template.save(adminAuthority);
        addUsers(userAuthority, adminAuthority);
    }

    @RollbackExecution
    public void rollback() {}

    private Authority createAuthority(String authority) {
        Authority adminAuthority = new Authority();
        adminAuthority.setName(authority);
        return adminAuthority;
    }

    private Authority createAdminAuthority() {
        Authority adminAuthority = createAuthority(AuthoritiesConstants.ADMIN);
        return adminAuthority;
    }

    private Authority createUserAuthority() {
        Authority userAuthority = createAuthority(AuthoritiesConstants.USER);
        return userAuthority;
    }

    private void addUsers(Authority userAuthority, Authority adminAuthority) {
        User user = createUser(userAuthority);
        template.save(user);
        User admin = createAdmin(adminAuthority, userAuthority);
        template.save(admin);
        User collaboratorUser = createCollaborator(adminAuthority, userAuthority);
        collaboratorUser  =template.save(collaboratorUser);
        Collaborator collaborator = new Collaborator();
        collaborator.setUser(collaboratorUser);
        template.save(collaborator);
        User hrUser = createHr(adminAuthority, userAuthority);
        hrUser = template.save(hrUser);
        HumanResource humanResource = new HumanResource();
        humanResource.setUser(hrUser);
        template.save(humanResource);
    }

    private User createUser(Authority userAuthority) {
        User userUser = new User();
        userUser.setId("user-2");
        userUser.setLogin("user");
        userUser.setPassword("$2a$10$VEjxo0jq2YG9Rbk2HmX9S.k1uZBGYUHdUcid3g/vfiEl7lwWgOH/K");
        userUser.setFirstName("");
        userUser.setLastName("User");
        userUser.setEmail("user@localhost");
        userUser.setActivated(true);
        userUser.setLangKey("en");
        userUser.setCreatedBy(Constants.SYSTEM);
        userUser.setCreatedDate(Instant.now());
        userUser.getAuthorities().add(userAuthority);
        return userUser;
    }

    private User createAdmin(Authority adminAuthority, Authority userAuthority) {
        User adminUser = new User();
        adminUser.setId("user-1");
        adminUser.setLogin("admin@cvmaker.com");
        adminUser.setPassword("$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC");
        adminUser.setFirstName("admin");
        adminUser.setLastName("Administrator");
        adminUser.setEmail("admin@cvmaker.com");
        adminUser.setActivated(true);
        adminUser.setLangKey("en");
        adminUser.setCreatedBy(Constants.SYSTEM);
        adminUser.setCreatedDate(Instant.now());
        adminUser.getAuthorities().add(adminAuthority);
        adminUser.getAuthorities().add(userAuthority);
        return adminUser;
    }


    private User createCollaborator(Authority adminAuthority, Authority userAuthority) {
        User collaboratorUser = new User();
        collaboratorUser.setId("collaborator-1");
        collaboratorUser.setLogin("collaborator@gmail.com");
        collaboratorUser.setPassword(passwordEncoder.encode("collaborator"));
        collaboratorUser.setFirstName("collaborator");
        collaboratorUser.setLastName("collaborator");
        collaboratorUser.setEmail("collaborator@gmail.com");
        collaboratorUser.setActivated(true);
        collaboratorUser.setLangKey("en");
        collaboratorUser.setCreatedBy(Constants.SYSTEM);
        collaboratorUser.setCreatedDate(Instant.now());
        collaboratorUser.getAuthorities().add(userAuthority);
        return collaboratorUser;
    }
    private User createHr(Authority adminAuthority, Authority userAuthority) {
        User humanResourceUser = new User();
        humanResourceUser.setId("human-resource-1");
        humanResourceUser.setLogin("humanresource@gmail.com");
        humanResourceUser.setPassword(passwordEncoder.encode("humanresources"));
        humanResourceUser.setFirstName("human-resource");
        humanResourceUser.setLastName("human-resource");
        humanResourceUser.setEmail("humanresource@gmail.com");
        humanResourceUser.setActivated(true);
        humanResourceUser.setLangKey("en");
        humanResourceUser.setCreatedBy(Constants.SYSTEM);
        humanResourceUser.setCreatedDate(Instant.now());
        humanResourceUser.getAuthorities().add(userAuthority);
        return humanResourceUser;
    }
}
