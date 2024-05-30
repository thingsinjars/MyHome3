package com.myhome.utils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * is a data structure that provides information about the number of pages, page size,
 * total pages, and total elements of a given Pageable and Page object. It generates
 * a PageInfo object containing these values through a constructor or by calling the
 * `of()` method with the necessary parameters.
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
   * generates a `PageInfo` object containing information about the number of pages,
   * size of each page, total number of pages and elements in a Pageable and Page instances.
   * 
   * @param pageable Pageable object that provides the paging information for the current
   * page of results.
   * 
   * 	- `getPageNumber()`: returns the page number of the current page being processed
   * 	- `getPageSize()`: returns the number of elements in a single page
   * 	- `getTotalPages()`: returns the total number of pages available for processing
   * 	- `getTotalElements()`: returns the total number of elements available for processing.
   * 
   * @param page current page of data being processed, providing the total number of
   * elements on that page.
   * 
   * 	- `pageNumber`: The page number of the result.
   * 	- `pageSize`: The size of each page in the result.
   * 	- `totalPages`: The total number of pages in the result.
   * 	- `totalElements`: The total number of elements in the result.
   * 
   * @returns a `PageInfo` object containing page-related metadata.
   * 
   * 	- pageable.getPageNumber(): The zero-based index of the current page being displayed.
   * 	- pageable.getPageSize(): The number of elements in a page.
   * 	- page.getTotalPages(): The total number of pages in the result set.
   * 	- page.getTotalElements(): The total number of elements in the result set.
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
