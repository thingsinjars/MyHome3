package com.myhome.utils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * is a data structure that represents a page of results from a query. It contains
 * information about the current page being viewed, the number of elements on the
 * page, and the total number of pages in the result set. The class also includes
 * methods for creating new instances of the class based on pageable and page parameters.
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
   * size, total pages, and total elements for a given pageable and page.
   * 
   * @param pageable pageable object that contains information about the current page
   * being processed, including its page number and size.
   * 
   * 	- `pageable.getPageNumber()`: The page number of the current page being processed.
   * 	- `pageable.getPageSize()`: The number of elements in each page.
   * 	- `page.getTotalPages()`: The total number of pages in the collection.
   * 	- `page.getTotalElements()`: The total number of elements in the collection.
   * 
   * @param page current page being processed, providing information on its position,
   * size, and total pages and elements in the collection.
   * 
   * 	- `pageNumber`: The page number that contains the element(s) being processed.
   * 	- `pageSize`: The number of elements in each page.
   * 	- `totalPages`: The total number of pages that contain elements.
   * 	- `totalElements`: The total number of elements across all pages.
   * 
   * @returns a `PageInfo` object containing various metadata about a pageable and its
   * corresponding page.
   * 
   * 	- pageable.getPageNumber(): The number of the current page being accessed.
   * 	- pageable.getPageSize(): The size of each page being displayed.
   * 	- page.getTotalPages(): The total number of pages available in the dataset.
   * 	- page.getTotalElements(): The total number of elements within the current page
   * and all previous pages.
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
