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
 * The class also provides constructors for creating instances of the class from a
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
   * generates a `PageInfo` object containing information about the page and total
   * elements of a pageable, based on the input `pageable` and `page`.
   * 
   * @param pageable Pageable interface, which provides methods for retrieving and
   * manipulating pages of data.
   * 
   * 	- `getPageNumber()`: The number of the current page being processed.
   * 	- `getPageSize()`: The number of elements in a single page.
   * 	- `getTotalPages()`: The total number of pages in the result set.
   * 	- `getTotalElements()`: The total number of elements in the result set.
   * 
   * @param page current page of results to be processed, providing the number of
   * elements on that page and the total number of pages in the result set.
   * 
   * 	- `pageNumber`: The page number associated with the current page.
   * 	- `pageSize`: The size of each page of the result set.
   * 	- `totalPages`: The total number of pages that can be accessed within a specific
   * range.
   * 	- `totalElements`: The sum of all elements that can be accessed within a specific
   * range.
   * 
   * @returns a `PageInfo` object containing various pagination-related metrics.
   * 
   * 1/ PageNumber - The number of the current page being displayed.
   * 2/ PageSize - The number of elements per page being displayed.
   * 3/ TotalPages - The total number of pages in the dataset.
   * 4/ TotalElements - The total number of elements in the dataset.
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
