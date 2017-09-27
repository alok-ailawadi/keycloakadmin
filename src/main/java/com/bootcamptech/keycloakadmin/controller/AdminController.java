package com.bootcamptech.keycloakadmin.controller;


import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

@RestController
public class AdminController {

    @RequestMapping(path = "/user", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> createUser(@RequestBody Map<String,Object> newUser)
    {
        Keycloak kc = Keycloak.getInstance(
                "http://localhost:8080/auth",
                "master", // the realm to log in to
                "admin", "admin",  // the user
                "admin-cli");

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue("test123");
        credential.setTemporary(true);
        UserRepresentation user = new UserRepresentation();
        user.setUsername((String) newUser.get("name"));
        user.setFirstName((String) newUser.get("name"));
        user.setLastName((String) newUser.get("lastname"));
        user.setCredentials(Arrays.asList(credential));
        user.setEnabled(true);
        RoleRepresentation roleRepresentation = new RoleRepresentation();
        roleRepresentation.setId("admin");
        user.setRealmRoles(Arrays.asList((String) newUser.get("role")));
        kc.realm((String) newUser.get("companyName")).users().create(user);

        return newUser;
    }

    @RequestMapping(path = "/realm", method = RequestMethod.POST)
    public @ResponseBody String createRealm(@RequestParam String companyName)
    {
        Keycloak kc = Keycloak.getInstance(
                "http://localhost:8080/auth",
                "master", // the realm to log in to
                "admin", "admin",  // the user
                "admin-cli");
        RealmRepresentation realmRepresentation = new RealmRepresentation();
        realmRepresentation.setRealm(companyName);
        realmRepresentation.setDisplayName(companyName);
        realmRepresentation.setNotBefore(0);
        realmRepresentation.setRevokeRefreshToken(false);
        realmRepresentation.setAccessCodeLifespan(300);
        realmRepresentation.setAccessTokenLifespanForImplicitFlow(900);
        realmRepresentation.setSsoSessionIdleTimeout(1800);
        realmRepresentation.setSsoSessionMaxLifespan(3600);
        realmRepresentation.setOfflineSessionIdleTimeout(2592000);
        realmRepresentation.setAccessCodeLifespan(60);
        realmRepresentation.setAccessCodeLifespanUserAction(300);
        realmRepresentation.setAccessCodeLifespanLogin(1800);
        realmRepresentation.setEnabled(true);
        realmRepresentation.setSslRequired("none"); //TODO: configuration property
        realmRepresentation.setRegistrationAllowed(true);
        realmRepresentation.setRegistrationEmailAsUsername(false);
        realmRepresentation.setRememberMe(false);
        realmRepresentation.setVerifyEmail(false);
        realmRepresentation.setResetPasswordAllowed(true);
        realmRepresentation.setEditUsernameAllowed(false);
        realmRepresentation.setBruteForceProtected(false);
        realmRepresentation.setMaxFailureWaitSeconds(900);
        realmRepresentation.setMinimumQuickLoginWaitSeconds(60);
        realmRepresentation.setWaitIncrementSeconds(60);
        //n"almRepresentation.setQuickLoginCheckMilliSeconds(1000;'');
        realmRepresentation.setMaxDeltaTimeSeconds(43200);
        realmRepresentation.setFailureFactor(30);

        realmRepresentation.setDefaultRoles(Arrays.asList("standard", "admin","superadmin","uma_authorization" ));
        realmRepresentation.setPasswordPolicy("hashIterations(20000)");
        realmRepresentation.setRegistrationFlow("registration");
        realmRepresentation.setDirectGrantFlow("direct grant");
        kc.realms().create(realmRepresentation);

        return "realm : "+ companyName + " created successfully";


    }

    @RequestMapping(path = "/token", method = RequestMethod.POST)
    public @ResponseBody
    AccessTokenResponse getToken(@RequestBody  Map<String , Object> credentials)
    {

        Keycloak kc = Keycloak.getInstance(
                "http://localhost:8080/auth",
                (String) credentials.get("company"), // the realm to log in to
                (String) credentials.get("user-name"), (String) credentials.get("password"),  // the user
                "admin-cli");

        return kc.tokenManager().getAccessToken();

    }
}
