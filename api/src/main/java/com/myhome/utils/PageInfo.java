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
   * generates a `PageInfo` object containing information about the number of pages,
   * page size, total pages, and total elements for a given `Pageable` and `Page`.
   * 
   * @param pageable pageable object containing information about the current page of
   * data being processed, which is used to calculate various pagination-related values
   * for the `PageInfo` object returned by the function.
   * 
   * 	- `getPageNumber(): int`: The number of the current page being accessed.
   * 	- `getPageSize(): int`: The size of a single page in the paginated collection.
   * 	- `getTotalPages(): int`: The total number of pages available for access in the
   * paginated collection.
   * 	- `getTotalElements(): long`: The overall number of elements present within all
   * the pages accessible in the paginated collection.
   * 
   * @param page current page being processed, providing information on its position
   * and size within the total number of pages and elements.
   * 
   * 	- The page number is described by `pageable.getPageNumber()`.
   * 	- The page size is described by `pageable.getPageSize()`.
   * 	- The total number of pages is described by `page.getTotalPages()`.
   * 	- The total number of elements is described by `page.getTotalElements()`.
   * 
   * @returns a `PageInfo` object containing various page-related metrics.
   * 
   * 	- pageable.getPageNumber(): The number of the current page being displayed.
   * 	- pageable.getPageSize(): The size of each page of elements displayed.
   * 	- page.getTotalPages(): The total number of pages in the dataset.
   * 	- page.getTotalElements(): The total number of elements in the dataset.
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
