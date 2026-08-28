package com.intern.booking_event.service;

import com.intern.booking_event.model.dto.request.AuthenticationRequest;
import com.intern.booking_event.model.dto.request.IntrospectRequest;
import com.intern.booking_event.model.dto.request.LogoutRequest;
import com.intern.booking_event.model.dto.request.RefreshRequest;
import com.intern.booking_event.model.dto.response.AuthenticationResponse;
import com.intern.booking_event.model.dto.response.IntrospectResponse;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);

    IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException;

    void logout(LogoutRequest request) throws ParseException, JOSEException;

    AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException;
}
