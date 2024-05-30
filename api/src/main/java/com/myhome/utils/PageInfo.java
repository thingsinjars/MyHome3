package com.myhome.utils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * is a data structure that provides information about a page of results, including
 * the current page number, page size, total pages, and total elements. The class
 * provides a constructor for creating instances from a Pageable and a Page object,
 * as well as a method for generating a new PageInfo object based on the current page
 * and total pages.
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
   * generates a `PageInfo` object representing the number of pages, page size, total
   * pages, and total elements for a given `Pageable` and `Page`.
   * 
   * @param pageable Pageable object that contains information about the current page
   * of data being processed, including the page number and size.
   * 
   * 	- `getPageNumber()`: returns the page number of the current page being processed.
   * 	- `getPageSize()`: returns the number of elements in a single page of the data.
   * 	- `getTotalPages()`: returns the total number of pages in the entire dataset.
   * 	- `getTotalElements()`: returns the total number of elements in the entire dataset.
   * 
   * @param page current page being processed, providing the total number of elements
   * on that page and the total number of pages available for the current query.
   * 
   * 	- `pageNumber`: The page number that contains the element being accessed.
   * 	- `pageSize`: The size of each page returned by the pagination.
   * 	- `totalPages`: The total number of pages in the result set.
   * 	- `totalElements`: The total number of elements returned by the pagination.
   * 
   * @returns a `PageInfo` object containing information about the current page and
   * total pages and elements.
   * 
   * 	- The first argument `pageable` is an instance of `Pageable`, which represents a
   * pagination-capable source of data.
   * 	- The second argument `page` is an instance of `Page`, which contains information
   * about the current page being processed, including the total number of pages and
   * elements in the collection.
   * 	- The output is a `PageInfo` object, which encapsulates information about the
   * pagination state of the data source. This includes the current page number, page
   * size, total number of pages, and total number of elements in the collection.
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
