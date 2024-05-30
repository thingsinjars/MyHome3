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
 * The class also provides constructors for creating instances from a Pageable object
 * and a Page object.
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
   * size, total pages, and total elements for a given pageable and page.
   * 
   * @param pageable pageable object that contains information about the current page
   * of data being processed, including the page number and size.
   * 
   * 	- `pageNumber`: The number of the page being accessed.
   * 	- `pageSize`: The size of each page being accessed.
   * 	- `totalPages`: The total number of pages in the dataset.
   * 	- `totalElements`: The total number of elements in the dataset.
   * 
   * @param page current page of elements being processed, providing the total number
   * of elements and pages involved in the process.
   * 
   * 	- `pageNumber`: The number of the page being returned.
   * 	- `pageSize`: The number of elements in the page.
   * 	- `totalPages`: The total number of pages in the result set.
   * 	- `totalElements`: The total number of elements in the result set.
   * 
   * @returns a `PageInfo` object containing information about the current page of data.
   * 
   * 	- The first parameter, `pageable`, represents the pageable object containing
   * information about the current page being processed.
   * 	- The second parameter, `page`, refers to the specific page being processed.
   * 	- The return value is a `PageInfo` object that contains four attributes: page
   * number, page size, total pages, and total elements. These values are obtained from
   * the pageable and page objects provided as inputs.
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
