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
 * `PageInfo` object containing page number, size, total pages, and total elements
 * using a constructor that takes in a Pageable and Page object.
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
   * page size, total pages, and total elements for a given pageable and page.
   * 
   * @param pageable pagination context, providing information about the number of pages
   * and elements in each page.
   * 
   * 1/ The page number is returned as an integer indicating the position of the current
   * page in the paginated data set.
   * 2/ The page size, which represents the number of elements that can be displayed
   * on a single page, is also included as an integer.
   * 3/ The total number of pages in the dataset is represented by the `totalPages`
   * property, also an integer.
   * 4/ The total number of elements in the entire dataset is provided by the `totalElements`
   * property, which is also an integer.
   * 
   * @param page current page being processed, providing the total number of elements
   * on that page.
   * 
   * 	- `pageNumber`: The page number that the input belongs to.
   * 	- `pageSize`: The size of the page that the input belongs to.
   * 	- `totalPages`: The total number of pages in the result set.
   * 	- `totalElements`: The total number of elements in the result set.
   * 
   * @returns a `PageInfo` object containing information about the current page of results.
   * 
   * 	- pageable.getPageNumber(): The page number of the current page being processed.
   * 	- pageable.getPageSize(): The size of the current page being processed.
   * 	- page.getTotalPages(): The total number of pages in the result set.
   * 	- page.getTotalElements(): The total number of elements in the result set.
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
