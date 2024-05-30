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
 * dataset, including the current page number, page size, total pages, and total
 * elements. The class provides a constructor for creating instances of the class
 * from a Pageable and a Page object, and also offers a method for generating a
 * PageInfo object based on a Pageable and a Page.
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
   * creates a `PageInfo` object containing information about the number of pages, size
   * of each page, total number of pages, and total elements in a Pageable and a Page.
   * 
   * @param pageable pagination information for the given `Page<?>` object, containing
   * the number of pages and elements per page.
   * 
   * 	- The page number is specified in `getPageNumber()`
   * 	- The size of the page is specified in `getPageSize()`
   * 	- The total number of pages is specified in `getTotalPages()`
   * 	- The total number of elements is specified in `getTotalElements()`
   * 
   * @param page current page being processed, providing the total number of elements
   * on that page.
   * 
   * 	- `pageNumber`: The page number that contains the element being retrieved.
   * 	- `pageSize`: The number of elements in each page.
   * 	- `totalPages`: The total number of pages available for retrieval.
   * 	- `totalElements`: The total number of elements in all pages combined.
   * 
   * @returns a `PageInfo` object containing page number, size, total pages, and total
   * elements.
   * 
   * 	- The first element is the page number (0-based)
   * 	- The second element is the number of items per page
   * 	- The third element is the total number of pages
   * 	- The fourth element is the total number of items in the collection
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
