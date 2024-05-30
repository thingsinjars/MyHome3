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
 * total pages, and total elements of a given Pageable and Page. It generates a
 * PageInfo object containing page number, size, total pages, and total elements based
 * on the provided Pageable and Page objects.
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
   * creates a `PageInfo` object that contains information about the current page and
   * total number of pages and elements in a pageable collection.
   * 
   * @param pageable Pageable object containing information about the current page of
   * data to be processed, which is used to calculate the page number, size, and total
   * pages and elements.
   * 
   * 	- The first parameter is `pageable`, which represents a pageable object that
   * contains information about the current page being processed.
   * 	- The second parameter is `page`, which refers to the actual page being processed.
   * 	- The returned `PageInfo` object contains four properties:
   * 	+ `pageNumber`: The number of the current page being processed.
   * 	+ `pageSize`: The size of the current page being processed.
   * 	+ `totalPages`: The total number of pages available for processing.
   * 	+ `totalElements`: The total number of elements available for processing.
   * 
   * @param page current page being processed, providing information on its position
   * within the overall paginated result set.
   * 
   * 	- `pageNumber`: The page number that contains the element being retrieved.
   * 	- `pageSize`: The number of elements in each page.
   * 	- `totalPages`: The total number of pages in the result set.
   * 	- `totalElements`: The total number of elements in the result set.
   * 
   * @returns a `PageInfo` object containing page number, size, total pages, and total
   * elements.
   * 
   * 	- The first argument is PageNumber, which represents the page number of the current
   * page being processed.
   * 	- The second argument is PageSize, which represents the number of elements in a
   * single page.
   * 	- The third argument is TotalPages, which represents the total number of pages
   * available in the collection.
   * 	- The fourth argument is TotalElements, which represents the total number of
   * elements in the entire collection.
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
