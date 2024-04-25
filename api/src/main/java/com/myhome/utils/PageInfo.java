package com.myhome.utils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * is a data structure used to represent information about a page of results in a
 * larger dataset. It provides the current page being viewed, the maximum number of
 * pages that can be displayed, and the total number of pages and elements in the
 * dataset. The class also includes a method for creating a new instance of the class
 * with the specified pageable and page parameters.
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
   * page size, total pages, and total elements of a given `Pageable` and `Page`.
   * 
   * @param pageable pageable object that contains information about the current page
   * being processed, including the page number and size.
   * 
   * 	- `getPageNumber()`: Returns the page number of the current page being processed.
   * 	- `getPageSize()`: Returns the size of a page in the collection being processed.
   * 	- `getTotalPages()`: Returns the total number of pages in the collection being processed.
   * 	- `getTotalElements()`: Returns the total number of elements in the collection
   * being processed.
   * 
   * @param page current page being processed, providing the total number of elements
   * on that page and the total number of pages in the result set.
   * 
   * 	- `pageNumber`: The number of the page being returned.
   * 	- `pageSize`: The size of each page of elements returned in the result set.
   * 	- `totalPages`: The total number of pages that can be returned based on the input
   * query.
   * 	- `totalElements`: The total number of elements contained within all pages returned
   * in the result set.
   * 
   * @returns a `PageInfo` object containing information about the number of pages,
   * size of each page, total number of pages, and total elements in the page.
   * 
   * 	- pageable.getPageNumber(): The page number of the current page being processed.
   * 	- pageable.getPageSize(): The number of elements in each page being processed.
   * 	- page.getTotalPages(): The total number of pages in the paginated data set.
   * 	- page.getTotalElements(): The total number of elements in the paginated data set.
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
