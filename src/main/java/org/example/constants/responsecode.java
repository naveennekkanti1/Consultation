package org.example.constants;

public class responsecode {

    // ✅ Success Codes
    public static final int SUCCESS = 200;
    public static final int CREATED = 201;

    // ✅ Client Error Codes
    public static final int BAD_REQUEST = 400;
    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;
    public static final int CONFLICT = 409;

    // ✅ Server Error Codes
    public static final int INTERNAL_SERVER_ERROR = 500;
    public static final int SERVICE_UNAVAILABLE = 503;

    // ✅ Custom Application Codes (optional)
    public static final int EMAIL_ALREADY_EXISTS = 1001;
    public static final int USERNAME_ALREADY_EXISTS = 1002;
    public static final int INVALID_CREDENTIALS = 1003;
    public static final int TOKEN_EXPIRED = 1004;
    public static final int VALIDATION_FAILED = 1005;

    private responsecode() {
        // prevent instantiation
    }
}
