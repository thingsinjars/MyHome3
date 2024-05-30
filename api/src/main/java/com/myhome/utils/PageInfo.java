package com.myhome.utils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * is a data structure that holds information about a page of results in a larger
 * dataset. It has four fields: currentPage, pageLimit, totalPages, and totalElements.
 * Additionally, it provides constructors for creating instances of the class from a
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
   * creates a `PageInfo` object with information about the number of pages, page size,
   * total pages, and total elements for a given `Pageable` and `Page`.
   * 
   * @param pageable pageable object that contains information about the current page
   * being processed, including its number and size.
   * 
   * 	- `getPageNumber()`: The page number of the current page being processed.
   * 	- `getPageSize()`: The size of each page in elements.
   * 	- `getTotalPages()`: The total number of pages in the collection.
   * 	- `getTotalElements()`: The total number of elements in the collection.
   * 
   * @param page current page of data being processed, providing information on its
   * number, size, total pages, and total elements.
   * 
   * 	- The page number is contained in the Pageable object's `getPageNumber()` method.
   * 	- The size of the page is contained in the Pageable object's `getPageSize()` method.
   * 	- The overall number of pages is contained in the `getTotalPages()` method of the
   * input `page`.
   * 	- The total quantity of elements on each page is contained in the `getTotalElements()`
   * method of the input `page`.
   * 
   * @returns a `PageInfo` object containing various information about the page of data.
   * 
   * 1/ pageable.getPageNumber(): The number of the current page being displayed.
   * 2/ pageable.getPageSize(): The number of elements displayed per page.
   * 3/ page.getTotalPages(): The total number of pages in the result set.
   * 4/ page.getTotalElements(): The total number of elements in the result set.
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
