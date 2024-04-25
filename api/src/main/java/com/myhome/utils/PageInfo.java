package com.myhome.utils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@EqualsAndHashCode
@ToString
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PageInfo {
  private final int currentPage;
  private final int pageLimit;
  private final int totalPages;
  private final long totalElements;

  /**
   * takes a `pageable` parameter and a `page` parameter, returning a `PageInfo` object
   * containing information about the page and its elements.
   * 
   * @param pageable Pageable object containing information about the pagination of the
   * results, which is used to determine the page number, size, total pages, and total
   * elements of the resulting PageInfo object.
   * 
   * @param page current page being processed, providing its total number of elements
   * and pages.
   * 
   * @returns a `PageInfo` object containing page-related metadata.
   */
  public static PageInfo of(Pageable pageable, Page<?> page) {
    return new PageInfo(
        pageable.getPageNumber(),
        pageable.getPageSize(),
        page.getTotalPages(),
        page.getTotalElements()
    );
  }
}
