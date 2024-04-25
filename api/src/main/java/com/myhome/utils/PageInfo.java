package com.myhome.utils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * is a data structure that provides information about a page of results returned by
 * a pagination system. It contains four fields: currentPage, pageLimit, totalPages,
 * and totalElements. The class also includes a factory method for creating new
 * instances based on a pageable object and a Page object.
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
   * takes a `pageable` and a `page` object as input, and returns a `PageInfo` object
   * containing various information about the page.
   * 
   * @param pageable pagination information for the query result, providing the page
   * number, page size, total pages, and total elements.
   * 
   * 	- `pageNumber`: The number of the page being queried.
   * 	- `pageSize`: The number of elements in a single page of the result set.
   * 	- `totalPages`: The total number of pages in the result set.
   * 	- `totalElements`: The total number of elements in the result set.
   * 
   * @param page current page being processed, providing the total number of elements
   * on that page.
   * 
   * 	- `pageNumber`: The page number of the result.
   * 	- `pageSize`: The size of each page in the result.
   * 	- `totalPages`: The total number of pages in the result.
   * 	- `totalElements`: The total number of elements in the result.
   * 
   * @returns a `PageInfo` object containing page number, page size, total pages, and
   * total elements.
   * 
   * 	- The first item in the output is the page number, which is an integer representing
   * the current page being displayed.
   * 	- The second item is the page size, which is also an integer and represents the
   * total number of elements that can be displayed on a single page.
   * 	- The third item is the total number of pages, which is an integer and indicates
   * the total number of pages available in the result set.
   * 	- The fourth item is the total number of elements, which is also an integer and
   * represents the total number of elements in the result set.
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
