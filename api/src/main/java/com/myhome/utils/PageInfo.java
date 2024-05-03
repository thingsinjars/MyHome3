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
 * a PageInfo object containing these values and can be constructed from a Pageable
 * and a Page object using the `of()` method.
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
   * @param pageable page request parameters, such as the number of pages to display
   * or the size of each page.
   * 
   * 	- `getPageNumber()`: Returns the page number of the current page being processed.
   * 	- `getPageSize()`: Returns the size of each page of elements returned by the server.
   * 	- `getTotalPages()`: Returns the total number of pages that the server can return
   * for a given query.
   * 	- `getTotalElements()`: Returns the total number of elements that the server can
   * return for a given query.
   * 
   * @param page current page being processed, providing the total number of elements
   * on that page.
   * 
   * 	- `pageNumber`: The page number that the input `page` represents.
   * 	- `pageSize`: The number of elements in a page of the input `page`.
   * 	- `totalPages`: The total number of pages in the input `page`.
   * 	- `totalElements`: The total number of elements in the input `page`.
   * 
   * @returns a `PageInfo` object containing various information about the page of data.
   * 
   * 	- pageable.getPageNumber(): The number of the current page being displayed.
   * 	- pageable.getPageSize(): The number of elements on each page being displayed.
   * 	- page.getTotalPages(): The total number of pages in the data set.
   * 	- page.getTotalElements(): The total number of elements in the data set.
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
