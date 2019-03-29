package bmatser.exception;

import java.util.Optional;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public abstract class ApplicationException extends bmatser.exception.AbstractException {
    public static final int DEFAULT_APPLICATION_EXCEPTION_STATUS_CODE = 500;
    public static final int DEFAULT_APPLICATION_EXCEPTION_NUMERIC_ERROR_CODE = -1;
    public static final String DEFAULT_APPLICATION_EXCEPTION_ERROR_CODE = "errors.com.patsnap.unknown_error";
    public static final String DEFAULT_APPLICATION_EXCEPTION_ERROR_MESSAGE = "An error occurred and we were unable to resolve it, please contact support on customer service.";
    private final int statusCode;
    private final int numericErrorCode;
    private final String errorCode;
    private String originatingService;
    private DateTime timestamp;

    protected ApplicationException() {
        this(500, -1, "errors.com.patsnap.unknown_error", "An error occurred and we were unable to resolve it, please contact support on customer service.");
    }

    public ApplicationException(String pattern, Object... args) {
        this(500, -1, "errors.com.patsnap.unknown_error", pattern, args);
    }

    protected ApplicationException(int status, int numericErrCode, String errCode, String pattern, Object... args) {
        super(pattern, args);
        this.statusCode = status;
        this.errorCode = (String)Optional.ofNullable(errCode).orElse("errors.com.patsnap.unknown_error");
        this.numericErrorCode = numericErrCode;
        this.originatingService = "interface";
        this.timestamp = DateTime.now(DateTimeZone.UTC);
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public int getNumericErrorCode() {
        return this.numericErrorCode;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public String getOriginatingService() {
        return this.originatingService;
    }

    public void setOriginatingService(String originatingService) {
        this.originatingService = originatingService;
    }

    public DateTime getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(DateTime timestamp) {
        this.timestamp = timestamp;
    }
}
