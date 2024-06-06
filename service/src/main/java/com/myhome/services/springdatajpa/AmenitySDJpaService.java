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
 * retrieves, creates, updates, and deletes amenities in a database using JPA and
 * Hibernate. It provides methods for listing all amenities associated with a specific
 * community, creating new amenities, getting an individual amenity's details, deleting
 * an amenity, and updating an amenity's details.
 */
@Service
@RequiredArgsConstructor
public class AmenitySDJpaService implements AmenityService {

  private final AmenityRepository amenityRepository;
  private final CommunityRepository communityRepository;
  private final CommunityService communityService;
  private final AmenityApiMapper amenityApiMapper;

  /**
   * creates a list of amenities for a community by mapping amenity DTOs to amenities
   * and saving them to the database. It returns an optional list of created amenities.
   * 
   * @param amenities set of amenities that need to be created or updated in the system.
   * 
   * * `Set<AmenityDto>` represents a set of amenities that are to be created in the community.
   * * `communityId` is a string representing the ID of the community where the amenities
   * will be created.
   * * `Community` is an optional object that contains information about the community
   * where the amenities will be created. If the community does not exist, this field
   * will be absent.
   * * `AmenityDto` represents a single amenity to be created in the community. It has
   * several attributes:
   * 	+ `id`: an ID of the amenity.
   * 	+ `name`: the name of the amenity.
   * 	+ `type`: the type of the amenity (e.g., "park", "library", etc.).
   * 	+ `description`: a brief description of the amenity.
   * 	+ `latitude`: the latitude coordinate of the amenity.
   * 	+ `longitude`: the longitude coordinate of the amenity.
   * 	+ `address`: the address of the amenity.
   * 
   * @param communityId ID of a specific community that the amenities will be associated
   * with.
   * 
   * @returns a list of `AmenityDto` objects representing newly created amenities.
   * 
   * * The Optional object contains a list of AmenityDto objects, representing the newly
   * created amenities in the database.
   * * The list is not empty by default, as the function will always return at least
   * one element (the created amenities).
   * * Each element in the list is an AmenityDto object, which has been converted from
   * the original amenity entity using the `amenityApiMapper`.
   * * The `Community` object associated with each amenity is obtained from the database
   * using the `communityService`, and is stored as part of the amenity entity.
   * * The list of created amenities can be used for further processing or storage,
   * depending on the context of the function call.
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
   * retrieves an optional instance of `Amenity` based on the provided `amenityId`. It
   * makes a call to the `amenityRepository` to find the details of the amenity using
   * its `amenityId`.
   * 
   * @param amenityId identifier of an amenity for which details are being requested.
   * 
   * @returns an Optional containing the details of the amenity with the provided ID.
   * 
   * * The `Optional` class represents a container for holding a value that may be
   * present or absent.
   * * The `findByAmenityId` method in the `amenityRepository` returns an optional
   * object containing details of the amenity with the provided `amenityId`.
   * * If no amenity is found with the given `amenityId`, the returned `Optional` will
   * be `empty()`.
   */
  @Override
  public Optional<Amenity> getAmenityDetails(String amenityId) {
    return amenityRepository.findByAmenityId(amenityId);
  }

  /**
   * deletes an amenity from a community by first finding the amenity to be deleted,
   * then removing it from the community's amenities list and finally deleting it from
   * the repository.
   * 
   * @param amenityId id of an amenity to be deleted.
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
   * retrieves a set of amenities for a given community by querying the community
   * repository and mapping the community's amenities to a set.
   * 
   * @param communityId ID of the community whose amenities should be listed.
   * 
   * @returns a set of amenity objects associated with a specific community.
   * 
   * The output is a `Set` of `Amenity` objects, which represent all the amenities
   * associated with a particular community.
   * 
   * The `Set` is generated by combining the results of two queries: first, finding all
   * communities with a specific `communityId`, and second, mapping each found community
   * to its associated amenities using the `getAmenities()` method.
   * 
   * If no amenities are associated with a particular community, the output will be an
   * empty `Set`.
   * 
   * Overall, the `listAllAmenities` function provides a convenient way to access all
   * the amenities associated with a given community, without having to perform multiple
   * queries or manually constructing the result set.
   */
  @Override
  public Set<Amenity> listAllAmenities(String communityId) {
    return communityRepository.findByCommunityIdWithAmenities(communityId)
        .map(Community::getAmenities)
        .orElse(new HashSet<>());
  }

  /**
   * updates an amenity in the database by finding the corresponding amenity record,
   * updating its fields, and saving it to the database if successful.
   * 
   * @param updatedAmenity updated amenity object containing the new name, price, and
   * other attributes of the amenity to be saved in the database.
   * 
   * * `amenityId`: The ID of the amenity being updated.
   * * `communityId`: The ID of the community associated with the amenity.
   * * `name`: The name of the amenity.
   * * `price`: The price of the amenity.
   * * `description`: A description of the amenity.
   * 
   * The function first retrieves the existing amenity with the same `amenityId` using
   * `amenityRepository.findByAmenityId(amenityId)`. If such an amenity is found, it
   * then retrieves the community associated with the amenity using
   * `communityRepository.findByCommunityId(updatedAmenity.getCommunityId())`. Finally,
   * it updates the amenity with the new values for `name`, `price`, and `description`
   * and saves it to the repository using `amenityRepository.save()`.
   * 
   * @returns a boolean value indicating whether the amenity was updated successfully
   * or not.
   * 
   * * `map(amenity -> communityRepository.findByCommunityId(updatedAmenity.getCommunityId())`:
   * This step retrieves the community associated with the updated amenity.
   * * `map(community -> { ... })`: This step updates the amenity object with the name,
   * price, id, amenity id, and description from the input `updatedAmenity`.
   * * `orElse(null)`: This step returns the updated amenity object if the community
   * associated with the updated amenity exists, or returns `null` otherwise.
   * * `map(amenityRepository::save)`: This step saves the updated amenity object to
   * the repository.
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
