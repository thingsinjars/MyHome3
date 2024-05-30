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
 * total pages, and total elements of a given Pageable and Page. The class offers a
 * constructor for creating instances from a Pageable and a Page object.
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
   * creates a `PageInfo` object containing information about the number of pages, page
   * size, total pages, and total elements of a given `Pageable` and `Page`.
   * 
   * @param pageable pagination information for the current page of data, providing the
   * page number, page size, total pages, and total elements.
   * 
   * 	- `getPageNumber()` returns the page number associated with the provided pageable
   * instance.
   * 	- `getPageSize()` returns the page size associated with the provided pageable instance.
   * 	- `getTotalPages()` returns the total number of pages associated with the provided
   * pageable instance.
   * 	- `getTotalElements()` returns the total number of elements associated with the
   * provided pageable instance.
   * 
   * @param page current page being processed, providing the total number of elements
   * on that page and the number of elements returned in the response.
   * 
   * 	- `pageNumber`: The number of the current page being displayed.
   * 	- `pageSize`: The number of elements in each page.
   * 	- `totalPages`: The total number of pages in the collection.
   * 	- `totalElements`: The total number of elements in the collection.
   * 
   * @returns a `PageInfo` object containing pagination metadata.
   * 
   * 1/ PageNumber - The number of the page being returned.
   * 2/ PageSize - The number of elements in each page.
   * 3/ TotalPages - The total number of pages in the dataset.
   * 4/ TotalElements - The total number of elements in the dataset.
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
