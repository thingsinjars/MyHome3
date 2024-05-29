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
 * total pages, and total elements of a given Pageable and Page object. It generates
 * a PageInfo object containing page number, size, total pages, and total elements
 * based on the provided Pageable and Page objects.
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
   * size of each page, total number of pages and elements in the result set.
   * 
   * @param pageable pageable object that contains information about the current page
   * being processed, including its number and size.
   * 
   * 	- `getPageNumber()`: The page number of the current page being served.
   * 	- `getPageSize()`: The number of elements in each page of the result set.
   * 	- `getTotalPages()`: The total number of pages in the result set.
   * 	- `getTotalElements()`: The total number of elements in the result set.
   * 
   * @param page current page being processed, providing the total number of elements
   * on that page.
   * 
   * 	- `pageNumber`: The number of the page being returned.
   * 	- `pageSize`: The size of each page being returned.
   * 	- `totalPages`: The total number of pages available in the result set.
   * 	- `totalElements`: The total number of elements in the result set.
   * 
   * @returns a `PageInfo` object containing various pagination-related metadata.
   * 
   * 	- The page number is provided in the PageInfo object as the first attribute (`pageable.getPageNumber()`).
   * 	- The size of the pages is given by the second parameter (`pageable.getPageSize()`).
   * 	- The overall number of pages is represented by the third parameter
   * (`page.getTotalPages()`), while the total number of elements in each page is
   * indicated by the fourth parameter (`page.getTotalElements()`).
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
