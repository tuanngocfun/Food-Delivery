package com.foodzy.web.az.authentication.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodzy.web.az.authentication.domain.AuthenticationToken;
import com.foodzy.web.az.authentication.request.AuthenticationRequestBodyResetPassword;
import com.foodzy.web.az.core.partner.domain.Partner;
import com.foodzy.web.az.core.partner.service.PartnerService;
import com.foodzy.web.az.core.service.SecurityContextService;
import com.foodzy.web.az.core.user.domain.User;
import com.foodzy.web.az.core.user.service.UserService;
import com.foodzy.web.az.foodzy.arena.service.ArenaService;
import com.foodzy.web.az.foodzy.centralized_system.response.FzUserAccessTokenResponse;
import com.foodzy.web.az.foodzy.centralized_system.response.FzUserInfoResponse;
import com.foodzy.web.az.foodzy.centralized_system.service.FzCentralizedSystemService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestClientException;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.Optional;

@Service
public class AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    private static final String SESSION_AUTH_TOKEN = "authentication_token";

    @Value("${application.ad-foodzy.access-key}")
    private String azAccessKey;

    @Value("${application.ad-foodzy.secret-key}")
    private String azSecretKey;

    @Value("${application.foodzy.arena.fz-sso-access-key}")
    private String arenaAccessKey;

    @Value("${application.foodzy.arena.fz-sso-secret-key}")
    private String arenaSecretKey;

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private PartnerService partnerService;

    @Autowired
    private FzCentralizedSystemService FzCentralizedSystemService;

    @Autowired
    private SecurityContextService securityContextService;

    @Autowired
    private ArenaService arenaService;

    @Autowired
    private UserService userService;

    /**
     * Retrieve the {@link AuthenticationToken} of current session. Return {@literal null} if there is none.
     *
     * @return The {@link AuthenticationToken} of current session; return {@literal null} if there is none.
     */
    public AuthenticationToken authenticationToken() {
        Object tmpObject = this.httpSession.getAttribute(SESSION_AUTH_TOKEN);
        if (Objects.isNull(tmpObject)) {
            logger.error("authenticationToken() returns null.");
            return null;
        }
        if (tmpObject instanceof AuthenticationToken) {
            logger.error("authenticationToken() returns AuthenticationToken object.");
            AuthenticationToken authenticationToken = (AuthenticationToken) tmpObject;
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                logger.info("Getting authentication token from session: {}",
                        objectMapper.writeValueAsString(authenticationToken));
            } catch (IOException ioe) {
                logger.error("IOException occurred getting authentication token from session. Reason: {}",
                        ioe.getLocalizedMessage());
            }
            return authenticationToken;
        }
        return null;
    }

    /**
     * Process an authentication / login request, which includes: username, password and partner code.
     *
     * @param partner
     * @param username
     * @param password
     * @return
     * @throws IllegalArgumentException
     * @throws RestClientException
     */
    public AuthenticationToken login(Partner partner, String username, String password)
            throws IllegalArgumentException, RestClientException {
        Assert.notNull(partner, "Failed to authenticate (reason: invalid Partner).");
        Assert.isTrue(StringUtils.isNotBlank(username), "Failed to authenticate (reason: invalid username).");
        Assert.isTrue(StringUtils.isNotBlank(password), "Failed to authenticate (reason: invalid password).");
        // log user authentication
        logger.info(String.format("Authenticating (partnerCode = %s, username = %s)", partner.toString(), username));
        // handle security context
        // TODO
        // Check if login should be done in front-end or here?
        // this.FzCentralizedSystemService.login(this.azAccessKey, this.azSecretKey, username, password);
        String userAccessTokenArena = this.FzCentralizedSystemService.queryUserAccessToken(
                        this.arenaAccessKey,
                        this.arenaSecretKey,
                        username,
                        password)
                .getAccessToken();
        FzUserAccessTokenResponse userAccessTokenResponse =
                this.FzCentralizedSystemService.queryUserAccessToken(
                        this.azAccessKey,
                        this.azSecretKey,
                        username,
                        password);
        String userAccessToken = userAccessTokenResponse.getAccessToken();
        FzUserInfoResponse FzUserInfo = this.FzCentralizedSystemService.queryUserInformation(userAccessToken);
        User user = this.getUser(FzUserInfo);
        user.setLastLoggedIn(LocalDateTime.now(ZoneOffset.UTC));
        user = this.userService.save(user);
        this.securityContextService.saveContext(user, userAccessToken);
        // build AuthenticationToken and save in to session
        AuthenticationToken authenticationToken = AuthenticationToken.builder()
//                .account(null)
                .authority(null)
                .partner(partner)
                .userUuid(user.getUuid())
                .userAccessToken(userAccessToken)
                .userAccessTokenArena(userAccessTokenArena)
                .build();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            logger.info("Writing authentication token into session: {}",
                    objectMapper.writeValueAsString(authenticationToken));
        } catch (IOException ioe) {
            logger.error("IOException occurred while writing authentication token into session. Reason: {}",
                    ioe.getLocalizedMessage());
        }
        this.httpSession.setAttribute(SESSION_AUTH_TOKEN, authenticationToken);
        return this.authenticationToken();
    }

    public void logout() {
        String userAccessToken = Optional.ofNullable(this.authenticationToken())
                .map(AuthenticationToken::getUserAccessToken)
                .orElse(null);
        if (StringUtils.isNotBlank(userAccessToken)) {
            this.securityContextService.clearContext();
            this.httpSession.removeAttribute(SESSION_AUTH_TOKEN);
            this.FzCentralizedSystemService.logout(userAccessToken);
        }
    }

    public void resetPassword(AuthenticationRequestBodyResetPassword request) throws IllegalArgumentException {
        if (Objects.isNull(request))
            throw new IllegalArgumentException("Failed to reset password (reason: invalid request).");
        String email = request.getEmail();
        if (StringUtils.isBlank(email))
            throw new IllegalArgumentException("Failed to reset password (reason: invalid email).");
        this.FzCentralizedSystemService.resetPassword(email);
    }

    /**
     * Utility method to retrieve a {@link User}, based on information of {@link FzUserInfoResponse}.
     *
     * @param FzUserInfo
     * @return
     */
    private User getUser(FzUserInfoResponse FzUserInfo) {
        Assert.notNull(FzUserInfo, "Failed to get User (reason: invalid foodzy User information).");
        String email = FzUserInfo.getEmail();
        User upsertUser = Optional.ofNullable(this.userService.queryByEmail(email))
                .orElse(User.builder().email(email).build());
        upsertUser.setProductAccessKey(FzUserInfo.getClientKey());
        upsertUser.setSsoAdmin(FzUserInfo.isAdmin());
        upsertUser.setSsoUserId(FzUserInfo.getUserId());
        if (StringUtils.isBlank(upsertUser.getGivenName()) && StringUtils.isBlank(upsertUser.getSurname())) {
            logger.info("Upserting name from FOODZY user information: {} {}.",
                    FzUserInfo.getGivenName(),
                    FzUserInfo.getSurname());
            upsertUser.setGivenName(FzUserInfo.getGivenName());
            upsertUser.setSurname(FzUserInfo.getSurname());
        }
        return this.userService.save(upsertUser);
    }
}
