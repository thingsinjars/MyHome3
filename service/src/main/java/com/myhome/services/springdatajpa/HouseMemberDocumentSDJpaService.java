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

package com.myhome.services.springdatajpa;

import com.myhome.domain.HouseMember;
import com.myhome.domain.HouseMemberDocument;
import com.myhome.repositories.HouseMemberDocumentRepository;
import com.myhome.repositories.HouseMemberRepository;
import com.myhome.services.HouseMemberDocumentService;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

/**
 * TODO
 */
@Service
public class HouseMemberDocumentSDJpaService implements HouseMemberDocumentService {

  private final HouseMemberRepository houseMemberRepository;
  private final HouseMemberDocumentRepository houseMemberDocumentRepository;
  @Value("${files.compressionBorderSizeKBytes}")
  private int compressionBorderSizeKBytes;
  @Value("${files.maxSizeKBytes}")
  private int maxFileSizeKBytes;
  @Value("${files.compressedImageQuality}")
  private float compressedImageQuality;

  public HouseMemberDocumentSDJpaService(HouseMemberRepository houseMemberRepository,
      HouseMemberDocumentRepository houseMemberDocumentRepository) {
    this.houseMemberRepository = houseMemberRepository;
    this.houseMemberDocumentRepository = houseMemberDocumentRepository;
  }

  /**
   * retrieves a House Member Document associated with a given member ID from the
   * repository and maps it to an Optional<HouseMemberDocument>.
   * 
   * @param memberId unique identifier of a member in the system, which is used to
   * retrieve the corresponding `HouseMemberDocument` from the repository.
   * 
   * 	- `houseMemberRepository`: This is a repository responsible for storing and
   * retrieving House Member documents.
   * 	- `findByMemberId(memberId)`: This method returns an optional `HouseMemberDocument`
   * object that corresponds to the provided `memberId`.
   * 	- `map(HouseMember::getHouseMemberDocument)`: This line maps the returned
   * `HouseMemberDocument` object to a new `Optional<HouseMemberDocument>` object, which
   * is then returned as the function's output.
   * 
   * @returns an optional instance of `HouseMemberDocument`.
   * 
   * 	- `Optional<HouseMemberDocument>` is the type of the output, indicating that it
   * may or may not contain a value depending on whether a match was found in the repository.
   * 	- `houseMemberRepository.findByMemberId(memberId)` is the method called to retrieve
   * the House Member Document from the repository based on the input `memberId`.
   * 	- `map(HouseMember::getHouseMemberDocument)` is a method that applies the function
   * `getHouseMemberDocument` to the result of the previous call, transforming it into
   * the final output.
   */
  @Override
  public Optional<HouseMemberDocument> findHouseMemberDocument(String memberId) {
    return houseMemberRepository.findByMemberId(memberId)
        .map(HouseMember::getHouseMemberDocument);
  }

  /**
   * deletes a house member's document by finding the member in the repository, setting
   * their document to null, and saving them in the repository. If successful, it returns
   * `true`.
   * 
   * @param memberId id of the member whose house member document should be deleted.
   * 
   * 	- `memberId`: A string representing the member ID to delete the house member
   * document for.
   * 
   * The function first retrieves the house member record associated with the `memberId`.
   * If the record exists and has a non-null value for the `HouseMemberDocument` field,
   * it is set to null, and then saved in the repository. Finally, the function returns
   * a boolean indicating whether the operation was successful or not.
   * 
   * @returns a boolean value indicating whether the house member document associated
   * with the provided member ID has been successfully deleted.
   */
  @Override
  public boolean deleteHouseMemberDocument(String memberId) {
    return houseMemberRepository.findByMemberId(memberId).map(member -> {
      if (member.getHouseMemberDocument() != null) {
        member.setHouseMemberDocument(null);
        houseMemberRepository.save(member);
        return true;
      }
      return false;
    }).orElse(false);
  }

