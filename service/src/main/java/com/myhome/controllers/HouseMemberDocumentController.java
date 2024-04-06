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
 * TODO
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class HouseMemberDocumentController implements DocumentsApi {

  private final HouseMemberDocumentService houseMemberDocumentService;

  /**
   * retrieves a house member document given its ID, and returns it as a byte array
   * with appropriate HTTP headers for caching and content type.
   * 
   * @param memberId unique identifier of the house member for whom the document is
   * being retrieved.
   * 
   * 	- `memberId`: A string parameter that represents the unique identifier for a house
   * member.
   * 
   * @returns a `ResponseEntity<byte[]>` object containing the requested document content.
   * 
   * 	- `byte[] content`: The document's content as a byte array.
   * 	- `HttpHeaders headers`: The HTTP headers for the response, including the
   * `Cache-Control` and `Content-Type` headers with appropriate values.
   * 	- `HttpStatus status`: The HTTP status code of the response, which is set to `OK`
   * in this case.
   * 
   * The `map` method is used to generate the response entity if a `HouseMemberDocument`
   * is found, or to return a `ResponseEntity` with a `NOT_FOUND` status code otherwise.
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
   * receives a request to add a house member document, validates it with the House
   * Member Document Service, and returns a response entity indicating whether the
   * validation was successful or not.
   * 
   * @param memberId unique identifier of the member whose document is being uploaded.
   * 
   * 	- `memberId`: This is the identifier for the member whose document is being
   * uploaded. It is a String type and represents a unique identifier for the member
   * within the application.
   * 
   * @param memberDocument document to be uploaded for a house member, which is passed
   * through the `MultipartFile` class and used by the `houseMemberDocumentService` to
   * create a new `HouseMemberDocument`.
   * 
   * 	- `memberId`: A string representing the member ID whose document is being uploaded.
   * 	- `memberDocument`: An instance of `MultipartFile`, which contains the document
   * to be uploaded for the specified member ID.
   * 
   * @returns a `ResponseEntity` object with a status code indicating whether the
   * operation was successful or not.
   * 
   * 	- `ResponseEntity.status(HttpStatus.NO_CONTENT)` represents a successful response
   * with no content, indicating that the document has been successfully uploaded and
   * no further action is required.
   * 	- `ResponseEntity.status(HttpStatus.NOT_FOUND)` represents a failed response with
   * a 404 status code, indicating that the member with the provided ID could not be found.
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
   * updates a house member's document based on a provided MultipartFile. If successful,
   * it returns a `HttpStatus.NO_CONTENT` response entity. Otherwise, it returns a
   * `HttpStatus.NOT_FOUND` response entity.
   * 
   * @param memberId 12-digit unique identifier of the member whose document is being
   * updated.
   * 
   * 	- `@PathVariable String memberId`: The ID of the house member whose document is
   * being updated.
   * 	- `@RequestParam("memberDocument") MultipartFile memberDocument`: The updated
   * document for the specified house member.
   * 
   * @param memberDocument document to be updated for a specific house member.
   * 
   * 	- `memberId`: A string representing the member ID for which the document is to
   * be updated.
   * 	- `memberDocument`: A MultipartFile object containing the updated member document.
   * 
   * @returns a response entity with a status code of either `NO_CONTENT` or `NOT_FOUND`,
   * depending on whether the update was successful.
   * 
   * 	- `map`: This method is used to map the updated House Member Document to a Response
   * Entity with a status code of `NO_CONTENT`. If the update operation succeeds, this
   * method will return a `ResponseEntity` with a status code of `NO_CONTENT`, indicating
   * that the document has been updated successfully.
   * 	- `orElseGet`: This method is used as a fallback to return a `ResponseEntity`
   * with a status code of `NOT_FOUND` if the update operation fails. This indicates
   * that the document could not be found or updated.
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
   * deletes a house member document based on the inputted memberId. If the document
   * is successfully deleted, a NO_CONTENT status code is returned. Otherwise, a NOT_FOUND
   * status code is returned.
   * 
   * @param memberId unique identifier of the house member whose document is to be deleted.
   * 
   * 	- `memberId`: A string representing the unique identifier for a house member in
   * the system. It is the path variable passed to the function.
   * 
   * @returns a `ResponseEntity` object with a status code indicating whether the
   * document was successfully deleted or not.
   * 
   * 	- `HttpStatus`: This is an integer value that represents the HTTP status code of
   * the response. In this case, it is either `NO_CONTENT` or `NOT_FOUND`.
   * 	- `ResponseEntity`: This is an object that contains the HTTP status code and other
   * information about the response, such as the headers and body.
   * 	- `Void`: This is a type parameter that represents the type of the value returned
   * by the function, which is void in this case.
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
