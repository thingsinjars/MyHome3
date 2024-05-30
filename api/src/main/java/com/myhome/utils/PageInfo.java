package com.myhome.utils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * provides information about the number of pages, page size, total pages, and total
 * elements of a given Pageable and Page object through its constructors and getters.
 * The class offers a convenient way to create instances of PageInfo from either a
 * Pageable or a Page object, making it useful for pagination-related applications.
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
   * generates a `PageInfo` object containing information about the current page and
   * total pages/elements for a given `Pageable` and `Page`.
   * 
   * @param pageable pagination information for the page being processed, providing the
   * page number, page size, total pages, and total elements.
   * 
   * 	- The first parameter is an instance of `Pageable`, which contains information
   * about the current page of data being processed.
   * 	- The second parameter is an instance of a specific type of page, such as a list
   * or a set.
   * 
   * The function then returns a new instance of `PageInfo`, which contains information
   * about the current page of data, including the page number, page size, total pages,
   * and total elements.
   * 
   * @param page current page being processed, providing the total number of elements
   * on that page.
   * 
   * 	- `pageNumber`: The page number of the result, as returned by the pagination mechanism.
   * 	- `pageSize`: The size of each page of results, which can be used to calculate
   * the total number of elements in the result set.
   * 	- `totalPages`: The total number of pages available for the given input, based
   * on the pagination mechanism's capabilities and constraints.
   * 	- `totalElements`: The total number of elements returned in the entire result
   * set, including all pages combined.
   * 
   * @returns a `PageInfo` object containing information about the page number, size,
   * total pages, and total elements of a Pageable.
   * 
   * 	- The first parameter is the `pageable` object, which contains information about
   * the pagination of the page being queried.
   * 	- The second parameter is the `page` object, which represents a single page of
   * elements from the data source.
   * 	- The `pageNumber` property of the output represents the number of the current
   * page being queried.
   * 	- The `pageSize` property represents the number of elements in a page.
   * 	- The `totalPages` property represents the total number of pages in the data source.
   * 	- The `totalElements` property represents the total number of elements in the
   * data source.
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
