package com.myhome.utils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * provides information about the number of pages, page size, total pages, and total
 * elements of a given Pageable and Page object. The class offers a constructor for
 * creating instances of the class from a Pageable and a Page object, as well as
 * methods for generating a `PageInfo` object containing this data.
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
   * generates a `PageInfo` object containing page-related information, such as the
   * current page number, size, total pages, and total elements, given a `Pageable`
   * object and a `Page` object.
   * 
   * @param pageable Pageable object that contains information about the pagination of
   * the data, such as the current page number and page size.
   * 
   * 	- The first argument `pageable` is an instance of `Pageable`.
   * 	- The `getPageNumber()` method returns the page number of the current page being
   * displayed.
   * 	- The `getPageSize()` method returns the number of elements in each page.
   * 	- The `getTotalPages()` method returns the total number of pages available for display.
   * 	- The `getTotalElements()` method returns the total number of elements in the
   * entire dataset.
   * 
   * @param page current page of data being processed, providing the total number of
   * elements on that page.
   * 
   * 	- `getPageNumber()`: The page number of the resulting page set.
   * 	- `getPageSize()`: The total number of elements in the resulting page set.
   * 	- `getTotalPages()`: The total number of pages in the paginated data set.
   * 	- `getTotalElements()`: The total number of elements in the entire data set.
   * 
   * @returns a `PageInfo` object containing page metadata.
   * 
   * 	- PageNumber: The number of the current page being displayed.
   * 	- PageSize: The number of elements on each page of the result set.
   * 	- TotalPages: The total number of pages in the result set.
   * 	- TotalElements: The total number of elements in the result set.
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
