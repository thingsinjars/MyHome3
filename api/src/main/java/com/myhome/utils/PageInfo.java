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
 * a PageInfo object containing page number, size, total pages, and total elements
 * through its constructor, which takes in a Pageable and Page objects as parameters.
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
   * creates a `PageInfo` object containing information about the specified pageable
   * and page, including the page number, size, total pages, and total elements.
   * 
   * @param pageable pageable object that contains information about the current page
   * being processed, such as the page number and page size.
   * 
   * 	- The `getPageNumber()` method returns the page number of the current page being
   * processed.
   * 	- The `getPageSize()` method returns the number of elements in a page.
   * 	- The `getTotalPages()` method returns the total number of pages in the entire collection.
   * 	- The `getTotalElements()` method returns the total number of elements in the
   * entire collection.
   * 
   * @param page current page of data being processed, providing the total number of
   * elements on that page.
   * 
   * 	- The first argument `pageable` represents the pageable object containing information
   * about the current page being accessed.
   * 	- The second argument `page` represents the actual page of data being processed.
   * 	- The `getPageNumber()` method returns the current page number being accessed,
   * which is a positive integer value.
   * 	- The `getPageSize()` method returns the size of each page of data, which is also
   * a positive integer value.
   * 	- The `getTotalPages()` method returns the total number of pages available in the
   * dataset, which is also a positive integer value.
   * 	- The `getTotalElements()` method returns the total number of elements in the
   * entire dataset, which can be either an integer or a long value depending on the
   * specific type of pageable object being used.
   * 
   * @returns a `PageInfo` object containing pagination information.
   * 
   * 1/ pageNumber: The number of the current page being displayed.
   * 2/ pageSize: The number of elements in a single page.
   * 3/ totalPages: The total number of pages available in the dataset.
   * 4/ totalElements: The total number of elements in the entire dataset.
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
