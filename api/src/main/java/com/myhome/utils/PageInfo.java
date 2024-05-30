package com.myhome.utils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * is used to generate information about the number of pages, page size, total pages,
 * and total elements of a given Pageable and Page. The class provides a constructor
 * for creating instances from a Pageable and a Page object, and also offers a static
 * method for generating a PageInfo object based on a Pageable and a Page.
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
   * generates a `PageInfo` object containing information about the page number, size,
   * total pages, and total elements of a given `Pageable` and `Page`.
   * 
   * @param pageable pageable object that contains information about the current page
   * being processed, including its number and size.
   * 
   * 	- `getPageNumber()`: This returns the page number of the current page being processed.
   * 	- `getPageSize()`: This returns the size of a single page in the paginated collection.
   * 	- `getTotalPages()`: This returns the total number of pages in the entire collection.
   * 	- `getTotalElements()`: This returns the total number of elements in the entire
   * collection, including those on each page.
   * 
   * @param page current page being processed, providing information on its number,
   * size, total pages, and total elements.
   * 
   * 	- `pageNumber`: The page number in which the element is located.
   * 	- `pageSize`: The number of elements per page.
   * 	- `totalPages`: The total number of pages in the result set.
   * 	- `totalElements`: The total number of elements in the result set.
   * 
   * @returns a `PageInfo` object containing information about the page number, size,
   * total pages, and total elements of a given pageable.
   * 
   * 1/ pageNumber: The number of the current page being displayed.
   * 2/ pageSize: The number of elements in each page.
   * 3/ totalPages: The total number of pages available for display.
   * 4/ totalElements: The total number of elements in the entire dataset.
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
