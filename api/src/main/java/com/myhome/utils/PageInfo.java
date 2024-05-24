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
 * an instance of the class by passing in the Pageable and Page objects, and returns
 * a PageInfo object containing the relevant information.
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
   * creates a `PageInfo` object containing information about the number of pages, page
   * size, total pages, and total elements for a given `Pageable` and `Page`.
   * 
   * @param pageable pageable object that contains information about the current page
   * being processed, such as the page number and size.
   * 
   * 	- `getPageNumber()`: Returns the page number of the current page being processed.
   * 	- `getPageSize()`: Returns the size of a single page of elements in the collection.
   * 	- `getTotalPages()`: Returns the total number of pages in the collection.
   * 	- `getTotalElements()`: Returns the total number of elements in the collection.
   * 
   * @param page current page being processed, providing the total number of elements
   * on that page and the total number of pages in the result set.
   * 
   * 	- `getPageNumber()`: The page number of the input.
   * 	- `getPageSize()`: The size of each page in the input.
   * 	- `getTotalPages()`: The total number of pages in the input.
   * 	- `getTotalElements()`: The total number of elements in the input.
   * 
   * @returns a `PageInfo` object containing various pagination-related information.
   * 
   * 	- The first item in the list is the page number (0-based) of the page being processed.
   * 	- The second item is the size of the page (i.e., the total number of elements).
   * 	- The third item is the total number of pages available.
   * 	- The fourth item is the total number of elements in the entire collection.
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
