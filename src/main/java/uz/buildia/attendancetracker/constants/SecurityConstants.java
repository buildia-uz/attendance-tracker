package uz.buildia.attendancetracker.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SecurityConstants {

    public static final String AUTH_API_ENTRYPOINT = "/auth";
    public static final String AUTH_TOKEN_HEADER_PREFIX = "Bearer ";

    public static final int AUTH_TOKEN_HEADER_PREFIX_LENGTH = 7;
    public static final int TOKEN_EXPIRATION_TIME = 1000 * 60 * 60 * 24;
}
