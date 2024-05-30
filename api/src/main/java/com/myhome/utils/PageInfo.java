package com.myhome.utils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * provides information about the number of pages, page size, total pages, and total
 * elements of a given Pageable and Page. It offers a constructor for creating instances
 * from a Pageable and a Page, as well as methods for generating a `PageInfo` object
 * containing these values.
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
   * takes a `pageable` and a `page` object as input, returning a `PageInfo` object
   * containing information about the page number, size, total pages, and total elements.
   * 
   * @param pageable pageable object that contains information about the current page
   * being processed, including its number and size.
   * 
   * 	- `getPageNumber()` - The page number being returned
   * 	- `getPageSize()` - The number of elements per page
   * 	- `getTotalPages()` - The total number of pages available for the result set
   * 	- `getTotalElements()` - The total number of elements in the result set
   * 
   * @param page current page being processed and provides the total number of elements
   * on that page.
   * 
   * 	- `pageNumber`: The number of the current page being processed.
   * 	- `pageSize`: The size of each page being processed.
   * 	- `totalPages`: The total number of pages in the dataset.
   * 	- `totalElements`: The total number of elements in the dataset.
   * 
   * @returns a `PageInfo` object containing page number, size, total pages, and total
   * elements.
   * 
   * 	- PageNumber: The number of the current page being displayed.
   * 	- PageSize: The size of each page of results displayed by the pagination.
   * 	- TotalPages: The total number of pages available for display.
   * 	- TotalElements: The total number of elements (e.g., documents, images, etc.) in
   * the paginated collection.
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
