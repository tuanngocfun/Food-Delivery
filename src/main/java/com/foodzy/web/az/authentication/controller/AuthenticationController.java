package com.foodzy.web.az.authentication.controller;

import com.foodzy.web.az.authentication.domain.AuthenticationToken;
import com.foodzy.web.az.authentication.dto.AuthenticationTokenDTO;
import com.foodzy.web.az.authentication.dto.LoginRequestDTO;
import com.foodzy.web.az.authentication.request.AuthenticationRequestBodyResetPassword;
import com.foodzy.web.az.authentication.service.AuthenticationService;
import com.foodzy.web.az.configs.ApiPathConfigs;
import com.foodzy.web.az.core.exception.ItemNotFoundException;
import com.foodzy.web.az.core.exception.UnauthorizedException;
import com.foodzy.web.az.core.partner.domain.Partner;
import com.foodzy.web.az.core.partner.dto.PartnerDTO;
import com.foodzy.web.az.core.partner.service.PartnerService;
import com.foodzy.web.az.core.user.domain.User;
import com.foodzy.web.az.core.user.dto.UserDTO;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

import javax.annotation.PostConstruct;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping(ApiPathConfigs.AUTHENTICATION)
@DependsOn({"coreController", "partnerService"})
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private AuthenticationService service;

    @Autowired
    private PartnerService partnerService;

    @Autowired
    private ModelMapper modelMapper;

    @PostConstruct
    protected void onPostConstruct() {
        this.initializeTypeMaps();
    }

    private TypeMap<AuthenticationToken, AuthenticationTokenDTO> typeMapAuthenticationTokenDTO;

    private void initializeTypeMaps() {
        Converter<Partner, PartnerDTO> converterPartnerDTO = ctx -> Objects.isNull(ctx.getSource()) ?
                null :
                this.modelMapper.getTypeMap(Partner.class, PartnerDTO.class).map(ctx.getSource());
        Converter<User, UserDTO> converterUserDTO = ctx -> Objects.isNull(ctx.getSource()) ?
                null :
                this.modelMapper.getTypeMap(User.class, UserDTO.class).map(ctx.getSource());
        this.typeMapAuthenticationTokenDTO = this.modelMapper.createTypeMap(AuthenticationToken.class, AuthenticationTokenDTO.class)
                .addMapping(AuthenticationToken::getAuthority, AuthenticationTokenDTO::setAuthority)
                .addMapping(AuthenticationToken::getUserAccessToken, AuthenticationTokenDTO::setUserAccessToken)
                .addMapping(AuthenticationToken::getUserUuid, AuthenticationTokenDTO::setUserUuid)
                .addMappings(mapper -> {
                    mapper.using(converterPartnerDTO).map(AuthenticationToken::getPartner, AuthenticationTokenDTO::setPartner);
                });
    }

    @RequestMapping(value = "token", method = RequestMethod.GET)
    @ResponseBody
    public AuthenticationTokenDTO status() throws IllegalArgumentException, RestClientException, UnauthorizedException {
        AuthenticationToken authenticationToken = this.service.authenticationToken();
        return Optional.ofNullable(authenticationToken)
                .map(this.typeMapAuthenticationTokenDTO::map)
                .orElseThrow(() -> new UnauthorizedException("Authentication status: UNAUTHORIZED."));
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public AuthenticationTokenDTO login(@RequestBody LoginRequestDTO requestBody)
            throws IllegalArgumentException, ItemNotFoundException, RestClientException {
        Partner partner = this.partnerService.queryByArenaId(requestBody.getPartner());
        return Optional
                .ofNullable(this.service.login(partner, requestBody.getUsername(), requestBody.getPassword()))
                .map(this.typeMapAuthenticationTokenDTO::map)
                .orElseThrow(() -> new UnauthorizedException("Authentication status: UNAUTHORIZED."));
    }

    @RequestMapping(value = "logout", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public ResponseEntity logout() throws IllegalArgumentException, RestClientException {
        this.service.logout();
        return ResponseEntity.ok(null);
    }

    @RequestMapping(value = "reset_password", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity resetPassword(@RequestBody AuthenticationRequestBodyResetPassword requestBody)
            throws IllegalArgumentException, RestClientException {
        this.service.resetPassword(requestBody);
        return ResponseEntity.ok(null);
    }
}
