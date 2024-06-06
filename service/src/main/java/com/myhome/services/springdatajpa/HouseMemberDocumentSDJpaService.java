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
 * is responsible for handling operations related to creating, updating, and retrieving
 * documents associated with house members. The service provides methods for creating
 * new documents, updating existing ones, and saving them to the repository. It also
 * includes a method for trying to create a document from an image file and saves it
 * to the repository if successful. Additionally, the class includes a method for
 * compressing and writing an image to a byte stream, as well as reading an image
 * from an InputStream created from a MultipartFile object and returning it as a
 * BufferedImage object.
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
   * @param memberId unique identifier of a member within the HouseMemberRepository,
   * which is used to locate the corresponding HouseMemberDocument in the repository.
   * 
   * @returns an Optional object containing a HouseMemberDocument if found, otherwise
   * empty.
   * 
   * The returned Optional value represents a HouseMemberDocument object, which contains
   * information about a member's house membership document. The map method is used to
   * extract the HouseMemberDocument attribute from the HouseMember object.
   */
  @Override
  public Optional<HouseMemberDocument> findHouseMemberDocument(String memberId) {
    return houseMemberRepository.findByMemberId(memberId)
        .map(HouseMember::getHouseMemberDocument);
  }

  /**
   * deletes a member's document from the repository by finding the member record,
   * setting the document to null, and saving the updated member record.
   * 
   * @param memberId ID of a member to delete their house member document.
   * 
   * @returns a boolean value indicating whether the member document was successfully
   * deleted.
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
   * updates an existing House Member Document by creating a new document using a
   * provided multipart file and member ID, then adding it to the House Member's documents
   * if successful.
   * 
   * @param multipartFile file containing the House Member Document that needs to be updated.
   * 
   * * `multipartFile`: A `MultipartFile` object representing a file to be updated in
   * the HouseMember document.
   * * `memberId`: A string representing the member ID associated with the HouseMember
   * document.
   * 
   * @param memberId identifier of the member whose House Member Document is being updated.
   * 
   * @returns an `Optional` containing a `HouseMemberDocument`, which represents the
   * updated document for the specified member.
   * 
   * * The function returns an `Optional<HouseMemberDocument>` object, which represents
   * a possible value of a House Member Document. This means that if no document is
   * found or created successfully, the function will return an empty `Optional`.
   * * The `houseMemberRepository.findByMemberId(memberId)` method is called to retrieve
   * a `List` of `HouseMemberDocument` objects associated with the given member ID.
   * This method returns a non-empty list if at least one document exists for the given
   * member ID.
   * * The `map()` method is used to apply a function to each element in the list, which
   * creates a new `Optional<HouseMemberDocument>` object for each document found. The
   * function takes two arguments: the first is the `MultipartFile` object passed as
   * an argument to the function, and the second is the `member` object retrieved from
   * the repository.
   * * The `tryCreateDocument()` method is called on each `HouseMemberDocument` object
   * created in this way, with the `multipartFile` object as its only argument. This
   * method creates a new document if one does not already exist for the given member
   * ID and returns an `Optional<HouseMemberDocument>` object representing the newly
   * created document.
   * * The `addDocumentToHouseMember()` method is called on each `HouseMemberDocument`
   * object that was successfully created, with the `member` object passed as its only
   * argument. This method adds the document to the House Member's membership record.
   * * The `orElse()` method is used to combine the result of the `map()` method call
   * with an empty `Optional<HouseMemberDocument>` object if no documents were created
   * successfully. This produces a final `Optional<HouseMemberDocument>` object
   * representing the updated state of the House Member's membership records.
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
   * creates a new House Member Document based on a provided MultipartFile and member
   * ID, and returns an Optional containing the created document or an empty one if
   * failed to create.
   * 
   * @param multipartFile file to be processed and transformed into a `HouseMemberDocument`.
   * 
   * * `multipartFile`: A `MultipartFile` object representing a file upload. Its
   * properties include the file name, content type, and size.
   * 
   * @param memberId ID of the member for whom the House Member Document is being created.
   * 
   * @returns an `Optional` of a `HouseMemberDocument`.
   * 
   * * The first step is to find the House Member by memberId using the
   * `houseMemberRepository.findByMemberId(memberId)` method. This returns an
   * Optional<HouseMember> object, which contains a reference to the House Member if
   * found, or an empty Optional if not found.
   * * The second step is to create a new House Member Document using the
   * `tryCreateDocument(multipartFile, member)` method. If the creation is successful,
   * this method returns an Optional<HouseMemberDocument> object containing the newly
   * created document. If the creation fails, the method returns an empty Optional.
   * * The third step is to add the newly created House Member Document to the House
   * Member using the `addDocumentToHouseMember(document, member)` method. This method
   * takes the created document and the House Member as arguments and adds the document
   * to the House Member's documents list.
   * * The final output of the function is an Optional<HouseMemberDocument> object that
   * contains the newly created or updated House Member Document, if any. If no House
   * Member Document was created or updated, the Optional will be empty.
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
   * takes a MultipartFile and a HouseMember as input, attempts to create a document
   * from the file and save it with a unique name based on the HouseMember's ID. If
   * successful, an Optional<HouseMemberDocument> is returned containing the saved document.
   * 
   * @param multipartFile file containing the image that needs to be converted into a
   * document.
   * 
   * * `multipartFile`: A `MultipartFile` object representing an uploaded file. It has
   * various attributes such as size, content type, and name.
   * * `member`: An instance of the `HouseMember` class, which contains information
   * about a member of a house.
   * * `getImageFromMultipartFile()`: A method that retrieves the image contained within
   * the `multipartFile`. Its implementation is not shown here.
   * * `writeImageToByteStream()` and `compressImageToByteStream()`: Methods that write
   * or compress the retrieved image to a `ByteArrayOutputStream` object, respectively.
   * Their implementations are not shown here.
   * * `saveHouseMemberDocument()`: A method that saves the compressed or uncompressed
   * image to a file with a specific name based on the `member` instance's `memberId`.
   * Its implementation is not shown here.
   * * `DataSize`: An enum class representing different sizes of data, such as kilobytes
   * or megabytes. It is used to check if the image needs to be compressed or not.
   * 
   * @param member HouseMember for whom the document is being created and used to
   * generate the file name of the resulting document.
   * 
   * * `member`: A HouseMember object with attributes such as `MemberId`, `Name`,
   * `Surname`, etc.
   * 
   * @returns an `Optional` object containing a `HouseMemberDocument` if the image could
   * be successfully compressed and saved, otherwise it is empty.
   * 
   * * The `Optional<HouseMemberDocument>` returned is an optional object that contains
   * a HouseMemberDocument or is empty if there is no document to be created.
   * * If the image size is less than the compression border size in bytes, the original
   * image is written directly to a byte stream without compression.
   * * If the image size is greater than the compression border size in bytes, the image
   * is compressed to a byte stream using the `compressImageToByteStream` method.
   * * The resulting HouseMemberDocument is saved with a file name formatted by
   * concatenating the member ID with the file extension ".jpg".
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
   * updates a `HouseMember` instance's `HouseMemberDocument` field with a provided
   * document, then saves the updated member to the repository.
   * 
   * @param houseMemberDocument House Member Document associated with the member, which
   * is assigned to the member upon calling the `addDocumentToHouseMember` method.
   * 
   * * `HouseMemberDocument`: This is the class that represents the document related
   * to the HouseMember.
   * * `member`: This is the HouseMember object that will be updated with the document
   * information.
   * * `houseMemberRepository`: This is a repository interface for saving or updating
   * HouseMembers in the database.
   * 
   * @param member House Member to which the provided `HouseMemberDocument` belongs,
   * and it is updated by setting its `HouseMemberDocument` field to the provided document.
   * 
   * * `member`: A `HouseMember` object with a reference to a `HouseMemberDocument`.
   * 
   * @returns a saved HouseMember object with the updated document field.
   * 
   * The function takes two arguments: `houseMemberDocument` and `member`. The
   * `houseMemberDocument` is a `HouseMemberDocument` object that represents the document
   * related to the member, while `member` is an instance of `HouseMember` that contains
   * information about the member.
   * 
   * The function first updates the `HouseMember` object `member` by setting its
   * `HouseMemberDocument` field to the provided `houseMemberDocument`. This update is
   * done using the `setHouseMemberDocument` method.
   * 
   * Next, the updated `member` object is saved in the `houseMemberRepository` using
   * the `save` method. The `save` method returns a new instance of `HouseMember`, which
   * represents the updated member object in the repository.
   * 
   * Therefore, the output of the `addDocumentToHouse Member` function is a new instance
   * of `HouseMember` that represents the updated member object in the repository,
   * including any changes made to the `HouseMemberDocument` field.
   */
  private HouseMember addDocumentToHouseMember(HouseMemberDocument houseMemberDocument,
      HouseMember member) {
    member.setHouseMemberDocument(houseMemberDocument);
    return houseMemberRepository.save(member);
  }

  /**
   * saves a `HouseMemberDocument` object to the repository, which is created by combining
   * an image file with its corresponding filename and storing it as a byte array.
   * 
   * @param imageByteStream 2D image of a house member that is being saved to the repository.
   * 
   * * `ByteArrayOutputStream`: This is an output stream that stores binary data in a
   * byte array.
   * * `imageByteStream`: The input stream contains image data serialized as bytes.
   * 
   * @param filename name of the file to which the HouseMemberDocument will be saved.
   * 
   * @returns a `HouseMemberDocument` object representing the saved document.
   * 
   * * `HouseMemberDocument`: This is the type of object that is returned by the function,
   * which represents a document containing information about a member of a house.
   * * `filename`: This is the name of the file that contains the image data for the document.
   * * `imageByteStream`: This is a byte array containing the image data for the document.
   */
  private HouseMemberDocument saveHouseMemberDocument(ByteArrayOutputStream imageByteStream,
      String filename) {
    HouseMemberDocument newDocument =
        new HouseMemberDocument(filename, imageByteStream.toByteArray());
    return houseMemberDocumentRepository.save(newDocument);
  }

  /**
   * converts a `BufferedImage` into a byte stream using `ImageIO`.
   * 
   * @param documentImage 2D graphical representation of an image that is to be written
   * to a byte stream using the `ImageIO.write()` method.
   * 
   * * The `BufferedImage` object represents an image that has been loaded from a file
   * or other external source and is stored in memory for further processing.
   * * The `ImageIO` class is used to write the image data to a byte stream, which can
   * be used for later writing or reading of the image.
   * * The image data is written to the byte stream using the `write()` method, with
   * the file format specified as "jpg".
   * 
   * @param imageByteStream byte stream where the image will be written.
   * 
   * * The `ByteArrayOutputStream` is an output stream that stores binary data as a
   * sequence of bytes in a byte array.
   * * It has the ability to be reset and reused for multiple writes.
   * * The `write` method takes three arguments: the image object, the desired image
   * format (in this case "jpg"), and the output stream.
   */
  private void writeImageToByteStream(BufferedImage documentImage,
      ByteArrayOutputStream imageByteStream)
      throws IOException {
    ImageIO.write(documentImage, "jpg", imageByteStream);
  }

  /**
   * compresses a `BufferedImage` using an `ImageWriter` and writes it to a `
   * ByteArrayOutputStream`. It sets the compression mode and quality based on user input.
   * 
   * @param bufferedImage 2D image to be compressed and written to a byte stream.
   * 
   * * `BufferedImage`: This class represents a raster image. It has various properties
   * such as dimensions, color model, and sample model.
   * * `ImageOutputStream`: This class is used to write an image to a stream in a
   * platform-independent manner. It provides methods for setting the image writer and
   * writing the image data.
   * * `ImageWriter`: This interface represents an image writer that can write an image
   * to a stream. It has a `setOutput` method for setting the output stream, which is
   * implemented by `ImageOutputStream`.
   * * `ImageWriteParam`: This class represents the parameters used when writing an
   * image. It has methods for setting compression mode and quality, which are used in
   * the `write` method of `ImageWriter`.
   * * `IIOImage`: This class represents an IIO (Independent Image Objects) image, which
   * is a composite object that contains an image and additional metadata. In this
   * function, it is created from the `bufferedImage` using the `IIOImage` constructor.
   * 
   * @param imageByteStream byte array that will be used to store the compressed image
   * data.
   * 
   * * `BufferedImage bufferedImage`: The original image to be compressed.
   * * `ByteArrayOutputStream imageByteStream`: A streaming output capable of producing
   * a byte array representation of the image.
   * * `IOException thrown IOException`: This exception may be thrown if there is an
   * error during the compression process.
   * * `ImageWriter imageWriter`: An object that manages the writing of images in a
   * format-specific way.
   * * `ImageWriteParam param`: An object that stores parameters for image writing,
   * such as quality and compression mode.
   * 
   * The function first tries to create an ImageOutputStream capable of writing an image
   * in JPEG format using `ImageIO.createImageOutputStream`. It then creates an instance
   * of ImageWriter and sets its output to the ImageOutputStream created earlier. Next,
   * it retrieves the default write parameters for the ImageWriter, which includes the
   * quality and compression mode settings. If the ImageWriter can write compressed
   * data, the function sets the compression mode and quality settings accordingly.
   * Finally, it writes the original image to the ImageOutputStream using `ImageWriter.write`.
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
   * reads an image from an input stream provided by a `MultipartFile` object and returns
   * a `BufferedImage`.
   * 
   * @param multipartFile MultipartFile object containing the image data to be read and
   * returned as a BufferedImage.
   * 
   * * `InputStream multipartFileStream`: This is an input stream that represents the
   * contents of the `MultipartFile`. It is obtained by calling the `getInputStream()`
   * method on the `MultipartFile` object.
   * 
   * @returns a `BufferedImage`.
   * 
   * * The output is a `BufferedImage` object, which represents a raster image in Java.
   * * The image data is read from an input stream provided by the `MultipartFile` object.
   * * The `ImageIO` class is used to read the image data from the input stream.
   */
  private BufferedImage getImageFromMultipartFile(MultipartFile multipartFile) throws IOException {
    try (InputStream multipartFileStream = multipartFile.getInputStream()) {
      return ImageIO.read(multipartFileStream);
    }
  }
}
