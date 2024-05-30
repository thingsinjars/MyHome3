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
 * total pages, and total elements of a given Pageable and Page object. The class
 * offers a constructor for creating instances of the class from a Pageable and a
 * Page object, as well as a method for generating a PageInfo object based on the
 * current page being processed and the total number of pages and elements available.
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
   * size of each page, total number of pages and elements for a given `Pageable` and
   * `Page`.
   * 
   * @param pageable Pageable object that contains information about the pagination of
   * the data being processed, which is used to compute the page number, size, and total
   * pages and elements.
   * 
   * 	- `getPageNumber()` returns the page number of the current page being processed.
   * 	- `getPageSize()` returns the number of elements in each page.
   * 	- `getTotalPages()` returns the total number of pages in the paginated result.
   * 	- `getTotalElements()` returns the total number of elements in the entire paginated
   * result.
   * 
   * @param page current page being processed, providing information on its position
   * and size within the overall dataset.
   * 
   * 	- `pageNumber`: The page number that corresponds to the current page being rendered.
   * 	- `pageSize`: The number of elements in each page.
   * 	- `totalPages`: The total number of pages in the result set.
   * 	- `totalElements`: The total number of elements returned by the query.
   * 
   * @returns a `PageInfo` object containing page-related information.
   * 
   * 	- pageNumber: The number of the current page being displayed.
   * 	- pageSize: The size of each page being displayed.
   * 	- totalPages: The total number of pages in the result set.
   * 	- totalElements: The total number of elements in the result set.
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
