package com.myhome.utils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * is used to represent information about a page of data containing details such as
 * the current page number, page size, total pages, and total elements. The class
 * provides a constructor for creating instances from a Pageable and a Page object,
 * allowing for efficient creation of PageInfo objects based on input data.
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
   * generates a `PageInfo` object containing information about the specified pageable
   * and page, including page number, page size, total pages, and total elements.
   * 
   * @param pageable pagination information of the result set, providing the number of
   * pages and the size of each page.
   * 
   * 	- `getPageNumber()`: The page number that is being served to the user.
   * 	- `getPageSize()`: The number of elements that can be displayed on a single page.
   * 	- `getTotalPages()`: The total number of pages available in the result set.
   * 	- `getTotalElements()`: The total number of elements returned by the query.
   * 
   * @param page current page being processed, providing information on its position
   * within the paginated collection.
   * 
   * 	- The page number is given by `pageable.getPageNumber()`.
   * 	- The page size is given by `pageable.getPageSize()`.
   * 	- The total pages are given by `page.getTotalPages()`.
   * 	- The total elements are given by `page.getTotalElements()`.
   * 
   * @returns a `PageInfo` object containing page number, size, total pages, and total
   * elements.
   * 
   * 	- pageable.getPageNumber(): The number of the current page being displayed.
   * 	- pageable.getPageSize(): The number of elements on each page.
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
