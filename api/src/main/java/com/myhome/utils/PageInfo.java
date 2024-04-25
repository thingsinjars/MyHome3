package com.myhome.utils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * is a data structure that contains information about a page of results in a larger
 * dataset. It has four fields: currentPage, pageLimit, totalPages, and totalElements.
 * The class also provides a constructor for creating instances of the class from a
 * Pageable object and a Page object.
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
   * page size, total pages, and total elements of a given `Pageable` and `Page`.
   * 
   * @param pageable pageable object that contains information about the current page
   * being processed, including the page number and size.
   * 
   * 	- The `getPageNumber()` method returns the page number.
   * 	- The `getPageSize()` method returns the page size.
   * 	- The `getTotalPages()` method returns the total number of pages.
   * 	- The `getTotalElements()` method returns the total number of elements in the page.
   * 
   * @param page current page of data being processed, providing the total number of
   * elements on that page.
   * 
   * 	- The `pageNumber` attribute indicates the page number in the pagination sequence.
   * 	- The `pageSize` attribute signifies the number of elements that can be displayed
   * on a single page.
   * 	- The `totalPages` attribute represents the total number of pages available for
   * the given input.
   * 	- The `totalElements` attribute indicates the overall number of elements in the
   * input.
   * 
   * @returns a `PageInfo` object containing page number, size, total pages, and total
   * elements.
   * 
   * 	- `pageNumber`: The page number of the resultant page.
   * 	- `pageSize`: The size of each page in the resultant page.
   * 	- `totalPages`: The total number of pages in the resultant page.
   * 	- `totalElements`: The total number of elements in the resultant page.
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
