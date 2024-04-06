package helpers;

import com.myhome.configuration.properties.mail.EmailTemplateLocalizationProperties;
import com.myhome.configuration.properties.mail.EmailTemplateProperties;
import com.myhome.configuration.properties.mail.MailProperties;
import com.myhome.controllers.dto.PaymentDto;
import com.myhome.controllers.dto.UserDto;
import com.myhome.domain.Amenity;
import com.myhome.domain.Community;
import com.myhome.domain.CommunityHouse;
import com.myhome.domain.HouseMember;
import com.myhome.domain.Payment;
import com.myhome.domain.User;
import com.myhome.model.HouseMemberDto;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.imageio.ImageIO;

import static helpers.TestUtils.CommunityHouseHelpers.getTestHouses;
import static helpers.TestUtils.General.generateUniqueId;
import static helpers.TestUtils.UserHelpers.getTestUsers;

public class TestUtils {

  public static class General {

    /**
     * creates a new `BufferedImage` object with specified dimensions, then writes it to
     * a byte array using `ImageIO`. The resulting byte array contains the image data in
     * JPEG format.
     * 
     * @param height height of the image in pixels that will be generated when calling
     * the `getImageAsByteArray()` method.
     * 
     * @param width width of the image that is to be converted into a byte array.
     * 
     * @returns a byte array containing the image data in JPEG format.
     */
    public static byte[] getImageAsByteArray(int height, int width) throws IOException {
      BufferedImage documentImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
      try (ByteArrayOutputStream imageBytesStream = new ByteArrayOutputStream()) {
        ImageIO.write(documentImage, "jpg", imageBytesStream);
        return imageBytesStream.toByteArray();
      }
    }

    /**
     * generates a unique, randomly-generated string of characters using the `UUID.randomUUID()`
     * method.
     * 
     * @returns a unique string of characters generated using the `UUID.randomUUID()` method.
     */
    public static String generateUniqueId() {
      return UUID.randomUUID().toString();
    }
  }

  public static class CommunityHouseHelpers {

    /**
     * generates `count` sets of a `CommunityHouse` object, each containing a unique ID
     * and default name.
     * 
     * @param count maximum number of community houses to be generated.
     * 
     * @returns a `Set` of `CommunityHouse` objects, generated randomly and limited to a
     * specified count.
     */
    public static Set<CommunityHouse> getTestHouses(int count) {
      return Stream
          .generate(() -> new CommunityHouse()
              .withHouseId(generateUniqueId())
              .withName("default-house-name")
          )
          .limit(count)
          .collect(Collectors.toSet());
    }

    /**
     * generates a new instance of the `CommunityHouse` class with a unique ID and default
     * name.
     * 
     * @returns a new `CommunityHouse` instance with a unique ID and a default community
     * name.
     */
    public static CommunityHouse getTestCommunityHouse() {
      return new CommunityHouse()
          .withHouseId(generateUniqueId())
          .withName("default-community-name");
    }

    /**
     * generates a new instance of the `CommunityHouse` class with an provided house ID
     * and a default name.
     * 
     * @param houseId identifier for the community house to be created, which is used to
     * uniquely identify the community house within the system.
     * 
     * @returns a new instance of the `CommunityHouse` class with the specified house ID
     * and default community name.
     */
    public static CommunityHouse getTestCommunityHouse(String houseId) {
      return new CommunityHouse()
          .withHouseId(houseId)
          .withName("default-community-name");
    }
  }

  public static class HouseMemberHelpers {

    /**
     * generates a set of `HouseMember` objects using a `Stream` API and returns it with
     * a specified count.
     * 
     * @param count number of `HouseMember` objects to generate and return from the function.
     * 
     * @returns a set of `HouseMember` objects generated dynamically with unique IDs and
     * default names.
     */
    public static Set<HouseMember> getTestHouseMembers(int count) {
      return Stream
          .generate(() -> new HouseMember()
              .withMemberId(generateUniqueId())
              .withName("default-house-member-name")
          )
          .limit(count)
          .collect(Collectors.toSet());
    }
    /**
     * generates a new instance of the `HouseMember` class with a unique ID and a
     * predetermined name for testing purposes.
     * 
     * @returns a new `HouseMember` object with a randomly generated ID and a default name.
     */
    public static HouseMember getTestHouseMember() {
      return new HouseMember()
              .withMemberId(generateUniqueId())
              .withName("default-house-member-name");
    }
  }

  public static class CommunityHelpers {

