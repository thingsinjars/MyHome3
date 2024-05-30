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
 * elements of a given Pageable and Page. The class offers a constructor for creating
 * instances from a Pageable and a Page object, and includes methods for generating
 * a `PageInfo` object based on a Pageable and a Page.
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
   * size of each page, total number of pages, and total elements in a given pageable
   * and page.
   * 
   * @param pageable pagination state of the query, providing information on the current
   * page number, page size, total pages, and total elements.
   * 
   * 	- `pageable.getPageNumber()` is an integer indicating the page number out of total
   * pages.
   * 	- `pageable.getPageSize()` is an integer indicating the number of elements per page.
   * 	- `page.getTotalPages()` is an integer indicating the total number of pages in
   * the result set.
   * 	- `page.getTotalElements()` is an integer indicating the total number of elements
   * in the result set.
   * 
   * @param page current page being processed, providing the number of elements on that
   * page and the total number of pages in the result set.
   * 
   * 	- `pageNumber`: The page number that this page belongs to.
   * 	- `pageSize`: The size of each page in the result set.
   * 	- `totalPages`: The total number of pages in the result set.
   * 	- `totalElements`: The total number of elements in the result set.
   * 
   * @returns a `PageInfo` object containing information about the page number, size,
   * total pages, and total elements of a `Pageable` and a `Page`.
   * 
   * 	- The first item in the output is the page number (0-based) of the page being queried.
   * 	- The second item is the number of elements per page.
   * 	- The third item is the total number of pages available for a given query.
   * 	- The fourth and final item is the total number of elements that match the query.
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
