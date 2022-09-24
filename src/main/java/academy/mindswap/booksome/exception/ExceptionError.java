package academy.mindswap.booksome.exception;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ExceptionError {
    private Date timestamp;
    private String message;
    private String method;
    private String path;
}
