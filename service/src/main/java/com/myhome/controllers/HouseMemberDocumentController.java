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
   * retrieves a house member document from the service and returns it as a byte array
   * with an appropriate HTTP status code and headers.
   * 
   * @param memberId ID of the house member whose document is requested.
   * 
   * 	- `memberId`: A string representing the member ID for which the document is being
   * requested.
   * 
   * @returns a `ResponseEntity` object containing the requested document content as a
   * byte array, with appropriate headers and status code.
   * 
   * 	- `HttpHeaders headers`: This object contains metadata about the response, such
   * as caching instructions and content type.
   * 	- `byte[] content`: The document's contents, represented as a byte array.
   * 	- `ContentDisposition contentDisposition`: A builder for creating a ContentDisposition
   * header, which specifies how the response should be handled by the client. In this
   * case, it is set to "inline" and includes the filename of the document.
   * 	- `ResponseEntity<byte[]> entity`: The overall response object, containing the
   * headers and content.
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
   * processes a request to add a house member document, creating a new document entity
   * if one does not exist for the provided member ID, and returning a `NO_CONTENT`
   * status code upon successful addition or a `NOT_FOUND` status code otherwise.
   * 
   * @param memberId unique identifier of the house member whose document is being uploaded.
   * 
   * 	- `memberId`: A string representing the unique identifier for a house member.
   * 
   * @param memberDocument document to be uploaded for the specified member ID.
   * 
   * 	- `@RequestParam("memberDocument") MultipartFile memberDocument`: This is a request
   * parameter containing the file to be uploaded as a house member document. The type
   * of this parameter is `MultipartFile`, which means it can contain only files and
   * not other types of data.
   * 	- `log.trace("Received request to add house member documents"`: This line logs a
   * trace message indicating that the function has received a request to upload a house
   * member document.
   * 
   * @returns a `ResponseEntity` object with a status code indicating whether the
   * operation was successful or not.
   * 
   * 	- `ResponseEntity.status(HttpStatus.NO_CONTENT)` - This status code indicates
   * that the request was successful and no content was returned.
   * 	- `ResponseEntity.status(HttpStatus.NOT_FOUND)` - This status code indicates that
   * the requested member document could not be found.
   * 
   * The function returns an `Optional` object, which contains a `HouseMemberDocument`
   * object if it exists, or an empty `Optional` if it does not exist. The `map` method
   * is used to transform the `Optional` into a `ResponseEntity` object with a status
   * code indicating whether the request was successful or not. If the `Optional` is
   * empty, the function returns a `ResponseEntity` with a status code of `HttpStatus.NOT_FOUND`.
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
   * updates a house member's document based on a provided MultipartFile and member ID,
   * returning a ResponseEntity with a NO_CONTENT status if successful or a NOT_FOUND
   * status otherwise.
   * 
   * @param memberId 12-digit unique identifier of the member whose document is being
   * updated.
   * 
   * 	- `memberId`: A string representing the unique identifier of a house member.
   * 
   * The function first logs a message to trace the request receipt and then attempts
   * to update the house member document using the `houseMemberDocumentService`. If the
   * update is successful, the function returns a `ResponseEntity` with a status code
   * of `NO_CONTENT`, indicating that the operation was successful. Otherwise, it returns
   * a `ResponseEntity` with a status code of `NOT_FOUND`, indicating that the house
   * member document could not be found.
   * 
   * @param memberDocument file containing the member's document that needs to be updated.
   * 
   * 	- `memberId`: A string representing the ID of the house member whose document is
   * being updated.
   * 	- `memberDocument`: A `MultipartFile` object containing the updated document for
   * the house member.
   * 
   * @returns a `ResponseEntity` object with a status code of either `NO_CONTENT` or
   * `NOT_FOUND`, depending on whether the document was successfully updated or not.
   * 
   * 	- `map`: This method is used to map the Optional<HouseMemberDocument> to a
   * ResponseEntity. If the update operation was successful, it returns a ResponseEntity
   * with a status code of NO_CONTENT (204).
   * 	- `orElseGet`: This method is used as a fallback when the `map` method returns
   * an empty Optional. It returns a ResponseEntity with a status code of NOT_FOUND (404).
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
   * deletes a house member document based on the provided `memberId`. If the document
   * is successfully deleted, a `HttpStatus.NO_CONTENT` response is returned. If the
   * document cannot be found, a `HttpStatus.NOT_FOUND` response is returned.
   * 
   * @param memberId ID of the house member whose document is to be deleted.
   * 
   * 	- `memberId`: This is a string variable that represents the unique identifier for
   * a house member. It could be obtained from various sources such as user input or
   * data stored in a database.
   * 
   * @returns a `ResponseEntity` with a status code of either `NO_CONTENT` or `NOT_FOUND`,
   * depending on whether the document was successfully deleted or not.
   * 
   * 	- `isDocumentDeleted`: A boolean value indicating whether the house member document
   * was successfully deleted or not.
   * 	- `HttpStatus`: The HTTP status code returned by the function, which can be either
   * `NO_CONTENT` (204) or `NOT_FOUND` (404).
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