  /**
   * updates an existing House Member Document by finding the corresponding member,
   * creating a new document if necessary, and adding it to the member's record.
   * 
   * @param multipartFile file containing the House Member document to be updated, which
   * is being passed through the `findByMemberId()` method to retrieve the corresponding
   * House Member entity.
   * 
   * 	- `multipartFile`: This is an instance of the `MultipartFile` class, which contains
   * various attributes related to a file upload. These attributes may include the file
   * name, file type, size, and other metadata.
   * 
   * @param memberId unique identifier of the member whose document is being updated.
   * 
   * 	- `memberId`: This is a String attribute that represents the unique identifier
   * for a member in the system.
   * 
   * @returns an `Optional` object containing a `HouseMemberDocument`, which represents
   * the updated document for the specified member.
   * 
   * 	- `Optional<HouseMemberDocument>` represents an optional reference to a House
   * Member Document. If a document exists for the provided member ID, this output will
   * contain a non-empty reference to that document. Otherwise, it will be empty.
   * 	- The `houseMemberRepository` method call returns a `Map` containing a single
   * entry with the member ID as key and an `Optional<House Member Document>` value.
   * This map is used to retrieve the House Member Document associated with the provided
   * member ID, or an empty reference if no document exists.
   * 	- The `tryCreateDocument` method creates a new House Member Document if one does
   * not already exist for the provided member ID. If the document cannot be created
   * (e.g., due to a database constraint violation), the output will contain an empty
   * reference. Otherwise, it will contain a non-empty reference to the newly created
   * document.
   * 	- The `addDocumentToHouse Member` method adds the new or updated House Member
   * Document to the House Member's collection of documents. This is a no-op if the
   * document already exists in the collection.
   */
  @Override
  public Optional<HouseMemberDocument> updateHouseMemberDocument(MultipartFile multipartFile,
      String memberId) {
    return houseMemberRepository.findByMemberId(memberId).map(member -> {
      Optional<HouseMemberDocument> houseMemberDocument = tryCreateDocument(multipartFile, member);
      houseMemberDocument.ifPresent(document -> addDocumentToHouseMember(document, member));
      return houseMemberDocument;
    }).orElse(Optional.empty());
  }

  /**
   * takes a `MultipartFile` and a `String` member ID as input, and returns an
   * `Optional<HouseMemberDocument>` representing the created document or empty if none
   * was created. It first retrieves the member from the repository using the member
   * ID, then creates a new document using the multipart file and associates it with
   * the member using the `addDocumentToHouseMember` function. If the creation is
   * successful, the function returns an `Optional<HouseMemberDocument>` containing the
   * created document; otherwise, it returns an empty `Optional`.
   * 
   * @param multipartFile file to be processed and is used to retrieve the document
   * from the repository.
   * 
   * 	- `multipartFile`: A MultipartFile object containing the file to be processed.
   * 	- `memberId`: The ID of the member for whom the document is being created.
   * 
   * @param memberId 12-digit unique identifier of a member for whom a HouseMemberDocument
   * is to be created.
   * 
   * 	- `memberId`: A string representing the member ID to find the corresponding House
   * Member document for.
   * 
   * @returns an `Optional` object containing a `HouseMemberDocument`, which represents
   * the created document if successful, or an empty `Optional` otherwise.
   * 
   * 	- The first line returns an `Optional` object containing a `HouseMemberDocument`.
   * If a document can be created successfully, this Optional will contain a non-empty
   * value. Otherwise, it will be empty.
   * 	- The second line uses the `map` method to apply a function to the `member`
   * parameter. In this case, the function tries to create a new `HouseMemberDocument`
   * based on the provided `multipartFile` and `memberId`. If successful, this function
   * returns an `Optional` containing the newly created document.
   * 	- The third line checks if the `Optional` returned by the previous line is
   * non-empty. If it is, the function calls the `addDocumentToHouseMember` method to
   * add the new document to the associated member. This method takes no arguments.
   * 	- The fourth line returns the Optional containing the newly created or updated `HouseMemberDocument`.
   */
  @Override
  public Optional<HouseMemberDocument> createHouseMemberDocument(MultipartFile multipartFile,
      String memberId) {
    return houseMemberRepository.findByMemberId(memberId).map(member -> {
      Optional<HouseMemberDocument> houseMemberDocument = tryCreateDocument(multipartFile, member);
      houseMemberDocument.ifPresent(document -> addDocumentToHouseMember(document, member));
      return houseMemberDocument;
    }).orElse(Optional.empty());
  }

