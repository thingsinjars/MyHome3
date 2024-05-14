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
 * The class also provides constructors for creating instances of the class from a
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
   * transforms a `Pageable` and a `Page` into a `PageInfo` object containing information
   * about the page number, size, total pages, and total elements.
   * 
   * @param pageable page request, providing the page number and size for the page of
   * data to be processed.
   * 
   * 	- `pageNumber`: The number of the current page being served.
   * 	- `pageSize`: The number of elements in each page.
   * 	- `totalPages`: The total number of pages available for the given input `pageable`.
   * 	- `totalElements`: The total number of elements returned by the `pageable`.
   * 
   * @param page current page of data being processed, providing information on its
   * position within the total number of pages and the number of elements it contains.
   * 
   * 1/ `pageNumber`: The page number that corresponds to this page in the paginated result.
   * 2/ `pageSize`: The size of each page in the paginated result.
   * 3/ `totalPages`: The total number of pages in the paginated result.
   * 4/ `totalElements`: The total number of elements in the paginated result.
   * 
   * @returns a `PageInfo` object containing information about the page number, size,
   * total pages, and total elements of the pageable.
   * 
   * 1/ PageNumber: The zero-based index of the page being referenced, representing the
   * position of the page in the paginated sequence.
   * 2/ PageSize: The number of elements per page, indicating the number of items
   * displayed on each page.
   * 3/ TotalPages: The total number of pages in the paginated sequence, including the
   * current page and all subsequent pages.
   * 4/ TotalElements: The total number of elements across all pages in the paginated
   * sequence, providing a cumulative count of the entire dataset.
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
