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
 * total pages, and total elements of a given Pageable and Page. It generates a
 * PageInfo object containing these details by using constructor arguments.
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
   * takes a `pageable` parameter and a `page` parameter, and returns a `PageInfo`
   * object containing information about the page number, page size, total pages, and
   * total elements.
   * 
   * @param pageable Pageable object containing information about the pagination of the
   * data, which is used to calculate the page number, size, total pages, and total
   * elements for the resulting PageInfo object.
   * 
   * 	- `getPageNumber()`: Returns the page number of the current page being processed.
   * 	- `getPageSize()`: Returns the size of a single page in the paginated result.
   * 	- `getTotalPages()`: Returns the total number of pages in the result set.
   * 	- `getTotalElements()`: Returns the total number of elements in the result set.
   * 
   * @param page current page of elements being processed, providing the total number
   * of elements on that page and the total number of pages available.
   * 
   * 	- `pageNumber`: The page number of the current page being processed.
   * 	- `pageSize`: The number of elements in each page of the result set.
   * 	- `totalPages`: The total number of pages in the result set.
   * 	- `totalElements`: The total number of elements in the result set.
   * 
   * @returns a `PageInfo` object containing page number, page size, total pages, and
   * total elements.
   * 
   * 	- The page number is represented by the first parameter, which is an integer
   * indicating the current page being accessed.
   * 	- The page size is the second input value, which is an integer representing the
   * total number of items that can be displayed on a single page.
   * 	- The total pages and total elements are the third and fourth inputs, respectively,
   * which provide information about the entire dataset that the user has access to.
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
