package itmo.java.advanced_124_31.model.exceptions;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public class CustomException extends RuntimeException {

	private final String message;
	private final HttpStatus status;
}