  /**
   * takes a MultipartFile and HouseMember object as input, and attempts to create a
   * document from the file using image manipulation and saving it as a JPEG file. If
   * successful, it returns an Optional containing the created HouseMemberDocument,
   * otherwise it returns an empty Optional.
   * 
   * @param multipartFile multipart file containing the image to be processed and
   * converted into a HouseMemberDocument.
   * 
   * 	- `MultipartFile multipartFile`: This represents a multipart file that contains
   * an image to be processed.
   * 	- `HouseMember member`: This parameter represents a house member whose document
   * is being created.
   * 	- `ByteArrayOutputStream imageByteStream`: A byte array output stream used to
   * capture the image data.
   * 	- `BufferedImage documentImage`: An instance of `BufferedImage` containing the
   * image data from the multipart file.
   * 	- `DataSize compressionBorderSizeKBytes`: The size of the image in kilobytes,
   * used for compression.
   * 	- `DataSize maxFileSizeKBytes`: The maximum size of a file in kilobytes, used for
   * validation.
   * 
   * @param member HouseMember object whose document is being created and saved.
   * 
   * 	- `member`: A HouseMember object representing an individual member of a house.
   * 	- `multipartFile`: A MultipartFile object containing the image file to be processed.
   * 	- `compressionBorderSizeKBytes`: The size threshold for compressing the image
   * file (in kilobytes).
   * 	- `maxFileSizeKBytes`: The maximum size allowed for the resulting document file
   * (in kilobytes).
   * 	- `getImageFromMultipartFile()`: A method that extracts an image from a MultipartFile
   * object.
   * 	- `writeImageToByteStream()`: A method that writes the extracted image to a
   * ByteArrayOutputStream object.
   * 	- `compressImageToByteStream()`: A method that compresses the image using a
   * compression algorithm.
   * 	- `saveHouseMemberDocument()`: A method that saves the compressed image to a file
   * with a specified name based on the member ID.
   * 
   * @returns an `Optional` containing a `HouseMemberDocument` object, or an empty
   * `Optional` if there was an error.
   * 
   * 	- `Optional<HouseMemberDocument>`: The output is an optional instance of
   * `HouseMemberDocument`, which represents a document related to a member of a house.
   * 	- `HouseMemberDocument`: This class represents a document related to a member of
   * a house, with properties such as the member ID and the document type.
   * 	- `member`: This is the input parameter representing the member for whom the
   * document is being created.
   * 	- `MultipartFile`: This is the input parameter representing the multipart file
   * containing the image data for the document.
   * 	- ` ByteArrayOutputStream` : This is a buffered output stream used to store the
   * image data in a byte array.
   * 	- `BufferedImage`: This is the input parameter representing the image data from
   * the multipart file, which is processed and stored in the `ByteArrayOutputStream`.
   * 	- `DataSize`: This is an intermediate variable used to compare the size of the
   * image data with the maximum allowed file size.
   * 	- `maxFileSizeKBytes`: This is a constant representing the maximum allowed file
   * size in kilobytes.
   * 	- `ImageIO`: This is a class used for reading and writing image files.
   * 	- `IOException`: This is an exception that may be thrown if there is an error
   * while reading or writing the image file.
   */
  private Optional<HouseMemberDocument> tryCreateDocument(MultipartFile multipartFile,
      HouseMember member) {

    try (ByteArrayOutputStream imageByteStream = new ByteArrayOutputStream()) {
      BufferedImage documentImage = getImageFromMultipartFile(multipartFile);
      if (multipartFile.getSize() < DataSize.ofKilobytes(compressionBorderSizeKBytes).toBytes()) {
        writeImageToByteStream(documentImage, imageByteStream);
      } else {
        compressImageToByteStream(documentImage, imageByteStream);
      }
      if (imageByteStream.size() < DataSize.ofKilobytes(maxFileSizeKBytes).toBytes()) {
        HouseMemberDocument houseMemberDocument = saveHouseMemberDocument(imageByteStream,
            String.format("member_%s_document.jpg", member.getMemberId()));
        return Optional.of(houseMemberDocument);
      } else {
        return Optional.empty();
      }
    } catch (IOException e) {
      return Optional.empty();
    }
  }

