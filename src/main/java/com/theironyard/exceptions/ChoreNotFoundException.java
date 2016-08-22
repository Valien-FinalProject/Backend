package com.theironyard.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by EddyJ on 8/21/16.
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Chore was not found")
public class ChoreNotFoundException extends RuntimeException {
}
