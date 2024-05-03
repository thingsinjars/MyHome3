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
 * total pages, and total elements of a given Pageable and Page. The class provides
 * a constructor for creating instances of the class from a Pageable and a Page object,
 * and also offers a static method for generating a PageInfo object based on a Pageable
 * and a Page.
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
   * creates a `PageInfo` object containing metadata about a pageable and the page it
   * represents, including the current page number, size, total pages, and total elements.
   * 
   * @param pageable pagination state of the data being queried, providing the current
   * page number, page size, total pages, and total elements.
   * 
   * 	- `pageable.getPageNumber()`: The page number of the resulting page.
   * 	- `pageable.getPageSize()`: The size of each page returned by the pageable.
   * 	- `page.getTotalPages()`: The total number of pages available for the given input.
   * 	- `page.getTotalElements()`: The total number of elements in the result set.
   * 
   * @param page current page being processed and provides information about its total
   * pages and elements.
   * 
   * 1/ `pageNumber`: The page number as determined by the `Pageable`.
   * 2/ `pageSize`: The size of each page as determined by the `Pageable`.
   * 3/ `totalPages`: The total number of pages as determined by the `Pageable`.
   * 4/ `totalElements`: The total number of elements in all pages as determined by the
   * `Pageable`.
   * 
   * @returns a `PageInfo` object containing page number, size, total pages, and total
   * elements.
   * 
   * 	- The first parameter, `pageable`, represents the pageable object containing
   * information about the current page being processed.
   * 	- The second parameter, `page`, represents the page object containing information
   * about the specific page being processed.
   * 	- The `PageNumber` property in the return value indicates the current page number
   * being processed.
   * 	- The `PageSize` property indicates the size of the page being processed.
   * 	- The `TotalPages` property represents the total number of pages in the collection.
   * 	- The `TotalElements` property represents the total number of elements in the collection.
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
