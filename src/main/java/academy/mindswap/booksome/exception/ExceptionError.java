package academy.mindswap.booksome.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionError {
    private Date timestamp;
    private String message;
    private String method;
    private String path;
}
