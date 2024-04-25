package com.myhome.utils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * is a data structure that represents information about a page of results, including
 * the current page number, page limit, total pages, and total elements. It provides
 * a convenient way to retrieve and manipulate page-related information.
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
   * size of each page, total number of pages, and total elements for a given `Pageable`
   * and `Page`.
   * 
   * @param pageable pageable object that contains information about the current page
   * of data, such as the page number and size.
   * 
   * 	- `getPageNumber()` returns the current page number.
   * 	- `getPageSize()` returns the total number of elements in this page.
   * 	- `getTotalPages()` returns the total number of pages in the result set.
   * 	- `getTotalElements()` returns the total number of elements in the result set.
   * 
   * @param page current page being processed, providing the total number of elements
   * on that page and the total number of pages in the result set.
   * 
   * 	- `getPageNumber()`: The page number that this page belongs to.
   * 	- `getPageSize()`: The number of elements in each page.
   * 	- `getTotalPages()`: The total number of pages in the result set.
   * 	- `getTotalElements()`: The total number of elements in the result set.
   * 
   * @returns a `PageInfo` object containing four properties: page number, page size,
   * total pages, and total elements.
   * 
   * 1/ pageNumber - The number of the current page being displayed.
   * 2/ pageSize - The size of the page being displayed.
   * 3/ totalPages - The total number of pages in the result set.
   * 4/ totalElements - The total number of elements in the result set.
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
