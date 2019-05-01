package br.inpe.forum.exception;

import java.util.NoSuchElementException;
import java.util.Objects;

import javax.validation.ConstraintViolationException;

import org.esfinge.guardian.exception.AuthorizationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.mongodb.MongoException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler({ Exception.class })
	public ResponseEntity<?> allExceptions(Exception e, WebRequest req) {
		return new ResponseEntity<>(e.getStackTrace(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler({ AuthorizationException.class })
	public ResponseEntity<?> handleAccessDeniedException(Exception ex, WebRequest request) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		StringBuffer message = new StringBuffer("{\"message\":\"");
		if (Objects.nonNull(ex.getMessage()))
			message.append(ex.getMessage() + "\"}");
		else
			message.append("unauthorized access\"}");
		return new ResponseEntity<String>(message.toString(), headers, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler({ ConstraintViolationException.class })
	public ResponseEntity<?> handleConstraintViolationException(Exception ex, WebRequest request) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		StringBuffer message = new StringBuffer("{\"message\":\"");
		if (Objects.nonNull(ex.getMessage()))
			message.append(ex.getMessage() + "\"}");
		else
			message.append("verify your request some constraint error occurred\"}");
		return new ResponseEntity<String>(message.toString(), headers, HttpStatus.CONFLICT);
	}

	@ExceptionHandler({ MongoException.class })
	public ResponseEntity<?> handleMongoClientException(Exception ex, WebRequest request) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		StringBuffer message = new StringBuffer("{\"message\":\"");
		if (Objects.nonNull(ex.getMessage()))
			message.append(ex.getMessage() + "\"}");
		else
			message.append("the server could not connect to database\"}");
		return new ResponseEntity<String>(message.toString(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler({ UsernameNotFoundException.class, NoSuchElementException.class })
	public ResponseEntity<?> handleUserDetailsException(Exception ex, WebRequest request) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		StringBuffer message = new StringBuffer("{\"message\":\"");
		if (Objects.nonNull(ex.getMessage()))
			message.append(ex.getMessage() + "\"}");
		else
			message.append("username not found\"}");
		return new ResponseEntity<String>(message.toString(), headers, HttpStatus.NOT_FOUND);
	}

}