    /**
     * iteratively generates `count` sets of a `Community` object, each containing unique
     * ID, name, and district values.
     * 
     * @param count maximum number of community objects to return in the generated set.
     * 
     * @returns a set of `Community` objects generated randomly based on a specified count.
     */
    public static Set<Community> getTestCommunities(int count) {
      return Stream.iterate(0, n -> n + 1)
          .map(index -> getTestCommunity(
              generateUniqueId(),
              "default-community-name" + index,
              "default-community-district" + index,
              0, 0)
          )
          .limit(count)
          .collect(Collectors.toSet());
    }

    /**
     * generates a new community instance with a unique ID, name, and district.
     * 
     * @returns a `Community` object representing a fictional community with a unique ID,
     * name, and district.
     */
    public static Community getTestCommunity() {
      return getTestCommunity(
          generateUniqueId(),
          "default-community-name",
          "default-community-district",
          0, 0);
    }

    /**
     * retrieves and returns a pre-defined community instance for testing purposes, adding
     * it to the user's community list and setting the user as its admin.
     * 
     * @param admin User who will have ownership and administration rights over the
     * returned `Community`.
     * 
     * @returns a new `Community` object representing a test community with the specified
     * user as an administrator.
     */
    public static Community getTestCommunity(User admin) {
      Community testCommunity = getTestCommunity();
      admin.getCommunities().add(testCommunity);
      testCommunity.setAdmins(Collections.singleton(admin));
      return testCommunity;
    }

    /**
     * creates a new community object and sets its name, ID, district, admin count, and
     * house count. It then populates the community with houses and admins generated
     * randomly, and returns the complete community object.
     * 
     * @param communityId unique identifier of the community being created, which is used
     * to assign the community its own distinct identity and distinguish it from other
     * communities in the system.
     * 
     * @param communityName name of the community being created or retrieved, and is used
     * to set the name of the new Community object created by the function.
     * 
     * @param communityDistrict district of the community being generated, which is used
     * to create a unique identifier for the community.
     * 
     * @param adminsCount number of users to be added as administrators to the generated
     * community.
     * 
     * @param housesCount number of houses to generate for the test community.
     * 
     * @returns a fully formed `Community` object with houses and admins added.
     */
    public static Community getTestCommunity(String communityId, String communityName, String communityDistrict, int adminsCount, int housesCount) {
      Community testCommunity = new Community(
          new HashSet<>(),
          new HashSet<>(),
          communityName,
          communityId,
          communityDistrict,
          new HashSet<>()
      );
      Set<CommunityHouse> communityHouses = getTestHouses(housesCount);
      communityHouses.forEach(house -> house.setCommunity(testCommunity));
      Set<User> communityAdmins = getTestUsers(adminsCount);
      communityAdmins.forEach(user -> user.getCommunities().add(testCommunity));

      testCommunity.setHouses(communityHouses);
      testCommunity.setAdmins(communityAdmins);
      return testCommunity;
    }
  }

  public static class AmenityHelpers {

    /**
     * creates a new `Amenity` object with provided `amenityId` and `amenityDescription`,
     * and also sets the `Community` of the amenity to a test community object.
     * 
     * @param amenityId identifier of the amenity to be created or retrieved, which is
     * used to uniquely identify the amenity within the system.
     * 
     * @param amenityDescription description of the amenity that is being created, and
     * it is used to set the value of the `description` field of the resulting `Amenity`
     * object.
     * 
     * @returns a new `Amenity` object with specified `amenityId`, `amenityDescription`,
     * and `community`.
     */
    public static Amenity getTestAmenity(String amenityId, String amenityDescription) {
      return new Amenity()
          .withAmenityId(amenityId)
          .withDescription(amenityDescription)
          .withCommunity(CommunityHelpers.getTestCommunity());
    }

    /**
     * generates a set of `Amenity` objects with unique IDs, names, and descriptions using
     * a stream of randomly generated amenities. It limits the number of created amenities
     * to the input count.
     * 
     * @param count number of amenities to be generated and returned by the `getTestAmenities`
     * function.
     * 
     * @returns a set of `Amenity` objects generated randomly with predefined properties.
     */
    public static Set<Amenity> getTestAmenities(int count) {
      return Stream
          .generate(() -> new Amenity()
              .withAmenityId(generateUniqueId())
              .withName("default-amenity-name")
              .withDescription("default-amenity-description")
          )
          .limit(count)
          .collect(Collectors.toSet());
    }

  }

