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
 * dataset. It provides four fields: currentPage, pageLimit, totalPages, and
 * totalElements. The class also offers a constructor for creating instances from a
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
   * takes a `pageable` and a `page` as input, returning a `PageInfo` object containing
   * information about the page number, size, total pages, and total elements.
   * 
   * @param pageable pageable object that contains information about the current page
   * being processed, including the page number and size.
   * 
   * 	- `pageable.getPageNumber()`: The current page number being processed.
   * 	- `pageable.getPageSize()`: The number of elements per page.
   * 	- `page.getTotalPages()`: The total number of pages in the result set.
   * 	- `page.getTotalElements()`: The total number of elements in the result set.
   * 
   * @param page current page being processed, providing the total number of elements
   * on that page and the total number of pages in the overall result set.
   * 
   * 	- `pageNumber`: The page number being returned.
   * 	- `pageSize`: The number of elements in each page.
   * 	- `totalPages`: The total number of pages in the result set.
   * 	- `totalElements`: The total number of elements in the result set.
   * 
   * @returns a `PageInfo` object containing information about the current page and
   * total pages of a pageable sequence.
   * 
   * 	- PageNumber: The number of the current page being displayed.
   * 	- PageSize: The number of elements displayed per page.
   * 	- TotalPages: The total number of pages in the result set.
   * 	- TotalElements: The total number of elements in the result set.
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
