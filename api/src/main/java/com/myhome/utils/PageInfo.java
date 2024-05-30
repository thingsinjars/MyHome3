package com.myhome.utils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * is a data structure that stores information about a page of results, including its
 * number, size, total pages, and elements. The class provides a constructor for
 * creating instances from a Pageable and a Page object, as well as methods for
 * generating a `PageInfo` object based on these inputs.
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
   * transforms a `Pageable` and a `Page` into a `PageInfo` object, providing the number
   * of pages, page size, total pages, and total elements for the page.
   * 
   * @param pageable Pageable object containing information about the pagination of the
   * data, which is used to calculate the page number, size, and total pages and elements.
   * 
   * 	- `getPageNumber()`: The number of the current page being displayed.
   * 	- `getPageSize()`: The number of elements in a single page of the data set.
   * 	- `getTotalPages()`: The total number of pages in the data set.
   * 	- `getTotalElements()`: The total number of elements in the data set.
   * 
   * @param page current page being processed, providing information on its position
   * and size within the overall result set.
   * 
   * 	- `getPageNumber(): int`: Returns the current page number of the iteration.
   * 	- `getPageSize(): int`: Returns the number of elements in a single page.
   * 	- `getTotalPages(): int`: Returns the total number of pages in the result set.
   * 	- `getTotalElements(): long`: Returns the total number of elements returned by
   * the query.
   * 
   * @returns a `PageInfo` object containing page number, size, total pages, and total
   * elements.
   * 
   * 	- The first argument is Pageable, which contains information about the pagination
   * of the page.
   * 	- The second argument is Page, which represents a single page of data.
   * 	- The return value is PageInfo, which encapsulates information about the page
   * number, page size, total pages, and total elements in the dataset.
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
