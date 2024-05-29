package com.myhome.utils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * provides a data structure for storing information about the number of pages, page
 * size, total pages, and total elements in a dataset. The class offers a constructor
 * for creating instances from a Pageable and a Page object, as well as methods for
 * generating a `PageInfo` object based on a Pageable and a Page.
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
   * generates a `PageInfo` object representing the number, size, total pages, and total
   * elements of a paginated result based on a provided `Pageable` and `Page`.
   * 
   * @param pageable page request parameters, such as the number of pages to retrieve
   * and the page size.
   * 
   * 	- The `getPageNumber()` method returns the page number associated with the input
   * `pageable`.
   * 	- The `getPageSize()` method returns the number of items returned in a single
   * page of the input `pageable`.
   * 	- The `getTotalPages()` method returns the total number of pages associated with
   * the input `pageable`.
   * 	- The `getTotalElements()` method returns the total number of elements returned
   * by the input `pageable`.
   * 
   * @param page current page being processed, providing the total number of elements
   * on that page.
   * 
   * 	- `pageNumber`: The page number that corresponds to the current page being rendered.
   * 	- `pageSize`: The size of the page, which is the number of elements that can be
   * displayed on a single page.
   * 	- `totalPages`: The total number of pages available in the paginated dataset.
   * 	- `totalElements`: The total number of elements in the paginated dataset.
   * 
   * @returns a `PageInfo` object containing information about the page number, size,
   * total pages, and total elements of a given pageable and page.
   * 
   * 	- The first parameter, `pageable`, represents the pageable object that contains
   * information about the current page being processed.
   * 	- The second parameter, `page`, represents the page object that contains information
   * about the specific page being processed.
   * 	- The return value is a `PageInfo` object, which contains four properties:
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
