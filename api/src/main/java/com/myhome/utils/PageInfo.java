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
 * total pages, and total elements of a given Pageable and Page object. The class
 * provides a constructor for creating instances of the class from a Pageable and a
 * Page object, and also offers a method for generating a PageInfo object based on
 * the current page being processed and the total number of pages and elements available.
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
   * takes a `pageable` and a `page` as input, and returns a `PageInfo` object containing
   * information about the page number, size, total pages, and total elements.
   * 
   * @param pageable pagination information for the current page of data, providing the
   * page number, page size, total pages, and total elements for the page.
   * 
   * 	- `getPageNumber()`: returns the current page number being displayed.
   * 	- `getPageSize()`: returns the number of elements per page.
   * 	- `getTotalPages()`: returns the total number of pages in the result set.
   * 	- `getTotalElements()`: returns the total number of elements in the result set.
   * 
   * @param page current page being processed, providing information on its position
   * and size within the total number of pages and elements.
   * 
   * 	- `pageNumber`: The page number of the page being returned.
   * 	- `pageSize`: The number of elements in each page of the result.
   * 	- `totalPages`: The total number of pages in the result set.
   * 	- `totalElements`: The total number of elements in the result set.
   * 
   * @returns a `PageInfo` object containing information about the page number, size,
   * total pages, and total elements of a pageable.
   * 
   * 	- `pageNumber`: The number of the page being referenced.
   * 	- `pageSize`: The size of each page being referenced.
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
