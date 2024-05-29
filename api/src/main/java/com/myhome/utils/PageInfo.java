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
 * totalElements. Additionally, it offers a constructor for creating instances of the
 * class from a Pageable object and a Page object.
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
   * takes a `pageable` and a `page` object as input, and returns a `PageInfo` object
   * that contains information about the page number, page size, total pages, and total
   * elements.
   * 
   * @param pageable pageable object that contains information about the pagination of
   * the data, including the current page number and total pages.
   * 
   * 	- `getPageNumber(): int`: The page number that corresponds to the current page
   * being displayed.
   * 	- `getPageSize(): int`: The number of elements in a page.
   * 	- `getTotalPages(): int`: The total number of pages in the result set.
   * 	- `getTotalElements(): long`: The total number of elements in the result set.
   * 
   * @param page current page being processed, providing the total number of elements
   * on that page.
   * 
   * 	- `pageNumber`: The page number of the result set, which is also the zero-based
   * index of the current page.
   * 	- `pageSize`: The number of elements in a page of the result set.
   * 	- `totalPages`: The total number of pages in the result set.
   * 	- `totalElements`: The total number of elements in the result set.
   * 
   * @returns a `PageInfo` object containing pagination metadata.
   * 
   * 	- PageNumber: The number of the page being returned.
   * 	- PageSize: The number of elements in each page.
   * 	- TotalPages: The total number of pages in the dataset.
   * 	- TotalElements: The total number of elements in the dataset.
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
