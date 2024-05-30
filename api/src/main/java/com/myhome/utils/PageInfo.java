package com.myhome.utils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * is a data structure that contains information about a page of results in a larger
 * dataset. It has four fields: currentPage, pageLimit, totalPages, and totalElements.
 * The class provides a constructor for creating instances of the class from a Pageable
 * object and a Page object.
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
   * size of each page, total number of pages and elements in the page.
   * 
   * @param pageable pagination state of the application, providing the current page
   * number and total pages available for navigation.
   * 
   * 	- `getPageNumber()`: The number of the current page being displayed.
   * 	- `getPageSize()`: The number of elements in a single page of the result set.
   * 	- `getTotalPages()`: The total number of pages in the result set.
   * 	- `getTotalElements()`: The total number of elements returned by the query.
   * 
   * @param page current page being processed, providing information on its position
   * and size within the overall paginated result.
   * 
   * 	- `getPageNumber():` The page number of the result set.
   * 	- `getPageSize():` The number of elements in each page of the result set.
   * 	- `getTotalPages():` The total number of pages in the result set.
   * 	- `getTotalElements():` The total number of elements in the result set.
   * 
   * @returns a `PageInfo` object containing information about the current page of results.
   * 
   * 	- pageable.getPageNumber(): The number of the current page being displayed.
   * 	- pageable.getPageSize(): The number of elements in each page.
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
