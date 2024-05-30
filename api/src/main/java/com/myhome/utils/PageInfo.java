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
   * generates a `PageInfo` object containing information about a given pageable and
   * page, including the page number, size, total pages, and total elements.
   * 
   * @param pageable pageable object that contains information about the current page
   * being processed, including the page number and size.
   * 
   * 	- `getPageNumber()` returns the page number of the current iteration.
   * 	- `getPageSize()` returns the total number of elements in the page.
   * 	- `getTotalPages()` returns the total number of pages available for the query.
   * 	- `getTotalElements()` returns the total number of elements in the result set.
   * 
   * @param page current page being processed, providing the number of elements on that
   * page and the total number of pages in the result set.
   * 
   * 	- `pageNumber`: The page number that contains the data being processed.
   * 	- `pageSize`: The size of the page being processed.
   * 	- `totalPages`: The total number of pages in the result set.
   * 	- `totalElements`: The total number of elements in all pages.
   * 
   * @returns a `PageInfo` object containing four properties: page number, page size,
   * total pages, and total elements.
   * 
   * 1/ Page number: The first property is the page number, which represents the position
   * of the page in the paginated sequence.
   * 2/ Page size: The second property is the page size, which indicates the number of
   * elements displayed on each page.
   * 3/ Total pages: The third property is the total number of pages in the paginated
   * sequence.
   * 4/ Total elements: The fourth property is the total number of elements in the
   * paginated sequence.
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
