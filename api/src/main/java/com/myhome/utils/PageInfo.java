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
 * a PageInfo object containing these values for use in pagination sequences.
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
   * @param pageable pagination information of the current page, including the page
   * number, page size, total pages, and total elements.
   * 
   * 	- `getPageNumber()`: The page number of the page being processed.
   * 	- `getPageSize()`: The number of elements in a single page.
   * 	- `getTotalPages()`: The total number of pages in the dataset.
   * 	- `getTotalElements()`: The total number of elements in the dataset.
   * 
   * @param page current page being processed, providing the total number of elements
   * on that page.
   * 
   * 	- `pageNumber`: The page number of the result, which can range from 1 to the total
   * number of pages.
   * 	- `pageSize`: The number of elements in a single page, which can vary depending
   * on the input `page`.
   * 	- `totalPages`: The total number of pages in the result, including any partial pages.
   * 	- `totalElements`: The total number of elements in the result, including any
   * partial elements.
   * 
   * @returns a `PageInfo` object containing information about the page number, size,
   * total pages, and total elements of a given pageable and page.
   * 
   * 	- The first parameter is `pageable`, which represents the pageable object containing
   * information about the current page being processed.
   * 	- The second parameter is `page`, which represents the specific page being processed.
   * 	- The return value is a `PageInfo` object, which contains four attributes:
   * 	+ `pageNumber`: The number of the current page being processed.
   * 	+ `pageSize`: The size of the current page being processed.
   * 	+ `totalPages`: The total number of pages in the result set.
   * 	+ `totalElements`: The total number of elements in the result set.
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
