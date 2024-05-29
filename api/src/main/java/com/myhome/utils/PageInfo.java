package com.myhome.utils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * is used to represent information about a page of data, including the current page
 * number, page size, total pages, and total elements. The class provides a constructor
 * for creating instances of the class from a Pageable and a Page object, as well as
 * methods for generating a `PageInfo` object containing information about the number
 * of pages, page size, total pages, and total elements of a given `Pageable` and `Page`.
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
   * generates a `PageInfo` object containing information about the number of pages and
   * elements in a paginated collection.
   * 
   * @param pageable Pageable object that contains information about the pagination of
   * the data, which is used to calculate the page number, page size, total pages, and
   * total elements returned by the function.
   * 
   * 	- `pageable.getPageNumber()`: The current page number being accessed.
   * 	- `pageable.getPageSize()`: The number of elements that can be displayed on a
   * single page.
   * 	- `page.getTotalPages()`: The total number of pages in the data set.
   * 	- `page.getTotalElements()`: The total number of elements in the data set.
   * 
   * @param page current page being processed and provides information about its position,
   * size, and total number of pages and elements in the dataset.
   * 
   * 	- `pageable`: The Pageable object representing the paging configuration for this
   * page.
   * 	- `page`: The Page object representing the current page being processed.
   * 
   * The returned `PageInfo` object contains four fields:
   * 
   * 	- `pageNumber`: The number of the current page.
   * 	- `pageSize`: The size of each page.
   * 	- `totalPages`: The total number of pages in the result set.
   * 	- `totalElements`: The total number of elements in the result set.
   * 
   * @returns a `PageInfo` object containing various pagination-related information.
   * 
   * 	- The `pageNumber` attribute represents the current page being displayed.
   * 	- The `pageSize` attribute signifies the number of elements per page.
   * 	- The `totalPages` attribute indicates the total number of pages available in the
   * data set.
   * 	- The `totalElements` attribute shows the overall number of elements in the data
   * set.
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
