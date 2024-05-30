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
 * dataset. It has four fields: currentPage, pageLimit, totalPages, and totalElements.
 * The class also provides constructors for creating instances from a Pageable object
 * and a Page object.
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
   * generates a `PageInfo` object representing the number of pages, size of each page,
   * total number of pages, and total elements for a given `Pageable` and `Page`.
   * 
   * @param pageable Pageable object that contains information about the current page
   * of data being processed, including the page number and the total number of pages
   * in the dataset.
   * 
   * 1/ PageNumber: The page number of the result set.
   * 2/ PageSize: The number of elements in each page of the result set.
   * 3/ TotalPages: The total number of pages in the result set.
   * 4/ TotalElements: The total number of elements in the result set.
   * 
   * @param page current page being processed, providing information on its position,
   * size, and total counts within the overall result set.
   * 
   * 	- `pageNumber`: The page number of the result set.
   * 	- `pageSize`: The size of each page in the result set.
   * 	- `totalPages`: The total number of pages in the result set.
   * 	- `totalElements`: The total number of elements in the result set.
   * 
   * @returns a `PageInfo` object containing information about the current page of a
   * paginated result set.
   * 
   * 1/ PageNumber - The page number of the pageable input.
   * 2/ PageSize - The size of the pageable input.
   * 3/ TotalPages - The total number of pages in the paginated data set.
   * 4/ TotalElements - The total number of elements in the paginated data set.
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
