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
 * elements in a given dataset. It generates a `PageInfo` object from a `Pageable`
 * and a `Page`, allowing for efficient retrieval of pagination information.
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
   * transforms a `pageable` object and a `Page` object into a `PageInfo` object,
   * providing information about the page number, size, total pages, and total elements.
   * 
   * @param pageable Pageable object containing information about the current page of
   * data to be processed, which is used to calculate the page number, size, and total
   * pages and elements in the PageInfo object returned by the function.
   * 
   * 	- `getPageNumber()` returns the current page number being viewed.
   * 	- `getPageSize()` returns the total number of elements in the page.
   * 	- `getTotalPages()` returns the total number of pages available for the data set.
   * 	- `getTotalElements()` returns the total number of elements in the entire data set.
   * 
   * @param page current page being processed, providing information on its position
   * and size within the overall result set.
   * 
   * 1/ PageNumber: The page number for which the information is being provided.
   * 2/ PageSize: The size of the page being returned.
   * 3/ TotalPages: The total number of pages in the result set.
   * 4/ TotalElements: The total number of elements in the result set.
   * 
   * @returns a `PageInfo` object containing page number, size, total pages, and total
   * elements.
   * 
   * 1/ `pageNumber`: The number of the current page being displayed.
   * 2/ `pageSize`: The number of elements displayed on each page.
   * 3/ `totalPages`: The total number of pages in the result set.
   * 4/ `totalElements`: The total number of elements in the result set.
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
