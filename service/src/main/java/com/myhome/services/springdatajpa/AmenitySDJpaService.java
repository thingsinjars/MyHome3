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

import com.myhome.controllers.mapper.AmenityApiMapper;
import com.myhome.domain.Amenity;
import com.myhome.domain.Community;
import com.myhome.model.AmenityDto;
import com.myhome.repositories.AmenityRepository;
import com.myhome.repositories.CommunityRepository;
import com.myhome.services.AmenityService;
import com.myhome.services.CommunityService;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * TODO
 */
@Service
@RequiredArgsConstructor
public class AmenitySDJpaService implements AmenityService {

  private final AmenityRepository amenityRepository;
  private final CommunityRepository communityRepository;
  private final CommunityService communityService;
  private final AmenityApiMapper amenityApiMapper;

  /**
   * takes a set of `AmenityDto` objects, a community ID, and returns an `Optional`
   * list of created `Amenity` objects. It retrieves the community details using the
   * ID, maps each `AmenityDto` to an `Amenity` object, saves the mapped objects in the
   * repository, and returns the created amenities in an `Optional` list.
   * 
   * @param amenities set of amenities to be created or updated in the system, which
   * are then transformed into a list of `AmenityDto` objects and saved in the database
   * using the `amenityRepository`.
   * 
   * 	- `Set<AmenityDto> amenities`: This parameter represents a set of `AmenityDto`
   * objects that will be transformed into `Amenity` objects.
   * 	- `String communityId`: This parameter represents the ID of a `Community` object,
   * which is used to retrieve the details of the community from the service.
   * 
   * The function first checks if the community with the provided ID exists by calling
   * the `communityService.getCommunityDetailsById(communityId)` method. If the community
   * does not exist, the function returns an empty `Optional`. Otherwise, it proceeds
   * to transform each `AmenityDto` object in the `amenities` set into a corresponding
   * `Amenity` object using the `amenityApiMapper.amenityDtoToAmenity()` method. The
   * transformed `Amenity` objects are then collected into a list using the
   * `Collectors.toList()` method. Finally, the list of created `Amenity` objects is
   * saved to the database using the `amenityRepository.saveAll()` method, and the
   * resulting list of `AmenityDto` objects is returned in an `Optional`.
   * 
   * @param communityId unique identifier of a community that the amenities will be
   * associated with.
   * 
   * The `communityId` parameter is a String representing the unique identifier of a community.
   * 
   * @returns an optional list of amenity DTOs representing created amenities.
   * 
   * 	- The Optional<List<AmenityDto>> return value represents an optional list of
   * amenities that have been created in the system. If no amenities were created
   * successfully, the list will be empty.
   * 	- The List<Amenity> contained within the Optional is a list of amenities that
   * have been mapped from their corresponding DTOs using the `amenityApiMapper`. Each
   * amenity has a community associated with it, which is obtained from the `Community`
   * object retrieved from the service.
   * 	- The List<AmenityDto> contained within the Optional is a list of DTOs representing
   * the created amenities. Each DTO contains the same attributes as the original amenity
   * DTO passed in the function, including the id, name, and community ID.
   * 	- The `saveAll` method used to save the created amenities returns a stream of
   * `Amenity` objects that have been persisted in the database. These objects are then
   * mapped back to their corresponding DTOs using the `amenityApiMapper`.
   */
  @Override
  public Optional<List<AmenityDto>> createAmenities(Set<AmenityDto> amenities, String communityId) {
    final Optional<Community> community = communityService.getCommunityDetailsById(communityId);
    if (!community.isPresent()) {
      return Optional.empty();
    }
    final List<Amenity> amenitiesWithCommunity = amenities.stream()
        .map(amenityApiMapper::amenityDtoToAmenity)
        .map(amenity -> {
          amenity.setCommunity(community.get());
          return amenity;
        })
        .collect(Collectors.toList());
    final List<AmenityDto> createdAmenities =
        amenityRepository.saveAll(amenitiesWithCommunity).stream()
            .map(amenityApiMapper::amenityToAmenityDto)
            .collect(Collectors.toList());
    return Optional.of(createdAmenities);
  }

  /**
   * retrieves the details of an amenity based on its ID, by querying the amenity
   * repository using the `findByAmenityId` method.
   * 
   * @param amenityId identifier of an amenity that is to be retrieved from the repository.
   * 
   * 	- `amenityId`: The unique identifier for an amenity, which is retrieved from the
   * repository using the `findByAmenityId` method.
   * 
   * @returns an Optional object containing the details of the amenity with the provided
   * ID.
   * 
   * 	- `Optional<Amenity>`: The output is an optional object of type `Amenity`, which
   * means that if no amenity details exist for the provided `amenityId`, the method
   * will return an empty `Optional`.
   * 	- `amenityRepository.findByAmenityId(amenityId)`: This method call returns a
   * single `Amenity` object based on the specified `amenityId`. The `amenityRepository`
   * is likely a data access layer or a database connection, and the `findByAmenityId`
   * method performs a query to retrieve the amenity details for the given `amenityId`.
   */
  @Override
  public Optional<Amenity> getAmenityDetails(String amenityId) {
    return amenityRepository.findByAmenityId(amenityId);
  }

