package com.myhome.utils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * is a data structure that provides information about a page of results in a larger
 * dataset, including the current page number, page size, total pages, and total
 * elements. It can be constructed from a Pageable object and a Page object using the
 * `of()` method.
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
   * of each page, total number of pages, and total elements in a pageable and a page.
   * 
   * @param pageable page number and size of the page being processed, which is used
   * to calculate the total pages and elements in thePageInfo object returned by the function.
   * 
   * The first parameter, `pageable`, is an instance of `Pageable`. This class contains
   * several attributes, such as page number (`getPageNumber()`), page size (`getPageSize()`,
   * and total pages (`getTotalPages()`). Additionally, `pageable` may also have other
   * properties or attributes not listed here.
   * 
   * @param page current page being processed, providing the total number of elements
   * on that page.
   * 
   * 	- `pageNumber`: The page number of the input `page`.
   * 	- `pageSize`: The size of the input `page`.
   * 	- `totalPages`: The total number of pages in the input `page`.
   * 	- `totalElements`: The total number of elements in the input `page`.
   * 
   * @returns a `PageInfo` object containing page number, size, total pages, and total
   * elements.
   * 
   * 	- pageable.getPageNumber(): The zero-based index of the current page being displayed.
   * 	- pageable.getPageSize(): The number of elements in each page being displayed.
   * 	- page.getTotalPages(): The total number of pages in the result set.
   * 	- page.getTotalElements(): The total number of elements in the result set.
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
