package com.ihb.mytalentbackend.service;

import com.ihb.mytalentbackend.domain.User;

public interface AuthenticationService {
    User signInAndReturnJWT(User signInRequest);
}
