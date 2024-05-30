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
 * total pages, and total elements in a given input. The class offers a constructor
 * for creating instances from a Pageable and a Page object, allowing for efficient
 * creation of `PageInfo` objects based on pagination-related data.
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
   * creates a `PageInfo` object containing information about the specified pageable
   * and page, such as page number, page size, total pages, and total elements.
   * 
   * @param pageable pagination information for the current page of data, providing the
   * page number and size.
   * 
   * The PageNumber property is an integer indicating how many pages the total number
   * of elements in the collection fits across.
   * 
   * The PageSize property is an integer indicating the number of elements that can be
   * displayed per page.
   * 
   * The TotalPages property is an integer indicating the total number of pages in a
   * sequence of pages for which all pages are available.
   * 
   * The TotalElements property is an integer indicating the total number of elements
   * present in the collection.
   * 
   * @param page current page of elements being processed, and provides the total number
   * of elements in the page.
   * 
   * 	- `pageNumber`: The page number of the result set.
   * 	- `pageSize`: The size of each page in the result set.
   * 	- `totalPages`: The total number of pages in the result set.
   * 	- `totalElements`: The total number of elements in the result set.
   * 
   * @returns a `PageInfo` object containing information about the page and total elements.
   * 
   * 	- The `pageNumber` attribute represents the page number of the result.
   * 	- The `pageSize` attribute represents the number of elements in each page.
   * 	- The `totalPages` attribute represents the total number of pages in the result.
   * 	- The `totalElements` attribute represents the total number of elements in the result.
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
