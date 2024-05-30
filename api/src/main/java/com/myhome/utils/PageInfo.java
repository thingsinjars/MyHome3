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
 * a `PageInfo` object containing these values through a constructor that takes in
 * the Pageable and Page arguments.
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
   * takes a `pageable` and a `page` as input, and returns a `PageInfo` object containing
   * information about the page of data.
   * 
   * @param pageable pagination information for the given page of data.
   * 
   * 	- `pageable.getPageNumber()`: The page number that the input is associated with.
   * 	- `pageable.getPageSize()`: The number of elements in a single page of the input.
   * 	- `page.getTotalPages()`: The total number of pages in the input.
   * 	- `page.getTotalElements()`: The total number of elements in the input.
   * 
   * @param page current page being processed, providing the total number of elements
   * on that page.
   * 
   * 	- `pageNumber`: The page number that this PageInfo represents.
   * 	- `pageSize`: The number of elements per page that this PageInfo represents.
   * 	- `totalPages`: The total number of pages in the result set that this PageInfo represents.
   * 	- `totalElements`: The total number of elements returned by the query that this
   * PageInfo represents.
   * 
   * @returns a `PageInfo` object containing various page-related metrics.
   * 
   * 	- The page number is provided as the first attribute of the PageInfo object
   * (pageable.getPageNumber()). This value represents the current page being processed.
   * 	- The page size is represented by the second feature of the PageInfo object
   * (pageable.getPageSize()). This figure indicates how many objects there are on each
   * page.
   * 	- The total number of pages is given as the third element of the PageInfo object
   * (page.getTotalPages()). This value represents the total number of pages in a result
   * set.
   * 	- The overall number of things is represented by the fourth feature of the PageInfo
   * object (page.getTotalElements()). This figure shows the complete amount of data
   * present across all pages.
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
