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
 * total pages, and total elements of a given Pageable and Page. The class offers a
 * constructor for creating instances from a Pageable and a Page object, as well as
 * a method for generating a PageInfo object based on the current page being processed
 * and the total number of pages and elements available.
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
   * generates a `PageInfo` object containing information about the current page of a
   * paginated result, such as the page number, size, total pages, and total elements.
   * 
   * @param pageable page request, providing the number of pages and the size of each
   * page to generate the page information.
   * 
   * 	- `getPageNumber()` returns the page number of the result set.
   * 	- `getPageSize()` returns the number of elements in each page of the result set.
   * 	- `getTotalPages()` returns the total number of pages in the result set.
   * 	- `getTotalElements()` returns the total number of elements in the result set.
   * 
   * @param page current page being processed, providing the number of elements it
   * contains and the total number of pages in the dataset.
   * 
   * 	- `pageNumber`: The current page number being displayed.
   * 	- `pageSize`: The size of each page in terms of elements.
   * 	- `totalPages`: The total number of pages available for display.
   * 	- `totalElements`: The total number of elements available in the collection.
   * 
   * @returns a `PageInfo` object containing various page-related metrics.
   * 
   * 	- pageable.getPageNumber(): The zero-based index of the current page being displayed.
   * 	- pageable.getPageSize(): The number of elements in each page.
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
