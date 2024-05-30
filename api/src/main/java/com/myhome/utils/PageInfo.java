package com.myhome.utils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * is used to represent information about a page of results in a larger dataset,
 * including the current page number, page size, total pages, and total elements. The
 * class provides a constructor for creating instances of the class from a Pageable
 * and a Page object, as well as a method for generating a PageInfo object based on
 * a given Pageable and Page.
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
   * takes a `pageable` and a `page` as input and returns a `PageInfo` object containing
   * various information about the page, including the page number, size, total pages,
   * and total elements.
   * 
   * @param pageable pagination information for the given `Page` object.
   * 
   * 	- `getPageNumber()`: The page number of the current page being processed.
   * 	- `getPageSize()`: The number of elements in a single page.
   * 	- `getTotalPages()`: The total number of pages in the result set.
   * 	- `getTotalElements()`: The total number of elements in the result set.
   * 
   * @param page current page being processed, providing information on its number,
   * size, total pages, and total elements.
   * 
   * 	- `pageNumber`: The page number that the current page belongs to.
   * 	- `pageSize`: The size of each page in elements.
   * 	- `totalPages`: The total number of pages in the result set.
   * 	- `totalElements`: The total number of elements returned by the query.
   * 
   * @returns a `PageInfo` object containing page number, page size, total pages, and
   * total elements.
   * 
   * 	- The page number is specified as the first attribute of the PageInfo object.
   * This value represents the current page being displayed to the user.
   * 	- The page size is the second attribute, which indicates the number of elements
   * displayed on each page.
   * 	- The total pages attribute specifies the total number of pages in the result set.
   * 	- The total elements attribute specifies the overall number of elements in the
   * result set.
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
