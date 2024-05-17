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
 * and also offers a method for generating a PageInfo object containing information
 * about the current page being processed.
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
   * size, total pages, and total elements for a given `Pageable` and `Page`.
   * 
   * @param pageable Pageable object that contains information about the current page
   * of data being processed.
   * 
   * 	- `getPageNumber()` returns the current page number.
   * 	- `getPageSize()` returns the number of elements in each page.
   * 	- `getTotalPages()` returns the total number of pages in the result set.
   * 	- `getTotalElements()` returns the total number of elements in the result set.
   * 
   * @param page current page being processed, providing the total number of elements
   * on that page.
   * 
   * 	- `getPageNumber()`: The page number that this PageInfo represents.
   * 	- `getPageSize()`: The number of elements in each page of the data.
   * 	- `getTotalPages()`: The total number of pages in the data.
   * 	- `getTotalElements()`: The total number of elements in the data.
   * 
   * @returns a `PageInfo` object containing information about the page number, size,
   * total pages, and total elements of a given pageable.
   * 
   * 	- The first field is `pageNumber`, which represents the current page number being
   * displayed.
   * 	- The second field is `pageSize`, which indicates the number of elements displayed
   * on each page.
   * 	- The third field is `totalPages`, which represents the total number of pages in
   * the dataset.
   * 	- The fourth field is `totalElements`, which signifies the total number of elements
   * in the dataset.
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
