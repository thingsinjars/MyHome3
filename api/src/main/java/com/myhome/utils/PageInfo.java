package com.myhome.utils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * is a data structure that provides information about a page of results from a larger
 * dataset. It consists of currentPage, pageLimit, totalPages, and totalElements. The
 * class also includes a static method for creating a new instance of the class based
 * on a Pageable object and a Page object.
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
   * size, total pages, and total elements of a given pageable and page.
   * 
   * @param pageable pageable object that contains information about the current page
   * being processed, including the page number and the size of the page.
   * 
   * 	- `getPageNumber()`: The page number associated with the provided `page`.
   * 	- `getPageSize()`: The number of elements that can be displayed on a single page.
   * 	- `getTotalPages()`: The total number of pages in the result set.
   * 	- `getTotalElements()`: The total number of elements in the result set.
   * 
   * @param page current page being processed and provides the total number of elements
   * on that page.
   * 
   * 1/ `pageNumber`: The page number of the resultant page, which is the number of the
   * page in the paginated sequence.
   * 2/ `pageSize`: The size of each page in the pagination sequence, indicating how
   * many elements can be displayed on a single page.
   * 3/ `totalPages`: The total number of pages that can be displayed based on the input
   * `pageable` and `page`.
   * 4/ `totalElements`: The total number of elements in the resultant paginated sequence.
   * 
   * @returns a `PageInfo` object containing information about the current page and
   * total pages and elements.
   * 
   * 	- The first element is the page number (0-based) of the current page.
   * 	- The second element is the number of elements per page.
   * 	- The third element is the total number of pages in the result set.
   * 	- The fourth element is the total number of elements in the result set.
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
