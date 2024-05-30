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
 * The class also provides a constructor for creating instances of the class from a
 * Pageable object and a Page object.
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
   * takes a `pageable` and a `page` parameter and returns a `PageInfo` object containing
   * information about the page number, size, total pages, and total elements.
   * 
   * @param pageable pageable object that contains information about the current page
   * of data being processed, including the page number and size.
   * 
   * 	- `getPageNumber()`: The page number of the current page being processed.
   * 	- `getPageSize()`: The number of elements in a single page of the result set.
   * 	- `getTotalPages()`: The total number of pages in the result set.
   * 	- `getTotalElements()`: The total number of elements in the result set.
   * 
   * @param page current page being processed, providing information on its position
   * and size within the overall paginated result set.
   * 
   * 	- `pageNumber`: The page number of the result set.
   * 	- `pageSize`: The number of elements in each page of the result set.
   * 	- `totalPages`: The total number of pages in the result set.
   * 	- `totalElements`: The total number of elements in the result set.
   * 
   * @returns a `PageInfo` object containing page number, size, total pages, and total
   * elements.
   * 
   * 	- The first parameter is `pageable`, which represents the pageable object that
   * contains information about the current page being processed.
   * 	- The second parameter is `page`, which represents the actual page being processed.
   * 	- The `PageInfo` class returns an object containing four properties:
   * 	+ `pageNumber`: The number of the current page being processed.
   * 	+ `pageSize`: The size of the current page being processed.
   * 	+ `totalPages`: The total number of pages in the result set.
   * 	+ `totalElements`: The total number of elements in the result set.
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
