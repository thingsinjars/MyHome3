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
 * offers a constructor for creating instances from a Pageable and a Page object, as
 * well as a method for generating a new PageInfo object based on the current page,
 * page limit, total pages, and total elements of a given page.
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
   * creates a `PageInfo` object containing information about the number of pages, size
   * of each page, total number of pages, and total elements in the page.
   * 
   * @param pageable pagination state of the application, providing the current page
   * number, page size, total pages, and total elements.
   * 
   * 	- `getPageNumber()`: Returns the page number of the current page being processed.
   * 	- `getPageSize()`: Returns the size of a page in the paginated result.
   * 	- `getTotalPages()`: Returns the total number of pages in the result set.
   * 	- `getTotalElements()`: Returns the total number of elements in the result set.
   * 
   * @param page current page of data to be processed, providing information on its
   * total elements and number of pages.
   * 
   * 	- `pageNumber`: The page number associated with the current page of results.
   * 	- `pageSize`: The number of elements in a page of results.
   * 	- `totalPages`: The total number of pages available for pagination.
   * 	- `totalElements`: The total number of elements returned by the query.
   * 
   * @returns a `PageInfo` object containing information about the page number, size,
   * total pages, and total elements of a given pageable and page.
   * 
   * 	- pageable.getPageNumber(): The number of the current page being processed.
   * 	- pageable.getPageSize(): The size of the page being processed.
   * 	- page.getTotalPages(): The total number of pages in the result set.
   * 	- page.getTotalElements(): The total number of elements in the result set.
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
