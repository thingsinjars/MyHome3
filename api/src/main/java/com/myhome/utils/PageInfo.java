package com.myhome.utils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * TODO
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
   * creates a `PageInfo` object containing information about a given pageable and page,
   * such as page number, size, total pages, and total elements.
   * 
   * @param pageable pagination information of the data being processed, providing the
   * number of pages and the size of each page.
   * 
   * 	- `getPageNumber()`: Returns the page number of the current page being processed.
   * 	- `getPageSize()`: Returns the number of elements in a single page.
   * 	- `getTotalPages()`: Returns the total number of pages available for processing.
   * 	- `getTotalElements()`: Returns the total number of elements that can be processed
   * across all pages.
   * 
   * @param page current page being processed, providing the total number of elements
   * on that page and the total number of pages in the overall result set.
   * 
   * 	- `getPageNumber()` returns the page number that contains the element(s) being queried.
   * 	- `getPageSize()` returns the number of elements per page in the query result.
   * 	- `getTotalPages()` returns the total number of pages in the result set.
   * 	- `getTotalElements()` returns the total number of elements in the result set.
   * 
   * @returns a `PageInfo` object containing information about the current page of a
   * paginated result set.
   * 
   * 	- pageable.getPageNumber(): The number of the current page being displayed.
   * 	- pageable.getPageSize(): The number of elements displayed on each page.
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
