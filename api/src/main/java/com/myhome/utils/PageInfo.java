package com.myhome.utils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * from the given file is a data structure that contains information about a page of
 * results in a larger dataset. It has four fields: currentPage, pageLimit, totalPages,
 * and totalElements. The class provides a constructor for creating instances of the
 * class from a Pageable object and a Page object.
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
   * generates a `PageInfo` object representing the specified pageable and page. The
   * object contains information on the current page number, size, total pages, and
   * total elements.
   * 
   * @param pageable pagination information for the current page of data, including the
   * number of pages and the total number of elements in the collection.
   * 
   * 	- `getPageNumber()` returns the page number of the input `pageable`.
   * 	- `getPageSize()` returns the page size of the input `pageable`.
   * 	- `getTotalPages()` returns the total number of pages in the input `pageable`.
   * 	- `getTotalElements()` returns the total number of elements in the input `pageable`.
   * 
   * @param page current page being processed, providing the total number of elements
   * on that page.
   * 
   * 	- `pageNumber`: The zero-based page number of the page being processed.
   * 	- `pageSize`: The number of elements in each page of the pageable input.
   * 	- `totalPages`: The total number of pages that can be generated from the input pageable.
   * 	- `totalElements`: The total number of elements present across all pages of the
   * input pageable.
   * 
   * @returns a `PageInfo` object containing information about the page and total elements.
   * 
   * 	- `pageNumber`: The number of the current page being displayed.
   * 	- `pageSize`: The number of elements in each page.
   * 	- `totalPages`: The total number of pages in the result set.
   * 	- `totalElements`: The total number of elements in the result set.
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
