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
 * TODO
 */
@ControllerAdvice
public class FileUploadExceptionAdvice {

  /**
   * processes an exception thrown by the server when a file size exceeds the allowed
   * limit, returning a customized response entity with an error message.
   * 
   * @param exc MaxUploadSizeExceededException that occurred and is passed to the
   * function for handling.
   * 
   * 	- `MaxUploadSizeExceededException`: This is the exception class that was caught
   * and handled by the function.
   * 	- `class`: This refers to the class of the exception, which in this case is `MaxUploadSizeExceededException`.
   * 	- `exc`: This refers to the instance of the exception class that was caught and
   * handled by the function.
   * 
   * @returns a `ResponseEntity` with a status code of `PAYLOAD_TOO_LARGE` and a body
   * containing a map with a single entry containing the message "File size exceeds limit!".
   * 
   * 	- The HTTP status code is `PAYLOAD_TOO_LARGE`, indicating that the file size
   * exceeds the limit.
   * 	- The body of the response entity contains a map with a single key-value pair.
   * The key is `"message"` and the value is a string containing the error message
   * `"File size exceeds limit!"`.
   */
  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ResponseEntity handleMaxSizeException(MaxUploadSizeExceededException exc) {
    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(new HashMap<String, String>() {{
      put("message", "File size exceeds limit!");
    }});
  }

  /**
   * processes an `MaxUploadSizeExceededException` and returns a custom response entity
   * with a message indicating something went wrong during document saving.
   * 
   * @param exc `MaxUploadSizeExceededException` that needs to be handled by the function.
   * 
   * 	- `MaxUploadSizeExceededException`: This exception is an instance of the `IOException`
   * class and represents an error that occurred during document saving due to exceeding
   * the maximum upload size limit.
   * 
   * @returns a `ResponseEntity` with a status code of `CONFLICT` and a message body
   * containing the error message "Something went wrong with document saving!".
   * 
   * 	- `HttpStatus`: The HTTP status code of the response entity, which is set to `CONFLICT`.
   * 	- `body`: A map containing a single key-value pair, where the key is `"message"`
   * and the value is a string representing an error message related to document saving.
   */
  @ExceptionHandler(IOException.class)
  public ResponseEntity handleIOException(MaxUploadSizeExceededException exc) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(new HashMap<String, String>() {{
      put("message", "Something go wrong with document saving!");
    }});
  }
}

