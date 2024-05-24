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
 * The class also provides constructors for creating instances of the class from a
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
   * takes a `pageable` and a `page` as input, and returns a `PageInfo` object containing
   * information about the page number, size, total pages, and total elements.
   * 
   * @param pageable pagination information for the page being processed, providing the
   * page number, page size, total pages, and total elements.
   * 
   * 	- `getPageNumber()`: The page number of the current page being accessed.
   * 	- `getPageSize()`: The number of elements in a single page of the data.
   * 	- `getTotalPages()`: The total number of pages in the dataset.
   * 	- `getTotalElements()`: The total number of elements in the dataset.
   * 
   * @param page current page being processed, providing information on its position,
   * size, and total pages and elements in the result set.
   * 
   * 	- `pageNumber`: The number of the page being returned.
   * 	- `pageSize`: The number of elements in each page.
   * 	- `totalPages`: The total number of pages in the result set.
   * 	- `totalElements`: The total number of elements in the result set.
   * 
   * @returns a `PageInfo` object containing various information about the pagination
   * of the input `Pageable` and `Page`.
   * 
   * 	- The first argument, `pageable`, represents the pagination state, containing
   * information such as the current page number and page size.
   * 	- The second argument, `page`, is the current page being processed, containing
   * information such as the total number of pages and elements in the dataset.
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
