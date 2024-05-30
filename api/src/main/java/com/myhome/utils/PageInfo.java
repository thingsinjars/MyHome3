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
 * The class also provides constructors for creating instances of the class from a
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
   * creates a `PageInfo` object containing information about the number of pages, size
   * of each page, total number of pages, and total elements for a given pageable and
   * page.
   * 
   * @param pageable pageable object that contains information about the pagination of
   * the data, such as the current page number and the number of elements per page.
   * 
   * 	- The `getPageNumber()` method returns the page number of the current page being
   * processed.
   * 	- The `getPageSize()` method returns the size of each page in the result set.
   * 	- The `getTotalPages()` method returns the total number of pages in the result set.
   * 	- The `getTotalElements()` method returns the total number of elements returned
   * by the query.
   * 
   * @param page current page being processed, providing information on its position
   * and size within the overall set of pages.
   * 
   * 	- `pageNumber`: The page number of the input pageable.
   * 	- `pageSize`: The size of each page in the input pageable.
   * 	- `totalPages`: The total number of pages in the input pageable.
   * 	- `totalElements`: The total number of elements in the input pageable.
   * 
   * @returns a `PageInfo` object containing page number, size, total pages, and total
   * elements.
   * 
   * 	- The `pageNumber` property represents the page number of the paginated result.
   * 	- The `pageSize` property represents the number of elements in a single page of
   * the paginated result.
   * 	- The `totalPages` property represents the total number of pages in the paginated
   * result.
   * 	- The `totalElements` property represents the total number of elements in the
   * paginated result.
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
