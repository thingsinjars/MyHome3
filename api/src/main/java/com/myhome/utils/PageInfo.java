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
 * The class provides a constructor for creating instances of the class from a Pageable
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
   * generates a `PageInfo` object from a `Pageable` and a `Page`. It returns values
   * for the current page number, page size, total pages, and total elements.
   * 
   * @param pageable pageable object that contains information about the current page
   * being processed, including its number and size.
   * 
   * 	- `pageable.getPageNumber()`: The current page number being accessed.
   * 	- `pageable.getPageSize()`: The number of elements in a page.
   * 	- `page.getTotalPages()`: The total number of pages available for the given input
   * size.
   * 	- `page.getTotalElements()`: The total number of elements in the input.
   * 
   * @param page current page being processed and provides information on its position,
   * size, total number of pages, and total elements.
   * 
   * 	- `pageNumber`: The number of the current page being displayed.
   * 	- `pageSize`: The number of elements in a single page.
   * 	- `totalPages`: The total number of pages in the result set.
   * 	- `totalElements`: The total number of elements in the result set.
   * 
   * @returns a `PageInfo` object containing pagination metadata.
   * 
   * 	- The first element is the page number (0-based) representing the position of the
   * current page in the paginated sequence.
   * 	- The second element is the page size, which indicates the number of elements
   * visible on a single page.
   * 	- The third element is the total number of pages available for the given pageable.
   * 	- The fourth element represents the total number of elements across all pages.
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
