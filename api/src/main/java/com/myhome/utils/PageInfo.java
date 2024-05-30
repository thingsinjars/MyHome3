package com.myhome.utils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * is used to generate information about a page of data containing page number, size,
 * total pages, and total elements from a given Pageable object and Page object. The
 * class provides methods for creating instances of the class and generates a PageInfo
 * object with necessary information.
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
   * creates a `PageInfo` object from a `Pageable` and a `Page`. It provides information
   * about the number of pages, page size, total pages, and total elements in the page.
   * 
   * @param pageable pagination information for the specified page of data.
   * 
   * 	- `getPageNumber()`: Gets the page number associated with the provided pageable
   * object.
   * 	- `getPageSize()`: Gets the number of elements in a page associated with the
   * provided pageable object.
   * 	- `getTotalPages()`: Returns the total number of pages associated with the provided
   * pageable object.
   * 	- `getTotalElements()`: Returns the total number of elements associated with the
   * provided pageable object.
   * 
   * @param page current page of data being processed, providing the total number of
   * elements on that page.
   * 
   * 	- The `getPageNumber()` method returns the page number of the input pageable.
   * 	- The `getPageSize()` method returns the size of a page in the input pageable.
   * 	- The `getTotalPages()` method returns the total number of pages in the input pageable.
   * 	- The `getTotalElements()` method returns the total number of elements in the
   * input page.
   * 
   * @returns a `PageInfo` object containing page number, size, total pages, and total
   * elements.
   * 
   * 	- The first element is `pageNumber`, which represents the page number of the
   * current page being processed.
   * 	- The second element is `pageSize`, which signifies the number of elements in a
   * single page of the input.
   * 	- The third element is `totalPages`, which indicates the total number of pages
   * in the input.
   * 	- The fourth and final element is `totalElements`, which represents the total
   * number of elements in the entire input.
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
