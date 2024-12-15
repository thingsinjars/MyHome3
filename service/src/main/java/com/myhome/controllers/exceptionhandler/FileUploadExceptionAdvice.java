/*
 * Copyright 2020 Prathab Murugan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.myhome.controllers.exceptionhandler;

import java.io.IOException;
import java.util.HashMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * is an exception handler that processes exceptions related to file size exceeding
 * limits and document saving errors. The class provides custom response entities
 * with error messages for each type of exception.
 */
@ControllerAdvice
public class FileUploadExceptionAdvice {

  /**
   * handles MaxUploadSizeExceededException by returning a response entity with an error
   * message.
   * 
   * @param exc MaxUploadSizeExceededException object that is passed to the function.
   * 
   * * `class`: The class of the exception object, which in this case is `MaxUploadSizeExceededException`.
   * * `message`: A string attribute containing a message about the file size exceeding
   * the limit.
   * 
   * @returns a response entity with a HTTP status code of `PAYLOAD_TOO_LARGE` and a
   * message body containing the error message.
   * 
   * * The `ResponseEntity` object is an instance of the `ResponseEntity` class in Java,
   * which represents a response entity with a status code and a body.
   * * The `status` field of the `ResponseEntity` object is set to `HttpStatus.PAYLOAD_TOO_LARGE`,
   * indicating that the uploaded file size exceeds the limit.
   * * The `body` field of the `ResponseEntity` object is a map containing a single
   * key-value pair, where the key is `"message"` and the value is a string containing
   * the error message `"File size exceeds limit!"`.
   */
  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ResponseEntity handleMaxSizeException(MaxUploadSizeExceededException exc) {
    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(new HashMap<String, String>() {{
      put("message", "File size exceeds limit!");
    }});
  }

  /**
   * handles an IOException exception by returning a ResponseEntity object with a status
   * code of CONFLICT and a message body containing a custom error message.
   * 
   * @param exc `MaxUploadSizeExceededException` that the function is designed to handle.
   * 
   * * `MaxUploadSizeExceededException`: This is the type of exception handled by this
   * function.
   * * `exc`: The deserialized input representing an instance of `MaxUploadSizeExceededException`.
   * 
   * @returns a `ResponseEntity` with a status code of `CONFLICT` and a message body
   * containing the error message "Something went wrong with document saving!".
   * 
   * * The status code of the response entity is `HttpStatus.CONFLICT`, indicating an
   * error condition.
   * * The body of the response entity contains a map with a single key-value pair,
   * where the key is "message" and the value is a string containing the error message
   * "Something went wrong with document saving!".
   */
  @ExceptionHandler(IOException.class)
  public ResponseEntity handleIOException(MaxUploadSizeExceededException exc) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(new HashMap<String, String>() {{
      put("message", "Something go wrong with document saving!");
    }});
  }
}

