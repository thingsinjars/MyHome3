package com.myhome.utils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * is a data structure that stores information about the number of pages, page size,
 * total pages, and total elements in a dataset. The class provides a constructor for
 * creating instances from a Pageable and a Page object, allowing for efficient
 * generation of PageInfos based on these inputs.
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
   * of each page, total number of pages and total elements in a given pageable and page.
   * 
   * @param pageable pagination information for the current page of data, providing the
   * number of pages and the number of elements on each page.
   * 
   * 	- `getPageNumber()` returns the page number of the input `pageable`.
   * 	- `getPageSize()` returns the page size of the input `pageable`.
   * 	- `getTotalPages()` returns the total number of pages of the input `pageable`.
   * 	- `getTotalElements()` returns the total number of elements in the input `pageable`.
   * 
   * @param page current page being processed, providing the total number of elements
   * on that page and the total number of pages in the result set.
   * 
   * 	- `pageNumber`: The number of the page being processed.
   * 	- `pageSize`: The size of each page being processed.
   * 	- `totalPages`: The total number of pages in the result set.
   * 	- `totalElements`: The total number of elements in the result set.
   * 
   * @returns a `PageInfo` object containing page number, size, total pages, and total
   * elements.
   * 
   * 	- `pageNumber`: The page number of the current page being processed.
   * 	- `pageSize`: The size of each page in the paginated result.
   * 	- `totalPages`: The total number of pages in the paginated result.
   * 	- `totalElements`: The total number of elements in the paginated result.
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
