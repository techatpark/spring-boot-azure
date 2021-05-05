package com.techatpark.starter.security.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class AuthenticationResponse {
    /**
     * declares variable authToken.
     */
    private final String authToken;
    /**
     * declares variable refreshToken.
     */
    private final String refreshToken;
    /**
     * declares variable profilePicture.
     */
    private final String profilePicture;

    /**
     * initializes the value for authToken,refresh_token,profile_pic.
     * @param anAuthToken
     * @param aRefreshToken
     * @param aProfilePicture
     */
    @JsonCreator
    public AuthenticationResponse(
            @JsonProperty("authToken") final String anAuthToken,
            @JsonProperty("refresh_token") final String aRefreshToken,
            @JsonProperty("profile_pic") final String aProfilePicture) {
        this.authToken = anAuthToken;
        this.refreshToken = aRefreshToken;
        this.profilePicture = aProfilePicture;
    }

    /**
     * gets the value for auth token.
     * @return auth token
     */
    public String getAuthToken() {
        return authToken;
    }
    /**
     * gets the value for refresh token.
     * @return refresh token
     */
    public String getRefreshToken() {
        return refreshToken;
    }
    /**
     * gets the value for profile picture.
     * @return profile picture
     */
    public String getProfilePicture() {
        return profilePicture;
    }
}
