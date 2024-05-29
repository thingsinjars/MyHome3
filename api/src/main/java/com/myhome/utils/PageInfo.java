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
 * provides a constructor for creating instances of the class from a Pageable and a
 * Page object, and also includes methods for generating a `PageInfo` object containing
 * information about the current page being processed, including the page number and
 * size, as well as the total number of pages and elements in the input.
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
   * generates a `PageInfo` object that contains information about the number of pages,
   * size of each page, total number of pages and total elements of a given pageable
   * and page.
   * 
   * @param pageable page request, providing information about the number of pages and
   * elements per page.
   * 
   * 	- The `pageNumber` property is the current page number being processed.
   * 	- The `pageSize` property represents the number of elements that can be displayed
   * on a single page.
   * 	- The `totalPages` property indicates the total number of pages available in the
   * collection.
   * 	- The `totalElements` property represents the total number of elements in the collection.
   * 
   * @param page current page being processed, providing the total number of elements
   * on that page.
   * 
   * 	- `pageNumber`: The number of the current page being displayed.
   * 	- `pageSize`: The number of elements displayed on each page.
   * 	- `totalPages`: The total number of pages in the result set.
   * 	- `totalElements`: The total number of elements in the result set.
   * 
   * @returns a `PageInfo` object containing pagination metadata.
   * 
   * 	- The `pageNumber` property represents the page number of the result.
   * 	- The `pageSize` property indicates the size of each page in the result.
   * 	- The `totalPages` property provides the total number of pages in the result.
   * 	- The `totalElements` property gives the total number of elements in the result.
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
