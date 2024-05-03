package com.myhome.utils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 
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
   * page size, total pages, and total elements of a Pageable object and its corresponding
   * Page object.
   * 
   * @param pageable pagination context, providing the number of pages and elements per
   * page for the page of data being processed.
   * 
   * 	- `getPageNumber()`: A page number representing the position of the current page
   * in the paginated sequence.
   * 	- `getPageSize()`: The number of elements that can be displayed on a single page.
   * 	- `getTotalPages()`: The total number of pages in the paginated sequence.
   * 	- `getTotalElements()`: The total number of elements in the paginated sequence.
   * 
   * @param page current page of data being processed, providing the total number of
   * elements on that page.
   * 
   * 	- `pageNumber`: The number of the current page being displayed.
   * 	- `pageSize`: The size of each page being displayed.
   * 	- `totalPages`: The total number of pages available for display.
   * 	- `totalElements`: The total number of elements in the input data.
   * 
   * @returns a `PageInfo` object containing information about the page number, size,
   * total pages, and total elements of a Pageable and a Page.
   * 
   * 	- pageable.getPageNumber(): The zero-based index of the current page being displayed.
   * 	- pageable.getPageSize(): The number of elements in each page.
   * 	- page.getTotalPages(): The total number of pages in the result set.
   * 	- page.getTotalElements(): The total number of elements returned by the query.
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
