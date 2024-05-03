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
 * total pages, and total elements of a given Pageable and Page. The class offers a
 * constructor for creating instances of the class from a Pageable and a Page object,
 * and includes methods for generating a PageInfo object containing page number, size,
 * total pages, and total elements of a given Pageable and Page.
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
   * @param pageable page number and size of the page being processed, which are used
   * to calculate the total pages and elements in the page.
   * 
   * 	- The PageNumber property represents the page number of the result set being processed.
   * 	- The PageSize property signifies the number of elements in each page.
   * 	- The TotalPages property indicates the total number of pages in the result set.
   * 	- The TotalElements property represents the total number of elements in the entire
   * result set.
   * 
   * @param page current page being processed, providing information on its position
   * and size within the overall page set.
   * 
   * 	- `pageNumber`: The page number that this page represents.
   * 	- `pageSize`: The size of this page.
   * 	- `totalPages`: The total number of pages in the result set.
   * 	- `totalElements`: The total number of elements in the result set.
   * 
   * @returns a `PageInfo` object containing page number, size, total pages, and total
   * elements.
   * 
   * 	- The first item in the list is pageNumber, which represents the number of the
   * current page being displayed.
   * 	- pageSize contains the total number of elements that can be displayed on a single
   * page.
   * 	- The third element is totalPages, which displays the entire number of pages
   * available for the given input.
   * 	- totalElements represents the entire number of elements in the result set.
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
