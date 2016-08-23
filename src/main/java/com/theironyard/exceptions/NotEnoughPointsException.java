package com.theironyard.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by EddyJ on 8/22/16.
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "You do not have enough points for that reward")
public class NotEnoughPointsException extends RuntimeException {
}
