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
 * PageInfo object containing these values based on the provided Pageable and Page objects.
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
   * creates a `PageInfo` object with information about the number of pages, size of
   * each page, total number of pages, and total elements in a given pageable and page.
   * 
   * @param pageable page number and size of the current page being processed, which
   * are used to calculate the total pages and elements in the resultant `PageInfo`.
   * 
   * 	- The `pageNumber` property represents the current page number being served.
   * 	- The `pageSize` property indicates the number of elements per page.
   * 	- The `totalPages` property denotes the total number of pages in the result set.
   * 	- The `totalElements` property provides the overall number of elements returned
   * by the query.
   * 
   * @param page current page being processed, providing the total number of elements
   * on that page.
   * 
   * 	- `pageNumber`: The page number that this page is associated with.
   * 	- `pageSize`: The size of each page in the paginated data set.
   * 	- `totalPages`: The total number of pages in the paginated data set.
   * 	- `totalElements`: The total number of elements in the paginated data set.
   * 
   * @returns a `PageInfo` object containing information about the page number, size,
   * total pages, and total elements of a given pageable and page.
   * 
   * 	- The first element is the page number (0-based) represented by the `Pageable`
   * object passed in as argument 1.
   * 	- The second element is the number of items per page, also represented by the
   * `Pageable` object.
   * 	- The third element is the total number of pages, computed from the last page
   * number and the number of items per page.
   * 	- The fourth element represents the total count of elements across all pages.
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