  /**
   * updates a HouseMember object's `HouseMemberDocument` field and saves it to the
   * repository, inserting or replacing the document with the given ID in the member's
   * record.
   * 
   * @param houseMemberDocument HouseMember document to be associated with the specified
   * `HouseMember`.
   * 
   * 	- `HouseMemberDocument`: Represents the document related to a house member, which
   * contains information about the member and their membership in the house.
   * 
   * @param member HouseMember to whom the `houseMemberDocument` is being added, and
   * it is set as the `HouseMemberDocument` of that member.
   * 
   * 	- `setHouseMemberDocument(houseMemberDocument)` sets the `HouseMemberDocument`
   * field of the `member` object to the provided `houseMemberDocument`.
   * 	- `save()` saves the updated `member` object in the repository.
   * 
   * @returns a saved House Member with the associated document.
   * 
   * The `houseMemberRepository.save()` method saves the updated `HouseMember` object
   * in the database. The `member` parameter is passed to this method as a reference
   * to the `HouseMember` object that contains an updated `HouseMemberDocument` field,
   * which was set to the input `house MemberDocument`.
   */
  private HouseMember addDocumentToHouseMember(HouseMemberDocument houseMemberDocument,
      HouseMember member) {
    member.setHouseMemberDocument(houseMemberDocument);
    return houseMemberRepository.save(member);
  }

  /**
   * saves a `HouseMemberDocument` object to the database, creating it first if it
   * doesn't exist and then storing its contents in the database.
   * 
   * @param imageByteStream image data of the HouseMemberDocument to be saved.
   * 
   * 	- ` ByteArrayOutputStream imageByteStream`: This is an output stream that stores
   * binary data as a byte array. The method `toByteArray()` returns the contents of
   * the stream as a byte array.
   * 
   * @param filename name of the output file for the saved HouseMemberDocument.
   * 
   * 	- `filename`: String representing the name of the document to be saved.
   * 
   * @returns a new `HouseMemberDocument` instance saved to the repository.
   * 
   * 	- `newDocument`: A new instance of `HouseMemberDocument`, representing a new
   * document created by combining the image data from `imageByteStream` with its
   * corresponding filename.
   * 	- `houseMemberDocumentRepository`: The repository responsible for storing the
   * newly created document in the database.
   */
  private HouseMemberDocument saveHouseMemberDocument(ByteArrayOutputStream imageByteStream,
      String filename) {
    HouseMemberDocument newDocument =
        new HouseMemberDocument(filename, imageByteStream.toByteArray());
    return houseMemberDocumentRepository.save(newDocument);
  }

