package com.myhome.utils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 
 */
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
   * @param pageable pagination information for the current page being processed,
   * including the page number, size, total pages, and total elements.
   * 
   * @param page current page being processed, providing the total number of elements
   * on that page and the total number of pages available for display.
   * 
   * 	- `pageNumber`: The number of the current page being processed.
   * 	- `pageSize`: The size of each page being processed.
   * 	- `totalPages`: The total number of pages available for processing in the input
   * dataset.
   * 	- `totalElements`: The total number of elements present in the input dataset
   * across all pages.
   * 
   * @returns a `PageInfo` object containing page-related metadata.
   * 
   * 	- `pageNumber`: The number of the current page being displayed.
   * 	- `pageSize`: The number of elements on each page.
   * 	- `totalPages`: The total number of pages in the dataset.
   * 	- `totalElements`: The total number of elements in the dataset.
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
