package com.myhome.utils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * is a data structure that represents a page of elements with pagination capabilities.
 * It contains four fields: currentPage, pageLimit, totalPages, and totalElements.
 * The class also provides a method for creating a new instance of the class based
 * on a pageable object and a page of elements.
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
   * transforms a `pageable` and a `page` into a `PageInfo` object, providing information
   * about the page number, size, total pages, and total elements.
   * 
   * @param pageable pageable object that contains information about the current page
   * being processed, including the page number and size.
   * 
   * 	- `getPageNumber()`: The page number that this pageable represents.
   * 	- `getPageSize()`: The number of elements in a single page of the sequence.
   * 	- `getTotalPages()`: The total number of pages in the entire sequence.
   * 	- `getTotalElements()`: The total number of elements in the sequence.
   * 
   * @param page current page of data being processed, providing the page number, size,
   * total pages, and total elements for the PageInfo object returned by the function.
   * 
   * 	- `pageNumber`: The number of the page being queried.
   * 	- `pageSize`: The number of elements in each page.
   * 	- `totalPages`: The total number of pages available for querying.
   * 	- `totalElements`: The total number of elements returned by the query.
   * 
   * @returns a `PageInfo` object containing page number, size, total pages, and total
   * elements.
   * 
   * 	- `pageNumber`: The number of the current page being displayed.
   * 	- `pageSize`: The number of elements in each page of the result set.
   * 	- `totalPages`: The total number of pages in the result set.
   * 	- `totalElements`: The total number of elements in the result set.
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
