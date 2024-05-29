package com.myhome.utils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * is a data structure that contains information about a page of results in a larger
 * dataset. It has four fields: currentPage, pageLimit, totalPages, and totalElements.
 * The class also provides a constructor for creating instances of the class from a
 * Pageable object and a Page object.
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
   * takes a `pageable` and a `page` as input, returning a `PageInfo` object that
   * contains information about the page number, page size, total pages, and total elements.
   * 
   * @param pageable pagination context for the page being processed, providing information
   * on the current page number, page size, total pages, and total elements.
   * 
   * The first argument is a `Pageable` object, which contains information about the
   * pagination process. Specifically, it holds the page number (represented by an
   * integer), size (represented by an integer), and total pages (represented by an
   * integer) for the current iteration of the pagination process.
   * 
   * The second argument is a `Page` object, which contains information about the
   * elements in the paginated result set. Specifically, it holds the total number of
   * pages (represented by an integer), the total number of elements (represented by
   * an integer), and the current page's elements (represented by a collection of objects).
   * 
   * @param page current page being processed, providing information on its position
   * and size within the overall paginated result.
   * 
   * 	- `pageNumber`: The number of the current page being rendered.
   * 	- `pageSize`: The size of each page being rendered.
   * 	- `totalPages`: The total number of pages in the result set.
   * 	- `totalElements`: The total number of elements in the result set.
   * 
   * @returns a `PageInfo` object containing various pagination-related information.
   * 
   * 	- `pageNumber`: The number of the current page being displayed.
   * 	- `pageSize`: The number of elements in each page being displayed.
   * 	- `totalPages`: The total number of pages available in the result set.
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