  public static class UserHelpers {

    /**
     * iterates over a sequence of integers, creating and returning a set of `User` objects
     * with randomly generated names, emails, and passwords, up to a specified count.
     * 
     * @param count number of users to be generated by the function.
     * 
     * @returns a set of `User` objects, each with unique ID and email address, generated
     * within a limited count.
     */
    public static Set<User> getTestUsers(int count) {
      return Stream.iterate(0, n -> n + 1)
          .map(index -> new User(
              "default-user-name" + index,
              generateUniqueId(),
              "default-user-email" + index,
              false,
              "default-user-password" + index,
              new HashSet<>(),
              new HashSet<>())
          )
          .limit(count)
          .collect(Collectors.toSet());
    }
  }

  public static class MailPropertiesHelper {

    /**
     * generates a mock MailProperties object with predefined values for various configuration
     * options, allowing for unit testing of mail-related code without relying on external
     * dependencies.
     * 
     * @returns a MailProperties object with pre-defined properties for testing purposes.
     */
    public static MailProperties getTestMailProperties() {
      MailProperties testMailProperties = new MailProperties();
      testMailProperties.setHost("test host");
      testMailProperties.setUsername("test username");
      testMailProperties.setPassword("test password");
      testMailProperties.setPort(0);
      testMailProperties.setProtocol("test protocol");
      testMailProperties.setDebug(false);
      testMailProperties.setDevMode(false);
      return testMailProperties;
    }

    /**
     * creates a new `EmailTemplateProperties` instance with custom properties for testing
     * purposes, setting the path, encoding, mode, and cache to specific values.
     * 
     * @returns an `EmailTemplateProperties` object with pre-defined properties for testing
     * purposes.
     */
    public static EmailTemplateProperties getTestMailTemplateProperties() {
      EmailTemplateProperties testMailTemplate = new EmailTemplateProperties();
      testMailTemplate.setPath("test path");
      testMailTemplate.setEncoding("test encoding");
      testMailTemplate.setMode("test mode");
      testMailTemplate.setCache(false);
      return testMailTemplate;
    }

    /**
     * generates high-quality documentation for code by creating and returning an instance
     * of `EmailTemplateLocalizationProperties` with predefined properties related to
     * email template localization, including path, encoding, and cache seconds.
     * 
     * @returns a `EmailTemplateLocalizationProperties` object containing properties for
     * testing email localization.
     */
    public static EmailTemplateLocalizationProperties getTestLocalizationMailProperties() {
      EmailTemplateLocalizationProperties testTemplatesLocalization = new EmailTemplateLocalizationProperties();
      testTemplatesLocalization.setPath("test path");
      testTemplatesLocalization.setEncoding("test encodig");
      testTemplatesLocalization.setCacheSeconds(0);
      return testTemplatesLocalization;
    }
  }

  public static class PaymentHelpers {

    /**
     * builds a `PaymentDto` object with provided parameters, including charge amount,
     * payment type, description, recurrence status, due date, and user and member details.
     * 
     * @param charge amount to be charged for the payment.
     * 
     * @param type payment type, which determines the specific payment being processed.
     * 
     * @param description description of the payment being created in the builder object,
     * which is used to construct the final PaymentDto object.
     * 
     * @param recurring whether the payment is recurring or not.
     * 
     * @param dueDate LocalDate when the payment is due, which is used to build the
     * PaymentDto object.
     * 
     * @param admin user who made the payment, and it is passed to the `PaymentDto.builder()`
     * method as an object of type `UserDto`.
     * 
     * @param member HouseMemberDto object containing information about the member whose
     * payment is being processed.
     * 
     * @returns a PaymentDto object built with the given parameters.
     */
    public static PaymentDto getTestPaymentDto(BigDecimal charge, String type, String description, boolean recurring, LocalDate dueDate, UserDto admin, HouseMemberDto member) {

      return PaymentDto.builder()
          .charge(charge)
          .type(type)
          .description(description)
          .recurring(recurring)
          .dueDate(dueDate.toString())
          .admin(admin)
          .member(member)
          .build();
    }
    /**
     * generates a Payment object with all fields set to null except for the 'recurring'
     * field which is false.
     * 
     * @returns a `Payment` object with all fields null or false except for the `recurring`
     * field.
     */
    public static Payment getTestPaymentNullFields() {
      //Only 'recurring' field will be not null, but false
      return new Payment(
          null,
          null,
          null,
          null,
          false,
          null,
          null,
          null);
    }
  }
}
