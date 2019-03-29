package bmatser.exception;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.helpers.MessageFormatter;

public abstract class AbstractException extends RuntimeException {
    private final String message;
    private final String messageTemplate;
    private final List<String> messageVars = new ArrayList(0);

    protected AbstractException() {
        this.message = "";
        this.messageTemplate = "";
    }

    protected AbstractException(Exception e) {
        super(e);
        this.message = super.getMessage();
        this.messageTemplate = "";
    }

    protected AbstractException(String pattern, Object... args) {
        this.messageTemplate = pattern;
        this.message = MessageFormatter.arrayFormat(pattern, args).getMessage();
        this.populateMessageVars(args);
    }

    protected AbstractException(Exception e, String pattern, Object... args) {
        super(e);
        this.messageTemplate = pattern;
        this.message = MessageFormatter.arrayFormat(pattern, args).getMessage();
        this.populateMessageVars(args);
    }

    protected final void populateMessageVars(Object... args) {
        if (args.length > 0) {
            Object[] var2 = args;
            int var3 = args.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                Object arg = var2[var4];
                this.messageVars.add(MessageFormatter.format("{}", arg).getMessage());
            }
        }

    }

    public String getMessage() {
        return this.message;
    }

    public String getMessageTemplate() {
        return this.messageTemplate;
    }

    public List<String> getMessageVars() {
        return this.messageVars;
    }
}
