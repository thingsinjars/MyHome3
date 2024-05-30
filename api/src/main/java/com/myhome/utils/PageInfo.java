package com.myhome.utils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * is a data structure that provides information about a page of results, including
 * the current page number, page size, total pages, and total elements. It generates
 * a `PageInfo` object containing these values based on a given `Pageable` and `Page`.
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
   * takes a `pageable` and a `page` object as input, and returns a `PageInfo` object
   * containing information about the page number, size, total pages, and total elements.
   * 
   * @param pageable pagination information of the query, providing the total number
   * of pages and elements per page.
   * 
   * 	- `pageable.getPageNumber()`: The page number of the current page being processed.
   * 	- `pageable.getPageSize()`: The size of each page in the paginated result.
   * 	- `page.getTotalPages()`: The total number of pages in the result set.
   * 	- `page.getTotalElements()`: The total number of elements in the result set.
   * 
   * @param page current page being processed, providing the total number of elements
   * on that page.
   * 
   * 1/ PageNumber - The page number of the result set.
   * 2/ PageSize - The number of elements in each page of the result set.
   * 3/ TotalPages - The total number of pages in the result set.
   * 4/ TotalElements - The total number of elements in the result set.
   * 
   * @returns a `PageInfo` object containing various page-related metrics.
   * 
   * 	- The first element of the PageInfo object is the page number, which indicates
   * the position of the page in the overall collection of pages.
   * 	- The second element is the page size, which represents the total number of
   * elements that can be displayed on a single page.
   * 	- The third element is the total number of pages, which gives the overall number
   * of pages in the collection.
   * 	- The fourth and final element is the total number of elements, which represents
   * the overall number of elements in the collection.
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
