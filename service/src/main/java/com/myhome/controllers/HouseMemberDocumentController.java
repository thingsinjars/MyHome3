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

package com.myhome.controllers;

import com.myhome.api.DocumentsApi;
import com.myhome.domain.HouseMemberDocument;
import com.myhome.services.HouseMemberDocumentService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.CacheControl;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * REST Controller which provides endpoints for managing house member documents
 */
/**
 * from the file provides functions for handling house member documents. The class
 * includes methods for retrieving and updating house member documents, as well as
 * deleting them. The functions log trace messages to indicate that they have received
 * a request to perform an operation on a house member document. The class also uses
 * the `HttpHeaders` and `ResponseEntity` objects to return status codes and headers
 * to the client.
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class HouseMemberDocumentController implements DocumentsApi {

  private final HouseMemberDocumentService houseMemberDocumentService;

  /**
   * retrieves a house member document given its ID and returns it as a byte array in
   * an HTTP response entity with appropriate headers for caching and content type.
   * 
   * @param memberId id of the house member for whom the document is being retrieved.
   * 
   * @returns a `ResponseEntity` object containing the requested house member document
   * content as a byte array, along with headers setting the cache control and content
   * type.
   * 
   * * `ResponseEntity<byte[]>` represents an entity containing the house member document
   * as a byte array.
   * * `HttpHeaders` is an instance of `HttpHeaders`, which contains headers related
   * to caching and content type.
   * * `ContentDisposition` is an instance of `ContentDisposition`, which provides
   * information about how the response should be displayed in the browser, including
   * the filename of the document.
   * * `HttpStatus.OK` indicates that the request was successful and the document could
   * be retrieved.
   * * The `map` method is used to transform the `Optional<HouseMemberDocument>` into
   * a `ResponseEntity<byte[]>` if the house member document exists, or an empty
   * `ResponseEntity` otherwise.
   */
  @Override
  public ResponseEntity<byte[]> getHouseMemberDocument(@PathVariable String memberId) {
    log.trace("Received request to get house member documents");
    Optional<HouseMemberDocument> houseMemberDocumentOptional =
        houseMemberDocumentService.findHouseMemberDocument(memberId);

    return houseMemberDocumentOptional.map(document -> {

      HttpHeaders headers = new HttpHeaders();
      byte[] content = document.getDocumentContent();

      headers.setCacheControl(CacheControl.noCache().getHeaderValue());
      headers.setContentType(MediaType.IMAGE_JPEG);

      ContentDisposition contentDisposition = ContentDisposition
          .builder("inline")
          .filename(document.getDocumentFilename())
          .build();

      headers.setContentDisposition(contentDisposition);

      return new ResponseEntity<>(content, headers, HttpStatus.OK);
    }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
  }

  /**
   * receives a request to upload a house member document and creates a new house member
   * document in the database using the provided multipart file. It then returns a
   * response entity indicating whether the document was successfully created or not.
   * 
   * @param memberId ID of the house member whose document is being uploaded.
   * 
   * @param memberDocument MultipartFile containing the member's document to be added
   * to the house member documents.
   * 
   * * `@RequestParam("memberDocument") MultipartFile memberDocument`: This represents
   * a file uploaded by the user as part of the House Member document upload process.
   * The `MultipartFile` class provides access to the file's metadata, such as its name
   * and size.
   * * `log.trace("Received request to add house member documents")` : This line logs
   * an informational message indicating that a request has been received to add new
   * House Member documents.
   * 
   * @returns a `ResponseEntity` object with a status code indicating whether the
   * document was successfully uploaded or not.
   * 
   * * `ResponseEntity.status(HttpStatus.NO_CONTENT).build()`: This returns a response
   * entity with a status code of NO_CONTENT, indicating that the operation was successful
   * and no additional content was provided.
   * * `ResponseEntity.status(HttpStatus.NOT_FOUND).build()`: This returns a response
   * entity with a status code of NOT_FOUND, indicating that the requested resource
   * could not be found.
   */
  @Override
  public ResponseEntity uploadHouseMemberDocument(
      @PathVariable String memberId, @RequestParam("memberDocument") MultipartFile memberDocument) {
    log.trace("Received request to add house member documents");

    Optional<HouseMemberDocument> houseMemberDocumentOptional =
        houseMemberDocumentService.createHouseMemberDocument(memberDocument, memberId);
    return houseMemberDocumentOptional
        .map(document -> ResponseEntity.status(HttpStatus.NO_CONTENT).build())
        .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
  }

  /**
   * updates a house member's document based on the inputted file. It returns a
   * ResponseEntity with a status code indicating whether the update was successful or
   * not.
   * 
   * @param memberId identifier of the house member whose document is being updated.
   * 
   * @param memberDocument file containing the updated member document to be saved in
   * the database by the `houseMemberDocumentService`.
   * 
   * * `memberId`: The ID of the member whose document is being updated.
   * * `memberDocument`: A `MultipartFile` object representing the document to be updated.
   * 
   * @returns a response entity with a status code of NO_CONTENT or NOT_FOUND, depending
   * on whether the update was successful.
   * 
   * * `ResponseEntity.status(HttpStatus.NO_CONTENT).build()`: This is a response entity
   * with a status code of NO_CONTENT, indicating that the request was successful and
   * no content was returned.
   * * `ResponseEntity.status(HttpStatus.NOT_FOUND).build()`: This is a response entity
   * with a status code of NOT_FOUND, indicating that the requested resource could not
   * be found.
   */
  @Override
  public ResponseEntity updateHouseMemberDocument(
      @PathVariable String memberId, @RequestParam("memberDocument") MultipartFile memberDocument) {
    log.trace("Received request to update house member documents");
    Optional<HouseMemberDocument> houseMemberDocumentOptional =
        houseMemberDocumentService.updateHouseMemberDocument(memberDocument, memberId);
    return houseMemberDocumentOptional
        .map(document -> ResponseEntity.status(HttpStatus.NO_CONTENT).build())
        .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
  }

  /**
   * deletes a house member document based on the given `memberId`. If the document is
   * deleted successfully, a `NO_CONTENT` status code is returned. Otherwise, a `NOT_FOUND`
   * status code is returned.
   * 
   * @param memberId unique identifier of the house member whose document is to be deleted.
   * 
   * @returns a `ResponseEntity` object with a status code of either `NO_CONTENT` or
   * `NOT_FOUND`, depending on whether the document was successfully deleted.
   * 
   * * `HttpStatus`: This is an instance of `HttpStatus`, which represents the HTTP
   * status code of the response. In this case, it can be either `NO_CONTENT` or `NOT_FOUND`.
   * * `ResponseEntity`: This is an instance of `ResponseEntity`, which is a higher-level
   * object that contains both the `HttpStatus` and the body of the response. In this
   * case, the body is ` Void`, which means the response has no content.
   */
  @Override
  public ResponseEntity<Void> deleteHouseMemberDocument(@PathVariable String memberId) {
    log.trace("Received request to delete house member documents");
    boolean isDocumentDeleted = houseMemberDocumentService.deleteHouseMemberDocument(memberId);
    if (isDocumentDeleted) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }
}
