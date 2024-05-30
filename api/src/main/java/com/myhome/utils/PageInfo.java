package com.myhome.utils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * provides a structured representation of page and element counts for a given input.
 * It takes in a Pageable and a Page object as inputs and returns a PageInfo object
 * containing information on the current page number, size, total pages, and elements.
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
   * returns a `PageInfo` object containing information about the number of pages, page
   * size, total pages, and total elements for a given pageable and page.
   * 
   * @param pageable pageable object that contains information about the current page
   * of results, including the page number and size.
   * 
   * 	- `getPageNumber()` returns the current page number.
   * 	- `getPageSize()` returns the number of elements in each page.
   * 	- `getTotalPages()` returns the total number of pages.
   * 	- `getTotalElements()` returns the total number of elements in the result set.
   * 
   * @param page current page of data being processed, providing the total number of
   * elements on that page.
   * 
   * 	- `pageNumber`: The number of the page being referenced.
   * 	- `pageSize`: The size of each page in the collection.
   * 	- `totalPages`: The total number of pages in the collection.
   * 	- `totalElements`: The total number of elements in the collection.
   * 
   * @returns a `PageInfo` object containing page-related metadata.
   * 
   * 	- pageable.getPageNumber(): The number of the current page being displayed.
   * 	- pageable.getPageSize(): The number of elements in each page of the result set.
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
