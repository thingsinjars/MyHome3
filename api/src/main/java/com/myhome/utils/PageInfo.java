package com.myhome.utils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * is designed to provide information about the number of pages, page size, total
 * pages, and total elements of a given `Pageable` and `Page`. The class offers a
 * constructor for creating instances from a `Pageable` and `Page`, as well as a
 * method for generating a `PageInfo` object based on these inputs.
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
   * size of each page, total pages, and total elements in a pageable.
   * 
   * @param pageable Pageable interface, which provides methods for retrieving a page
   * of data from a source.
   * 
   * 	- `getPageNumber()` returns the page number of the current page being processed.
   * 	- `getPageSize()` returns the size of the page being processed.
   * 	- `getTotalPages()` returns the total number of pages in the dataset.
   * 	- `getTotalElements()` returns the total number of elements in the dataset.
   * 
   * @param page current page being processed, providing information on its position,
   * size, total pages, and total elements.
   * 
   * 	- `pageNumber`: The number of the current page being displayed.
   * 	- `pageSize`: The number of elements displayed per page.
   * 	- `totalPages`: The total number of pages in the result set.
   * 	- `totalElements`: The total number of elements in the result set.
   * 
   * @returns a `PageInfo` object containing information about the page of elements.
   * 
   * 	- The page number represents the position of the current page in the paginated sequence.
   * 	- The page size indicates the number of elements displayed per page.
   * 	- The total pages indicate the total number of pages available for display.
   * 	- The total elements represent the total number of elements displayed across all
   * pages.
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
