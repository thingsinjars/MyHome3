package com.myhome.utils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * is used to provide information about a page of results in a larger dataset. It
 * includes four fields: currentPage, pageLimit, totalPages, and totalElements. The
 * class also provides constructors for creating instances of the class from a Pageable
 * object and a Page object.
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
   * takes a `pageable` and a `page` as input, returns a `PageInfo` object containing
   * information about the page number, size, total pages, and total elements.
   * 
   * @param pageable page request, providing the number of pages and the page size to
   * calculate the total pages and elements in the result.
   * 
   * 	- `getPageNumber()`: Returns the current page number being displayed.
   * 	- `getPageSize()`: Returns the number of elements per page.
   * 	- `getTotalPages()`: Returns the total number of pages in the result set.
   * 	- `getTotalElements()`: Returns the total number of elements in the result set.
   * 
   * @param page current page being processed, providing the total number of elements
   * on that page and the total number of pages in the overall result set.
   * 
   * 	- The first parameter `pageable` represents the pageable object that contains
   * information about the current page being processed.
   * 	- The second parameter `page` represents the Page object returned by the pageable
   * object's `getPage()` method, which contains additional information about the current
   * page.
   * 
   * @returns a `PageInfo` object containing information about the page number, size,
   * total pages, and total elements of a pageable.
   * 
   * 	- pageable.getPageNumber(): The number of the current page being displayed.
   * 	- pageable.getPageSize(): The number of elements on each page.
   * 	- page.getTotalPages(): The total number of pages in the result set.
   * 	- page.getTotalElements(): The total number of elements returned by the query.
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