  /**
   * deletes an amenity from the database by finding it using its ID, removing it from
   * the community's amenities list, and then deleting it.
   * 
   * @param amenityId ID of an amenity that needs to be deleted.
   * 
   * 	- `amenityId`: A unique identifier for an amenity in a community.
   * 
   * The function retrieves the amenity from the repository using the `findByAmenityIdWithCommunity`
   * method and then performs the following operations:
   * 
   * 	- Removes the amenity from the community's list of amenities.
   * 	- Deletes the amenity from the repository.
   * 
   * The return value is `true` if the amenity was successfully deleted, or `false` otherwise.
   * 
   * @returns a boolean value indicating whether the amenity was successfully deleted.
   */
  @Override
  public boolean deleteAmenity(String amenityId) {
    return amenityRepository.findByAmenityIdWithCommunity(amenityId)
        .map(amenity -> {
          Community community = amenity.getCommunity();
          community.getAmenities().remove(amenity);
          amenityRepository.delete(amenity);
          return true;
        })
        .orElse(false);
  }

  /**
   * retrieves a community's amenities by querying the community repository and mapping
   * the resulting Community objects to their respective amenity sets using the
   * `getAmenities()` method.
   * 
   * @param communityId identifier of the community whose amenities are to be listed.
   * 
   * 	- `communityRepository`: This is an instance of `CommunityRepository`, which is
   * responsible for managing community data.
   * 	- `findByCommunityIdWithAmenities`: This method returns a `List` of `Community`
   * objects that match the given `communityId`. It also includes the amenities associated
   * with each community.
   * 	- `map`: This method applies a transformation to the returned list, in this case
   * mapping each `Community` object to its associated amenities using the `getAmenities()`
   * method.
   * 	- `orElse`: This method returns an alternative value if the original method call
   * returns `null`. In this case, it returns an empty `HashSet` of amenities if the
   * method call returns `null`.
   * 
   * @returns a set of amenities associated with a specific community.
   * 
   * 	- The function returns a `Set<Amenity>` data structure, indicating that it is a
   * collection of amenities associated with a particular community.
   * 	- The `CommunityRepository` class is used to fetch the community information along
   * with its amenities, using the `findByCommunityIdWithAmenities` method.
   * 	- The `map` method is applied to the result of the previous step, which transforms
   * the `Community` objects into `Amenity` objects. This allows for the creation of a
   * single collection of amenities that can be used by the application.
   * 	- If no community information is found, the function returns an empty `Set<Amenity>`,
   * indicating that there are no amenities associated with the given community ID.
   */
  @Override
  public Set<Amenity> listAllAmenities(String communityId) {
    return communityRepository.findByCommunityIdWithAmenities(communityId)
        .map(Community::getAmenities)
        .orElse(new HashSet<>());
  }

  /**
   * updates an amenity in the database by retrieving the existing amenity with the
   * matching amenity ID, updating its name, price, and description, and then saving
   * the updated amenity or returning null if failed.
   * 
   * @param updatedAmenity updated amenity object containing the modified values for
   * name, price, id, description, and community Id.
   * 
   * 	- `amenityId`: The ID of the amenity being updated.
   * 	- `communityId`: The ID of the community to which the amenity belongs.
   * 	- `name`: The name of the amenity.
   * 	- `price`: The price of the amenity.
   * 	- `description`: A description of the amenity.
   * 
   * @returns a boolean value indicating whether the amenity was updated successfully.
   * 
   * 	- `map(amenity -> communityRepository.findByCommunityId(updatedAmenity.getCommunityId())`:
   * This method retrieves the `Community` object associated with the given `Amenity`
   * object's `CommunityId`.
   * 	- `map(community -> { ... }):` This method performs an operation on the retrieved
   * `Community` object, which is then returned as a new `Amenity` object.
   * 	- `orElse(null)`: If no `Community` object is found, the function returns `null`.
   * 	- `map(amenityRepository::save):` This method saves the updated `Amenity` object
   * in the repository.
   * 
   * The output of the `updateAmenity` function is a `Optional` object containing the
   * updated `Amenity` object or `null`, depending on whether a `Community` object was
   * found and saved successfully.
   */
  @Override
  public boolean updateAmenity(AmenityDto updatedAmenity) {
    String amenityId = updatedAmenity.getAmenityId();
    return amenityRepository.findByAmenityId(amenityId)
        .map(amenity -> communityRepository.findByCommunityId(updatedAmenity.getCommunityId())
            .map(community -> {
              Amenity updated = new Amenity();
              updated.setName(updatedAmenity.getName());
              updated.setPrice(updatedAmenity.getPrice());
              updated.setId(amenity.getId());
              updated.setAmenityId(amenityId);
              updated.setDescription(updatedAmenity.getDescription());
              return updated;
            })
            .orElse(null))
        .map(amenityRepository::save).isPresent();
  }
}
