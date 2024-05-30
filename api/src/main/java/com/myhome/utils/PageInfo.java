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
 * Page object, as well as a method for generating a PageInfo object containing these
 * values.
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
   * size of each page, total pages, and total elements for a given `Pageable` and `Page`.
   * 
   * @param pageable pagination information of the result set, providing the page number,
   * page size, total pages, and total elements for the current page.
   * 
   * 	- `pageable.getPageNumber()` represents the current page number being processed.
   * 	- `pageable.getPageSize()` denotes the number of elements per page.
   * 	- `page.getTotalPages()` indicates the total number of pages in the result set.
   * 	- `page.getTotalElements()` reveals the sum of all elements across all pages.
   * 
   * @param page current page being processed, providing the total number of elements
   * on that page and the total number of pages in the result set.
   * 
   * 	- `pageNumber`: The page number of the current page being processed.
   * 	- `pageSize`: The number of elements in each page.
   * 	- `totalPages`: The total number of pages in the result set.
   * 	- `totalElements`: The total number of elements returned by the query.
   * 
   * @returns a `PageInfo` object containing page number, size, total pages, and total
   * elements.
   * 
   * 	- The first element is `pageNumber`, which represents the number of the current
   * page being displayed.
   * 	- The second element is `pageSize`, which indicates the number of elements that
   * can be displayed on a single page.
   * 	- The third element is `totalPages`, which gives the total number of pages in the
   * result set.
   * 	- The fourth and final element is `totalElements`, which represents the overall
   * number of elements in the result set.
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
