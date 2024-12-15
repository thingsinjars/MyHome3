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

package com.myhome.controllers.dto.mapper;

import com.myhome.domain.HouseMember;
import com.myhome.model.HouseMemberDto;
import java.util.Set;
import org.mapstruct.Mapper;

/**
 * maps between House Member objects and their corresponding REST API response versions
 * using a set-based mapping approach.
 */
@Mapper
public interface HouseMemberMapper {
  Set<com.myhome.model.HouseMember> houseMemberSetToRestApiResponseHouseMemberSet(
      Set<HouseMember> houseMemberSet);

  Set<HouseMember> houseMemberDtoSetToHouseMemberSet(Set<HouseMemberDto> houseMemberDtoSet);

  Set<com.myhome.model.HouseMember> houseMemberSetToRestApiResponseAddHouseMemberSet(
      Set<HouseMember> houseMemberSet);
}
