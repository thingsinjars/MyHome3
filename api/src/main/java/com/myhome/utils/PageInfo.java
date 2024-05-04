package com.myhome.utils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * from the file is a data structure that contains information about a page of results
 * in a larger dataset. It has four fields: currentPage, pageLimit, totalPages, and
 * totalElements. The class also provides a constructor for creating instances of the
 * class from a Pageable object and a Page object.
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
   * creates a `PageInfo` object containing information about the number of pages, size
   * of each page, total number of pages, and total elements in a pageable.
   * 
   * @param pageable page request, providing the page number and size for the page of
   * data to be processed and returned.
   * 
   * 	- `getPageNumber()`: The number of the current page being displayed.
   * 	- `getPageSize()`: The number of elements on each page.
   * 	- `getTotalPages()`: The total number of pages in the result set.
   * 	- `getTotalElements()`: The total number of elements returned by the query.
   * 
   * @param page current page being processed, providing information on its position
   * and size within the overall paginated result.
   * 
   * 	- The `pageNumber` property is an integer representing the page number.
   * 	- The `pageSize` property is also an integer, indicating the number of elements
   * in a single page.
   * 	- The `totalPages` property is an integer representing the total number of pages
   * available for the collection.
   * 	- The `totalElements` property is an integer representing the total number of
   * elements in the collection.
   * 
   * @returns a `PageInfo` object containing various pagination-related information.
   * 
   * 	- pageable.getPageNumber(): The number of the current page being displayed.
   * 	- pageable.getPageSize(): The size of each page being displayed.
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