  /**
   * converts a `BufferedImage` into a JPEG file and stores it in an `OutputStream`.
   * 
   * @param documentImage 2D image to be written to a byte stream as a JPEG file.
   * 
   * 	- `BufferedImage`: This object represents an image that is to be written to a
   * byte stream. It contains various attributes related to the image, such as its size,
   * resolution, and color depth.
   * 	- `ByteArrayOutputStream`: This object is used to store the output of the function,
   * which is a byte array representing the image data.
   * 
   * @param imageByteStream byte array that will store the written image data after
   * being converted from an image format to JPEG format.
   * 
   * 	- It is an instance of `ByteArrayOutputStream`, which is a class in Java for
   * buffering bytes.
   * 	- It has a capacity to hold at least 1024 bytes (the default size), but this can
   * be changed by the user through its constructor.
   * 	- It has a `write` method that allows you to write bytes to it.
   * 	- It does not have any other properties or attributes beyond these basic functionalities.
   */
  private void writeImageToByteStream(BufferedImage documentImage,
      ByteArrayOutputStream imageByteStream)
      throws IOException {
    ImageIO.write(documentImage, "jpg", imageByteStream);
  }

  /**
   * compresses a `BufferedImage` using an `ImageWriter`, setting the compression mode
   * and quality to specified values, and writes the compressed image to a `ByteArrayOutputStream`.
   * 
   * @param bufferedImage 2D image that will be compressed and written to an output stream.
   * 
   * 	- The `BufferedImage` object represents a bitmapped image that has been loaded
   * from an image file or stream.
   * 	- It has several attributes such as `width`, `height`, `colorspace`, `imageType`,
   * and others.
   * 	- These attributes provide information about the image's format, size, and other
   * characteristics.
   * 
   * @param imageByteStream output stream where the compressed image will be written.
   * 
   * 	- `BufferedImage bufferedImage`: The original image to be compressed.
   * 	- `ByteArrayOutputStream imageByteStream`: A buffered output stream used to store
   * the compressed image data as a byte array.
   * 	- `IOException`: An exception class that may be thrown if there is an error during
   * compression or output.
   * 	- `ImageIO`: A class that provides methods for reading and writing images in
   * various formats.
   * 	- `ImageWriter`: An interface that provides methods for writing images to various
   * output streams.
   * 	- `ImageWriteParam`: An interface that provides methods for configuring the
   * compression settings for an image write operation.
   * 	- `MODE_EXPLICIT`: A value representing the compression mode, which can be either
   * "EXPLICIT" or "IMPLICIT".
   * 	- `compressedImageQuality`: A value representing the quality of the compressed
   * image, which can range from 0 to 100.
   */
  private void compressImageToByteStream(BufferedImage bufferedImage,
      ByteArrayOutputStream imageByteStream) throws IOException {

    try (ImageOutputStream imageOutStream = ImageIO.createImageOutputStream(imageByteStream)) {

      ImageWriter imageWriter = ImageIO.getImageWritersByFormatName("jpg").next();
      imageWriter.setOutput(imageOutStream);
      ImageWriteParam param = imageWriter.getDefaultWriteParam();

      if (param.canWriteCompressed()) {
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(compressedImageQuality);
      }
      imageWriter.write(null, new IIOImage(bufferedImage, null, null), param);
      imageWriter.dispose();
    }
  }

  /**
   * reads an image from an InputStream created from a MultipartFile object and returns
   * the image as a BufferedImage object.
   * 
   * @param multipartFile MultipartFile object that contains the image file to be read.
   * 
   * 	- `InputStream multipartFileStream`: A stream of binary data representing the
   * contents of the uploaded file.
   * 	- `MultipartFile multipartFile`: The file being processed, containing information
   * such as the file name, size, and content type.
   * 
   * @returns a `BufferedImage` object.
   * 
   * 	- The output is an instance of `BufferedImage`, which represents a raster image
   * in Java.
   * 	- The image is read from the input stream provided by the `MultipartFile` object
   * using the `ImageIO.read()` method.
   * 	- The resulting image is stored in the `BufferedImage` instance for later use or
   * processing.
   */
  private BufferedImage getImageFromMultipartFile(MultipartFile multipartFile) throws IOException {
    try (InputStream multipartFileStream = multipartFile.getInputStream()) {
      return ImageIO.read(multipartFileStream);
    }
  }
}
