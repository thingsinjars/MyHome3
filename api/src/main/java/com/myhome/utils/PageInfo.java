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
 * total pages, and total elements in a given dataset. It generates an object with
 * four fields: currentPage, pageLimit, totalPages, and totalElements, which are
 * calculated from a Pageable object and a Page object using a constructor.
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
   * creates a `PageInfo` object containing information about the number of pages and
   * elements for a given pageable and page.
   * 
   * @param pageable page number and size of the page being processed, which is used
   * to calculate the total pages and elements in the PageInfo object.
   * 
   * 	- `getPageNumber()`: returns the page number of the current page being processed
   * 	- `getPageSize()`: returns the number of elements in a page
   * 	- `getTotalPages()`: returns the total number of pages in the collection
   * 	- `getTotalElements()`: returns the total number of elements in the collection
   * 
   * @param page current page of data being processed, providing the total number of
   * elements on that page.
   * 
   * 	- `pageNumber`: The number of the current page.
   * 	- `pageSize`: The size of the current page.
   * 	- `totalPages`: The total number of pages in the collection.
   * 	- `totalElements`: The total number of elements in the collection.
   * 
   * @returns a `PageInfo` object containing information about the page number, size,
   * total pages, and total elements of a pageable.
   * 
   * 	- `pageNumber`: The page number of the current page being displayed.
   * 	- `pageSize`: The number of elements on each page.
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
