package com.myhome.utils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * is a data structure used to represent pagination information for a pageable dataset.
 * It contains four fields: currentPage, pageLimit, totalPages, and totalElements.
 * The class provides a constructor for creating instances of the class with the
 * necessary parameters from a Pageable and a Page object.
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
   * creates a `PageInfo` object containing information about the number of pages, page
   * size, total pages, and total elements of a given `Pageable` and `Page`.
   * 
   * @param pageable pagination information for the current page of data, providing the
   * page number, page size, total pages, and total elements.
   * 
   * 	- `getPageNumber()` - Returns the current page number.
   * 	- `getPageSize()` - Returns the number of elements per page.
   * 	- `getTotalPages()` - Returns the total number of pages in the result set.
   * 	- `getTotalElements()` - Returns the total number of elements in the result set.
   * 
   * @param page current page being processed, providing the total number of elements
   * on that page and the number of elements per page for calculation of pagination metadata.
   * 
   * 	- `pageNumber`: The number of the current page being displayed.
   * 	- `pageSize`: The number of elements in each page.
   * 	- `totalPages`: The total number of pages available for display.
   * 	- `totalElements`: The total number of elements in the data set.
   * 
   * @returns a `PageInfo` object containing page number, size, total pages, and total
   * elements.
   * 
   * 	- pageable.getPageNumber(): The number of the current page being displayed.
   * 	- pageable.getPageSize(): The number of elements displayed per page.
   * 	- page.getTotalPages(): The total number of pages in the data set.
   * 	- page.getTotalElements(): The total number of elements in the data set.
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
